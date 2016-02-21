package ua.artcode.service;

import ua.artcode.exception.AppException;
import ua.artcode.exception.NoSuchCourseException;
import ua.artcode.exception.NoSuchGroupException;
import ua.artcode.exception.NoSuchLessonException;
import ua.artcode.model.Course;
import ua.artcode.model.common.User;
import ua.artcode.model.common.UserGroup;
import ua.artcode.model.Lesson;
import ua.artcode.model.codingbat.CodingBatTask;

import java.util.List;

/**
 * Created by Razer on 02.02.16.
 */
public interface TeacherService {

    //Lesson methods
    void addLesson(Lesson lesson) throws AppException;

    List<Lesson> getAllLessons();

    Lesson findLessonByTitle(String title) throws NoSuchLessonException;

    void addTaskToLesson(String title, CodingBatTask codingBatTask) throws NoSuchLessonException, AppException;

    boolean deleteLesson(String title) throws NoSuchLessonException;

    void updateLesson(Lesson lesson) throws NoSuchLessonException, AppException;

    int sizeOfLesson();

    //Course methods
    Course addCourse(Course course) throws AppException;

    boolean deleteCourse(String title) throws NoSuchCourseException;

    List<Course> getAllCourses();

    void updateCourse(Course course) throws NoSuchCourseException, AppException;

    Course findCourseByTitle(String title) throws NoSuchCourseException;

    int sizeOfCourse();

    //Group methods
    UserGroup addGroup(UserGroup group) throws AppException;

    boolean deleteGroup(String name) throws NoSuchGroupException;

    void addUserToGroup(String name, User user) throws AppException, NoSuchGroupException;

    void updateGroup(UserGroup userGroup) throws AppException, NoSuchGroupException;

    List<UserGroup> getAllGroups() throws AppException;

    UserGroup findUserGroupByName(String name) throws NoSuchGroupException;

    boolean isExistGroup(String name);

    int sizeOfGroup();

}
