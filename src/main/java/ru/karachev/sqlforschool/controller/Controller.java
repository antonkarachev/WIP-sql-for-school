package ru.karachev.sqlforschool.controller;

import ru.karachev.sqlforschool.dao.CourseDao;
import ru.karachev.sqlforschool.dao.GroupDao;
import ru.karachev.sqlforschool.dao.StudentDao;
import ru.karachev.sqlforschool.entity.Group;
import ru.karachev.sqlforschool.entity.Student;

import java.util.List;

public class Controller {

    private final GroupDao groupDao;
    private final CourseDao courseDao;
    private final StudentDao studentDao;

    public Controller(GroupDao groupDao, CourseDao courseDao, StudentDao studentDao) {
        this.groupDao = groupDao;
        this.courseDao = courseDao;
        this.studentDao = studentDao;
    }

    public List<Group> findAllGroupsWithLessOrEqualsStudentCountCommand (int counter) {
        return groupDao.findGroupsWithSelectedCountOfStudents(counter);
    }

    public List<String> getAllCoursesNames (){
        return courseDao.findAllCourseNames();
    }

    public List<Student> findAllStudentsRelatedToCourseName(String courseName) {
        return studentDao.findAllStudentsRelatedToCourseByName(courseName);
    }

    public void addNewStudent(Student student) {
        studentDao.save(student);
    }

    public void addStudentToSpecifiedCourse(int studentId, int courseId) {
        studentDao.addStudentToSpecifiedCourse(studentId, courseId);
    }

    public void deleteById(int studentId) {
        studentDao.deleteById(studentId);
    }

    public void deleteStudentFromOneCourse(int studentId, int courseId) {
        courseDao.deleteStudentFromOneCourse(studentId, courseId);
    }

}
