package ua.artcode.controller;

import com.mongodb.DuplicateKeyException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import ua.artcode.exception.*;
import ua.artcode.model.common.Course;
import ua.artcode.model.common.Lesson;
import ua.artcode.model.common.Task;
import ua.artcode.model.common.User;
import ua.artcode.model.taskComponent.TaskTestResult;
import ua.artcode.service.AdminService;
import ua.artcode.service.TeacherService;
import ua.artcode.service.UserService;
import ua.artcode.to.Message;
import ua.artcode.to.MessageType;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/lesson-menu")
public class LessonController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private AdminService adminService;

    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;

    @RequestMapping(value = "/create-lesson")
    public ModelAndView addLesson() {
        ModelAndView mav = new ModelAndView("main/create-lesson");
        mav.addObject("lesson", new Lesson());
        return mav;
    }

    @RequestMapping(value = "/add-lesson", method = RequestMethod.POST)
    public ModelAndView createLesson(@Valid Lesson lesson,
                                     BindingResult result,
                                     HttpServletRequest req,
                                     RedirectAttributes redirectAttributes,
                                     Principal principal) {
        ModelAndView mav = new ModelAndView("lesson/create-lesson");
        if (!result.hasErrors()) {
            try {

                lesson.setOwner(userService.findUser(principal.getName()));

                String tasksTitle = req.getParameter("lessonTasks");
                String[] titles = tasksTitle.split(",\\s");
                Arrays.stream(titles).forEach((task) -> {
                    try {
                        lesson.getTasks().add(adminService.findTaskByTitle(task));
                    } catch (NoSuchTaskException e) {
                        e.printStackTrace();
                    }
                });

                teacherService.addLesson(lesson);
                redirectAttributes.addFlashAttribute("message", "The lesson has been successfully created.");
                mav.setViewName("redirect:/lesson-menu");

            } catch (DuplicateKeyException e) {
                e.printStackTrace();
                mav.addObject("message", "Lesson with title: " + lesson.getTitle() + " already exist!");
            } catch (NoSuchUserException e) {
                e.printStackTrace();
                mav.addObject("message", "User is not in system");
            }
        }
        return mav;
    }

    //
    @RequestMapping(value = "/add-lesson/json", method = RequestMethod.POST)
    public @ResponseBody Message createLessonJson(@Valid Lesson lesson, BindingResult result,
                                                  HttpServletRequest req,
                                                  RedirectAttributes redirectAttributes) {
        if (!result.hasErrors()) {
            try {
                String tasksTitle = req.getParameter("tasks");
                String[] titles = tasksTitle.split(",\\s");
                Arrays.stream(titles).forEach((task) -> {
                    try {
                        lesson.getTasks().add(adminService.findTaskByTitle(task));
                    } catch (NoSuchTaskException e) {
                        e.printStackTrace();
                    }
                });

                teacherService.addLesson(lesson);
                return new Message("Success", MessageType.INFO, "Lesson " + lesson.getTitle() + " created");
            } catch (DuplicateKeyException e) {
                e.printStackTrace();
                // todo use logger
                return new Message(e.getMessage(), MessageType.ERROR, e.getMessage());
            }

        } else {
            return new Message("Validation Error", MessageType.ERROR, result.toString());
        }
    }

    @RequestMapping(value = "/setup-tasks")
    public ModelAndView setupTasks(HttpServletRequest req, RedirectAttributes attributes) {
        ModelAndView mav = new ModelAndView("lesson/setup-tasks");
        Map<String, ?> map = RequestContextUtils.getInputFlashMap(req);
        if (map != null) {
            mav.addObject("title047163Lesson", map.get("title"));
            mav.addObject("tasks", map.get("tasks"));
        } else {
            attributes.addFlashAttribute("message", "Lesson created successfully.");
            mav.setViewName("redirect:/lesson-menu");
        }
        return mav;
    }

    @RequestMapping(value = "/add-task")
    public ModelAndView addTask(RedirectAttributes redirectAttributes, HttpServletRequest req) {
        ModelAndView mav = new ModelAndView("main/lesson-menu");
        try {
            List<Task> tasks = adminService.getAllTasks();
            String title = req.getParameter("title047163Lesson");
            Lesson lesson = teacherService.findLessonByTitle(title);
            List<Task> list = lesson.getTasks();
            for (Task task : tasks) {
                if (req.getParameter(task.getTitle()) != null) {
                    list.add(task);
                }
            }
            lesson.setTasks(list);
            teacherService.updateLesson(lesson.getId(), lesson);
            redirectAttributes.addFlashAttribute("message", "The lesson has been successfully created.");
            mav.setViewName("redirect:/lesson-menu");
        } catch (NoSuchLessonException e) {
            mav.addObject("message", e.getMessage());
        } catch (DuplicateDataException e) {
            mav.addObject("message", e.getMessage());
        }
        return mav;
    }

    @RequestMapping(value = "/edit-lesson", method = RequestMethod.POST)
    public ModelAndView editLesson(HttpServletRequest req) {
        ModelAndView mav = new ModelAndView("lesson/edit-lesson");
        String id = req.getParameter("id");
        try {
            Lesson lesson = teacherService.findLessonById(new ObjectId(id));
            List<Task> tasksOnLesson = lesson.getTasks();
            List<Task> allTasks = adminService.getAllTasks();
            allTasks.removeAll(tasksOnLesson);

            mav.addObject("lesson", lesson);
            mav.addObject("tasksInLesson", tasksOnLesson);
            mav.addObject("allTasks", allTasks);
        } catch (NoSuchLessonException e) {
            mav.setViewName("lesson/list-lessons");
            mav.addObject("message", e.getMessage());
        }
        return mav;
    }

    @RequestMapping(value = "/update-lesson")
    public ModelAndView updateLesson(@Valid Lesson lesson, BindingResult result, HttpServletRequest req, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView("lesson/edit-lesson");
        List<Task> taskInLesson = new ArrayList<>();
        List<Task> allTasks = adminService.getAllTasks();
        for (Task task : allTasks) {
            if (req.getParameter(task.getTitle()) != null) {
                taskInLesson.add(task);
            }
        }
        if (result.hasErrors()) {
            allTasks.removeAll(taskInLesson);

            mav.addObject("tasksInLesson", taskInLesson);
            mav.addObject("allTasks", allTasks);

        } else {
            try {
                lesson.setTasks(taskInLesson);
                teacherService.updateLesson(lesson.getId(), lesson);
                redirectAttributes.addFlashAttribute("message", "The lesson has been successfully updated.");
                mav.setViewName("redirect:/lesson-menu");
            } catch (DuplicateDataException e) {
                mav.addObject("message", e.getMessage());
                mav.setViewName("lesson/edit-lesson");
                mav.addObject("tasksInLesson", taskInLesson);
                mav.addObject("allTasks", allTasks);
            } catch (NoSuchLessonException e) {
                mav.addObject("message", e.getMessage());
                mav.setViewName("main/lesson-menu");
            }
        }
        return mav;
    }

    @RequestMapping(value = "/show-lessons")
    public ModelAndView showLessons() {
        ModelAndView mav = new ModelAndView("lesson/list-lessons");
        mav.addObject("lessons", teacherService.getAllLessons());
        return mav;
    }

    @RequestMapping(value = "/find-lesson")
    public String findLesson() {
        return "lesson/find-lesson";
    }

    @RequestMapping(value = "/show-lesson/{title}")
    public ModelAndView showLesson(@PathVariable String title,
                                   @RequestParam(name = "courseId", required = false) String courseId,
                                   Principal principal) {
        ModelAndView mav = new ModelAndView("main/show-lesson");
        try {

            User user = userService.findUser(principal.getName());
            Lesson lesson = teacherService.findLessonByTitle(title);

            mav.addObject(lesson);

            List<Task> tasks = lesson.getTasks();
            tasks.stream().forEach(task -> {
                TaskTestResult taskTestResult = user.getSolvedTaskContainer().get(task.getId());
                task.setPerformed(taskTestResult != null && taskTestResult.getPassedAll());
            });


            mav.addObject("tasks", tasks);

            //todo think about adding Course field to Lesson
            if(courseId != null && !courseId.isEmpty()){
                try {
                    Course course = teacherService.findCourseById(new ObjectId(courseId));
                    mav.addObject("courseTitle", course.getTitle());
                } catch (NoSuchCourseException e) {
                    e.printStackTrace();
                }
            }


        } catch (NoSuchLessonException e) {
            mav.addObject("message", e.getMessage());
            mav.setViewName("lesson/list-lessons");
        } catch (NoSuchUserException e) {
            e.printStackTrace();
            mav.addObject("message", e.getMessage());
        }
        return mav;
    }

    @RequestMapping(value = "/show-lesson", method = RequestMethod.POST)
    public ModelAndView showLessonPost(HttpServletRequest req) {
        ModelAndView mav = new ModelAndView("lesson/show-lesson");
        try {
            String title = req.getParameter("title");
            Lesson lesson = teacherService.findLessonByTitle(title);
            mav.addObject("lesson", lesson);
            mav.addObject("tasks", lesson.getTasks());
        } catch (NoSuchLessonException e) {
            mav.setViewName("lesson/find-lesson");
            mav.addObject("error", e.getMessage());
        }
        return mav;
    }

    @RequestMapping(value = "/delete-lesson")
    public String deleteLessonForm() {
        return "lesson/delete-lesson";
    }

    @RequestMapping(value = "/delete-form")
    public ModelAndView deleteLesson(HttpServletRequest req, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView("redirect:/lesson-menu");
        try {
            String lessonTitle = req.getParameter("lessonTitle");
            teacherService.deleteLessonByTitle(lessonTitle);
            redirectAttributes.addFlashAttribute("message", "The lesson has been successfully deleted.");
        } catch (NoSuchLessonException e) {
            mav.addObject("message", e.getMessage());
            mav.setViewName("lesson/delete-lesson");
        }
        return mav;
    }

}
