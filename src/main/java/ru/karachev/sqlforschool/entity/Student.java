package ru.karachev.sqlforschool.entity;

import java.util.Objects;

public class Student {

    private final Integer studentId;
    private final Integer groupId;
    private final String name;
    private final String lastName;

    private Student(Builder builder) {
        this.studentId = builder.studentId;
        this.groupId = builder.groupId;
        this.name = builder.name;
        this.lastName = builder.lastName;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Integer getId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public Integer getGroupId() {
        return groupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Student student = (Student) o;
        return Objects.equals(studentId, student.studentId) &&
                Objects.equals(groupId, student.groupId) &&
                Objects.equals(name, student.name) &&
                Objects.equals(lastName, student.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, groupId, name, lastName);
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", groupId=" + groupId +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    public static class Builder {
        private Integer studentId;
        private Integer groupId;
        private String name;
        private String lastName;

        public Builder withStudentId(Integer studentId) {
            this.studentId = studentId;
            return this;
        }

        public Builder withGroupId(Integer groupId) {
            this.groupId = groupId;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Student build() {
            return new Student(this);
        }
    }

}
