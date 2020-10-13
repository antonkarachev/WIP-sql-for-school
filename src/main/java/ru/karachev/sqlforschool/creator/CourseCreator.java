package ru.karachev.sqlforschool.creator;

import org.apache.log4j.Logger;
import ru.karachev.sqlforschool.entity.Course;

import java.util.ArrayList;
import java.util.List;

public class CourseCreator {

    private static final Logger LOGGER = Logger.getLogger(CourseCreator.class);

    private static final int PLACE_OF_NAME = 0;
    private static final int PLACE_OF_DESCRIPTION = 1;
    private static final String SPLITERATOR = "_";

    public List<Course> createCourses(int maxCourses, List<String> courseNamesAndDescription) {
        List<Course> courses = new ArrayList<>();
        for (int i = 0; i < maxCourses; i++) {
            String line = courseNamesAndDescription.get(i);
            courses.add(Course.builder()
                    .withCourseId(i + 1)
                    .withCourseName(getFromLine(PLACE_OF_NAME, line))
                    .withDescription(getFromLine(PLACE_OF_DESCRIPTION, line))
                    .build());
        }

        LOGGER.info("Creation of courses complete");
        return courses;
    }

    private String getFromLine(int placeOfWhatGet, String line) {
        return line.split(SPLITERATOR)[placeOfWhatGet];
    }

}
