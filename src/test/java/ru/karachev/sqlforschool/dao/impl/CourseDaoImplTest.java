package ru.karachev.sqlforschool.dao.impl;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.karachev.sqlforschool.dao.CourseDao;
import ru.karachev.sqlforschool.entity.Course;
import ru.karachev.sqlforschool.exception.DataBaseException;
import ru.karachev.sqlforschool.service.DBConnector;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CourseDaoImplTest {

    private static final DBConnector connector = new DBConnector("src/test/resources/database.properties");
    private final CourseDao courseDao = new CourseDaoImpl(connector);

    static Course findByIdTest(int id) {
        String query = "SELECT * FROM courses WHERE course_id = ?";
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            Course course = null;
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    course = Course.builder()
                            .withCourseId(resultSet.getInt("course_id"))
                            .withCourseName(resultSet.getString("course_name"))
                            .withDescription(resultSet.getString("description"))
                            .build();
                }
                return course;
            }
        } catch (SQLException e) {
            throw new DataBaseException("Finding failed", e);
        }
    }

    @BeforeEach
    void createDataBase() {
        ScriptRunner runner = null;
        try (Connection connection = connector.getConnection()) {
            runner = new ScriptRunner(connection);
            runner.setLogWriter(null);
            runner.runScript(new BufferedReader(new FileReader("src/test/resources/tables.sql")));
        } catch (SQLException | IOException e) {
            throw new DataBaseException("Table creation failed", e);
        } finally {
            if (runner != null) {
                runner.closeConnection();
            }
        }
    }

    @Test
    void saveShouldSaveCourseToDataBase() {
        Course expectedCourse = Course.builder()
                .withCourseId(4)
                .withCourseName("expected name")
                .withDescription("expected description")
                .build();
        courseDao.save(expectedCourse);
        Course actualCourse = findByIdTest(4);

        assertThat(expectedCourse).isEqualTo(actualCourse);
    }

    @Test
    void findByIdShouldReturnCourseWithDesiredId() {
        Course expectedCourse = Course.builder()
                .withCourseId(1)
                .withCourseName("biology")
                .withDescription("biological things")
                .build();
        Course actualCourse = courseDao.findById(1).get();

        assertThat(actualCourse).isEqualTo(expectedCourse);
    }

    @Test
    void findAllShouldReturnListOfAllCourses() {
        List<Course> expected = new ArrayList<>();
        Course course1 = Course.builder()
                .withCourseId(1)
                .withCourseName("biology")
                .withDescription("biological things")
                .build();
        Course course2 = Course.builder()
                .withCourseId(2)
                .withCourseName("history")
                .withDescription("historical stuff")
                .build();
        Course course3 = Course.builder()
                .withCourseId(3)
                .withCourseName("mathematics")
                .withDescription("interesting things")
                .build();
        expected.add(course1);
        expected.add(course2);
        expected.add(course3);
        List<Course> actual = courseDao.findAll(1, 3);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void updateShouldUpdateCourse() {
        Course expected = Course.builder()
                .withCourseId(3)
                .withCourseName("not mathematics")
                .withDescription("some things")
                .build();

        courseDao.update(expected);
        Course actual = findByIdTest(3);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void deleteShouldDeleteCourseFromDataBase() {
        courseDao.deleteById(3);
        Course actual = findByIdTest(3);
        assertThat(actual).isNull();
    }

    @Test
    void findAllCoursesNamesShouldReturnListOfNamesFromDataBase() {
        List<String> expected = new ArrayList<>();
        expected.add("biology");
        expected.add("history");
        expected.add("mathematics");
        List<String> actual = courseDao.findAllCourseNames();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void deleteStudentFromOneCourseShouldDeleteStudentFromTableStudentToCourses() {
        courseDao.deleteStudentFromOneCourse(1, 2);
        String query = "SELECT FROM students_to_courses WHERE student_id = 1 AND course_id = 2";
        boolean isExist = true;
        try (Connection connection = connector.getConnection();
             Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(query)) {
                isExist = resultSet.next();
            }
        } catch (SQLException e) {
            throw new DataBaseException("Deleting student from course failed",e);
        }

        assertThat(isExist).isFalse();
    }

}
