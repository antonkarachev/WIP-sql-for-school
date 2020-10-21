package ru.karachev.sqlforschool.creator;

import org.junit.jupiter.api.Test;
import ru.karachev.sqlforschool.entity.Course;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CourseCreatorTest {

    private final CourseCreator courseCreator = new CourseCreator();

    @Test
    void createCoursesShouldReturnListOfCoursesWhenGettingIntMaxCoursesAndListOfStrings() {
        List<String> courseNamesAndDescription = new ArrayList<>();
        courseNamesAndDescription.add("first course_first description");
        courseNamesAndDescription.add("second course_second description");

        List<Course> expected = new ArrayList<>();
        expected.add(Course.builder()
                .withCourseId(1)
                .withCourseName("first course")
                .withDescription("first description")
                .build());
        expected.add(Course.builder()
                .withCourseId(2)
                .withCourseName("second course")
                .withDescription("second description")
                .build());

        List<Course> actual = courseCreator.createCourses(2, courseNamesAndDescription);

        assertThat(expected).isEqualTo(actual);
    }

}
