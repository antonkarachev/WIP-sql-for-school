package ru.karachev.sqlforschool.dao;

import ru.karachev.sqlforschool.entity.Course;

import java.util.List;

public interface CourseDao extends CrudDao<Course, Integer> {

    List<String> findAllCourseNames();

    void deleteStudentFromOneCourse(Integer studentId, Integer courseId);
}
