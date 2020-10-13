package ru.karachev.sqlforschool.dao;

import ru.karachev.sqlforschool.entity.Course;

import java.util.List;

public interface CourseDao extends CrudDao<Course, Integer> {

    List<Course> findAllByStudentId(int studentId);

    void removeStudentFromCourse(Integer studentId, Integer courseId);
}
