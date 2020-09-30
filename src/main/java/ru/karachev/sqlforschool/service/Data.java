package ru.karachev.sqlforschool.service;

import ru.karachev.sqlforschool.entity.Course;
import ru.karachev.sqlforschool.entity.Group;
import ru.karachev.sqlforschool.entity.Student;

import java.util.List;

public class Data {
    private final List<Group> groups;
    private final List<Course> courses;
    private final List<Student> students;

    public Data(List<Group> groups, List<Course> courses, List<Student> students) {
        this.groups = groups;
        this.courses = courses;
        this.students = students;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public List<Student> getStudents() {
        return students;
    }
}
