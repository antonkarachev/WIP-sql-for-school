package ru.karachev.sqlforschool.dao;

import ru.karachev.sqlforschool.entity.Student;

import java.util.List;

public interface StudentDao extends CrudDao<Student, Integer> {

    List<Student> findAllByCourseName(String courseName);

    void assignToCourse(Integer studentId, Integer courseId);

}
