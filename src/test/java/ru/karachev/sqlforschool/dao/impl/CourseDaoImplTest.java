package ru.karachev.sqlforschool.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.karachev.sqlforschool.service.DataBaseGenerator;
import ru.karachev.sqlforschool.dao.CourseDao;
import ru.karachev.sqlforschool.entity.Course;
import ru.karachev.sqlforschool.exception.DataBaseException;
import ru.karachev.sqlforschool.service.DBConnector;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CourseDaoImplTest {

    private final DBConnector connector = new DBConnector("src/test/resources/database.properties");
    private final DataBaseGenerator dataBaseGenerator = new DataBaseGenerator(connector);
    private final CourseDao courseDao = new CourseDaoImpl(connector);

    @BeforeEach
    void createDataBase() {
        dataBaseGenerator.generateDataBase("tables.sql");
    }

    @Test
    void saveShouldSaveCourseToDataBaseWhenGettingCourse() {
        Course expected = Course.builder()
                .withCourseId(4)
                .withCourseName("expected name")
                .withDescription("expected description")
                .build();
        courseDao.save(expected);
        Course actualCourse = courseDao.findById(4).orElse(null);

        assertThat(expected).isEqualTo(actualCourse);
    }

    @Test
    void saveAllShouldSaveCoursesWhenGettingListOfCourses(){
        List<Course> coursesForSave = new ArrayList<>();
        Course course1 = Course.builder()
                .withCourseId(4)
                .withCourseName("1 added course")
                .withDescription("course with id 4")
                .build();
        Course course2 = Course.builder()
                .withCourseId(5)
                .withCourseName("2 added course")
                .withDescription("course with id 5")
                .build();
        Course course3 = Course.builder()
                .withCourseId(6)
                .withCourseName("3 added course")
                .withDescription("course with id 6")
                .build();
        coursesForSave.add(course1);
        coursesForSave.add(course2);
        coursesForSave.add(course3);
        courseDao.saveAll(coursesForSave);
        List<Course> actual = courseDao.findAll();
        assertThat(actual)
                .contains(course1)
                .contains(course2)
                .contains(course3);
    }

    @Test
    void findByIdShouldReturnCourseWhenGettingId() {
        Course expectedCourse = Course.builder()
                .withCourseId(1)
                .withCourseName("biology")
                .withDescription("biological things")
                .build();
        Course actualCourse = courseDao.findById(1).orElse(null);

        assertThat(actualCourse).isEqualTo(expectedCourse);
    }

    @Test
    void findAllShouldReturnListOfCoursesWhenGettingPaginationParameters() {
        List<Course> expected = new ArrayList<>();
        expected.add(courseDao.findById(1).orElse(null));
        expected.add(courseDao.findById(2).orElse(null));
        List<Course> actual = courseDao.findAll(1,2);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findAllShouldReturnListOfAllCoursesWhenNotGettingPaginationParameters() {
        List<Course> expected = new ArrayList<>();
        expected.add(courseDao.findById(1).orElse(null));
        expected.add(courseDao.findById(2).orElse(null));
        expected.add(courseDao.findById(3).orElse(null));
        List<Course> actual = courseDao.findAll();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void deleteByIdShouldDeleteCourseFromDataBaseWhenGettingId() {
        courseDao.deleteById(3);
        Course actual = courseDao.findById(3).orElse(null);
        assertThat(actual).isNull();
    }

    @Test
    void updateShouldUpdateCourseWhenGettingCourse() {
        Course expected = Course.builder()
                .withCourseId(3)
                .withCourseName("not mathematics")
                .withDescription("some things")
                .build();

        courseDao.update(expected);
        Course actual = courseDao.findById(3).orElse(null);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findAllStudentCoursesShouldReturnListOfCoursesStudentRelatedToWhenGettingStudentId() {
        List<Course> expected = new ArrayList<>();
        expected.add(courseDao.findById(1).orElse(null));
        expected.add(courseDao.findById(2).orElse(null));
        List<Course> actual = courseDao.findAllByStudentId(1);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void deleteStudentFromOneCourseShouldDeleteStudentFromTableStudentToCourses() {
        courseDao.removeStudentFromCourse(1, 2);
        String query = "SELECT FROM students_to_courses WHERE student_id = 1 AND course_id = 2";
        boolean isExist;
        try (Connection connection = connector.getConnection();
             Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(query)) {
                isExist = resultSet.next();
            }
        } catch (SQLException e) {
            throw new DataBaseException("Deleting student from course failed", e);
        }
        assertThat(isExist).isFalse();
    }

}
