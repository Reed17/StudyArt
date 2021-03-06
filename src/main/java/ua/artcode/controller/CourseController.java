package ua.artcode.controller;

import com.mongodb.DuplicateKeyException;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import ua.artcode.exception.DuplicateDataException;
import ua.artcode.exception.NoSuchCourseException;
import ua.artcode.exception.NoSuchLessonException;
import ua.artcode.exception.NoSuchUserException;
import ua.artcode.model.common.Course;
import ua.artcode.model.common.Lesson;
import ua.artcode.model.common.User;
import ua.artcode.service.AdminService;
import ua.artcode.service.TeacherService;
import ua.artcode.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping(value = "/course-menu")
public class CourseController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private AdminService adminService;

    @RequestMapping(value = "/create-course")
    public ModelAndView addCourse() {
        ModelAndView mav = new ModelAndView("main/create-course");
        mav.addObject("course", new Course());
        return mav;
    }

    @RequestMapping(value = "/subscribe/{courseTitle}")
    public String subscribe(@PathVariable("courseTitle") String courseTitle, Principal principal) {

        try {
            User currentUser = userService.findUser(principal.getName());
            Course course = teacherService.findCourseByTitle(courseTitle);
            currentUser.getSubscribedCourses().add(course);

            userService.update(currentUser.getEmail(), currentUser);

        } catch (NoSuchUserException | NoSuchCourseException | DuplicateDataException e) {
            e.printStackTrace();
        }


        return "redirect:/course-menu/show-course/" + courseTitle;

    }

    @RequestMapping(value = "/add-course", method = RequestMethod.POST)
    public ModelAndView createCourse(@Valid Course course, BindingResult result, HttpServletRequest req,
                                     RedirectAttributes redirectAttributes,
                                     Principal principal) {
        ModelAndView mav = new ModelAndView();

        if (!result.hasErrors()) {
            try {

                User user = userService.findUser(principal.getName());
                course.setOwner(user);

                String courseLessons = req.getParameter("courseLessons");

                String[] titles = courseLessons.split(",\\s");
                Arrays.stream(titles).forEach((lessonTitle) -> {
                    try {
                        course.getLessonList().add(teacherService.findLessonByTitle(lessonTitle));
                    } catch (NoSuchLessonException e) {
                        e.printStackTrace();
                    }
                });

                teacherService.addCourse(course);

                mav.addObject("subscribed", user.getSubscribedCourses().contains(course));
                userService.addUserCourseStatInformation(user, course);

                mav.addObject("message", messageSource.getMessage("course.successfully.create",
                        null, LocaleContextHolder.getLocale()));

                mav.addObject("course", course);

                mav.setViewName("main/show-course");

            } catch (DuplicateKeyException e) {
                mav.addObject("message", "Course with title: " + course.getTitle() + " already exist!");
                mav.setViewName("main/create-course");
            } catch (NoSuchUserException e) {
                e.printStackTrace();
                mav.addObject("message", e.getMessage());
                mav.setViewName("main/create-course");
            }
        }



        return mav;
    }

    @RequestMapping(value = "/setup-lessons")
    public ModelAndView setupLessons(HttpServletRequest req) {
        ModelAndView mav = new ModelAndView("course/setup-lessons");
        Map<String, ?> map = RequestContextUtils.getInputFlashMap(req);
        if (map != null) {
            // todo hardcode parameter, why do we need it?
            mav.addObject("title639824Course", map.get("title"));
            mav.addObject("lessons", map.get("lessons"));
        } else {
            mav.setViewName("redirect:/course-menu");
        }
        return mav;
    }

    @RequestMapping(value = "/add-lesson")
    public ModelAndView addLesson(RedirectAttributes redirectAttributes, HttpServletRequest req) {
        ModelAndView mav = new ModelAndView("main/course-menu");

        try {
            List<Lesson> allLessons = teacherService.getAllLessons();
            // todo hardcode parameter, why do we need it?
            String title = req.getParameter("title639824Course");

            Course course = teacherService.findCourseByTitle(title);
            List<Lesson> lessonsForCourse = new ArrayList<>();
            for (Lesson lesson : allLessons) {
                if (req.getParameter(lesson.getTitle()) != null) {
                    lessonsForCourse.add(lesson);
                }
            }
            course.setLessonList(lessonsForCourse);
            teacherService.updateCourse(course.getId(), course);
            redirectAttributes.addFlashAttribute("message", "Course has been successfully created.");
            mav.setViewName("redirect:/course-menu");
        } catch (DuplicateDataException e) {
            mav.addObject("message", e.getMessage());
        } catch (NoSuchCourseException e) {
            mav.addObject("message", e.getMessage());
        }

        return mav;
    }

    @RequestMapping(value = "/show-courses")
    public ModelAndView showCourse() {
        ModelAndView mav = new ModelAndView("course/list-courses");
        mav.addObject("courses", teacherService.getAllCourses());
        return mav;
    }

    @RequestMapping(value = "/find-course")
    public String loadFindCourse() {
        return "course/find-course";
    }

    @RequestMapping(value = "/edit-course", method = RequestMethod.POST)
    public ModelAndView editCourse(HttpServletRequest req) {
        ModelAndView mav = new ModelAndView("course/edit-course");
        String id = req.getParameter("id");

        try {
            Course course = teacherService.findCourseById(new ObjectId(id));
            List<Lesson> lessonsInCourse = course.getLessonList();
            List<Lesson> allLessons = teacherService.getAllLessons();
            allLessons.removeAll(lessonsInCourse);

            mav.addObject("lessonsInCourse", lessonsInCourse);
            mav.addObject("allLessons", allLessons);
            mav.addObject("course", course);
        } catch (NoSuchCourseException e) {
            mav.setViewName("main/course-menu");
            mav.addObject(e.getMessage());
        }
        return mav;
    }

    @RequestMapping(value = "/update-course")
    public ModelAndView updateCourse(@Valid Course course, BindingResult result, HttpServletRequest req, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView("course/edit-course");
        List<Lesson> lessonInCourse = new ArrayList<>();
        List<Lesson> allLessons = teacherService.getAllLessons();

        for (Lesson lesson : allLessons) {
            if (req.getParameter(lesson.getTitle()) != null) {
                lessonInCourse.add(lesson);
            }
        }
        if (result.hasErrors()) {

            allLessons.removeAll(lessonInCourse);

            mav.addObject("lessonsInCourse", lessonInCourse);
            mav.addObject("allLessons", allLessons);
        } else {
            try {
                course.setLessonList(lessonInCourse);
                teacherService.updateCourse(course.getId(), course);
                redirectAttributes.addFlashAttribute("message", "The course has been successfully updated.");
                mav.setViewName("redirect:/course-menu");
            } catch (NoSuchCourseException e) {
                redirectAttributes.addFlashAttribute("message", e.getMessage());
                mav.setViewName("redirect:/course-menu");
            } catch (DuplicateDataException e) {
                mav.addObject("message", e.getMessage());
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


    @RequestMapping(value = "/show-course/{title}")
    public ModelAndView showCourse(@PathVariable String title, @RequestParam(required = false, name = "user") String targetUserName,
                                   RedirectAttributes redirectAttributes, Principal principal) {
        ModelAndView mav = new ModelAndView("main/show-course");
        try {

            boolean isNotEmpty = targetUserName != null && !targetUserName.isEmpty();
            String userName = isNotEmpty ? targetUserName : principal.getName();

            // go to other user statistic page
            if(isNotEmpty && !principal.getName().equals(targetUserName)){
                mav.setViewName("main/show-course-progress");
            }

            User currentUser = userService.findUser(userName);

            Course course = teacherService.findCourseByTitle(title);
            userService.addUserCourseStatInformation(currentUser, course);

            mav.addObject("currentUser", currentUser);
            mav.addObject("course", course);
            mav.addObject("subscribed", currentUser.getSubscribedCourses().contains(course));

        } catch (NoSuchCourseException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            mav.setViewName("redirect:/course-menu/show-courses");
        } catch (NoSuchUserException e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "/show-course")
    public ModelAndView showCourse(HttpServletRequest req) {
        ModelAndView mav = new ModelAndView();
        try {
            String title = req.getParameter("title");
            Course course = teacherService.findCourseByTitle(title);
            mav.setViewName("main/show-course");
            mav.addObject("course", course);
        } catch (NoSuchCourseException e) {
            mav.setViewName("course/find-course");
            mav.addObject("error", e.getMessage());
        }
        return mav;
    }

    @RequestMapping(value = "/delete-course")
    public String loadDeleteLesson() {
        return "course/delete-course";
    }

    @RequestMapping(value = "/delete-form")
    public ModelAndView deleteCourse(HttpServletRequest req, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView("redirect:/course-menu");
        try {
            String courseTitle = req.getParameter("courseTitle");
            teacherService.deleteCourse(courseTitle);
            redirectAttributes.addFlashAttribute("message", "The course has been successfully deleted.");
        } catch (NoSuchCourseException e) {
            mav.addObject("message", e.getMessage());
            mav.setViewName("course/delete-course");
        }
        return mav;
    }
}
