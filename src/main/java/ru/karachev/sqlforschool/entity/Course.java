package ru.karachev.sqlforschool.entity;

import java.util.Objects;

public class Course {
    private final Integer courseId;
    private final String courseName;
    private final String description;
    
    private Course(Builder builder) {
        this.courseId = builder.courseId;
        this.courseName = builder.courseName;
        this.description = builder.description;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public Integer getId() {
        return courseId;
    }
    
    public String getName() {
        return courseName;
    }
    
    public String getDescription() {
        return description;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Course course = (Course) o;
        return Objects.equals(courseId, course.courseId) &&
                Objects.equals(courseName, course.courseName) &&
                Objects.equals(description, course.description);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(courseId, courseName, description);
    }
    
    @Override
    public String toString() {
        return "Course{" +
                "courseId=" + courseId +
                ", courseName='" + courseName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
    
    public static class Builder {
        private Integer courseId;
        private String courseName;
        private String description;
        
        public Builder withCourseId(Integer courseId) {
            this.courseId = courseId;
            return this;
        }
        
        public Builder withCourseName(String courseName) {
            this.courseName = courseName;
            return this;
        }
        
        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }
        
        public Course build() {
            return new Course(this);
        }
    }
}

