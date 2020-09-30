package ru.karachev.sqlforschool.dao;

import ru.karachev.sqlforschool.entity.Student;

import java.util.List;

public interface StudentDao extends CrudDao <Student, Integer>{

    void saveFromList(List<Student> students);

    void assignStudentToCourses(Student student);

    void assignStudentsFromListToCourses(List<Student> students);

    void addStudentToSpecifiedCourse(Integer studentId, Integer courseId);

    List<Student> findAllStudentsRelatedToCourseByName(String courseName);

}
