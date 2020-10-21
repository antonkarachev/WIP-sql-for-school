package ru.karachev.sqlforschool.datasource;

import ru.karachev.sqlforschool.entity.Course;
import ru.karachev.sqlforschool.entity.Group;
import ru.karachev.sqlforschool.entity.Student;

import java.util.List;
import java.util.Objects;

public class DataSource {
    
    private final List<Group> groups;
    private final List<Course> courses;
    private final List<Student> students;
    
    public DataSource(List<Student> students, List<Group> groups, List<Course> courses) {
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
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DataSource that = (DataSource) o;
        return Objects.equals(groups, that.groups) &&
                Objects.equals(courses, that.courses) &&
                Objects.equals(students, that.students);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(groups, courses, students);
    }
    
    @Override
    public String toString() {
        return "DataSource{" +
                "groups=" + groups +
                ", courses=" + courses +
                ", students=" + students +
                '}';
    }
}
