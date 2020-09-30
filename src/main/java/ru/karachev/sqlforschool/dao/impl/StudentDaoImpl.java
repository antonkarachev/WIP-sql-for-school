package ru.karachev.sqlforschool.dao.impl;

import org.apache.log4j.Logger;
import ru.karachev.sqlforschool.dao.StudentDao;
import ru.karachev.sqlforschool.entity.Student;
import ru.karachev.sqlforschool.exception.DataBaseException;
import ru.karachev.sqlforschool.service.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class StudentDaoImpl extends AbstractCrudDaoImpl<Student> implements StudentDao {

    private static final Logger LOGGER = Logger.getLogger(StudentDaoImpl.class);

    private static final String SAVE_QUERY = "INSERT INTO students VALUES (?, ?, ?, ?)";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM students WHERE student_id=?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM students ORDER BY student_id LIMIT ? OFFSET (?)-1";
    private static final String UPDATE_QUERY = "UPDATE students SET group_id = ?, student_name=?, " +
            "student_last_name=? WHERE group_id = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM students WHERE student_id=?";

    private static final String ASSIGN_QUERY = "INSERT INTO students_to_courses (student_id, course_id) " +
            "VALUES (?, ?)";
    private static final String FIND_ALL_STUDENTS_RELATED_TO_COURSE_NAME_QUERY =
            "SELECT * FROM students WHERE student_id IN " +
                    "(SELECT student_id FROM students_to_courses WHERE course_id IN" +
                    "(SELECT course_id FROM courses WHERE course_name = ?)) " +
                    "ORDER BY student_id";
    private static final String ADD_STUDENT_TO_COURSE_QUERY = "INSERT into students_to_courses VALUES (?,?)";

    public StudentDaoImpl(DBConnector connector) {
        super(connector, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY, UPDATE_QUERY, DELETE_BY_ID_QUERY);
    }

    @Override
    public void saveFromList(List<Student> students) {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_QUERY)) {
            for (Student student : students) {
                insertForSave(preparedStatement, student);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            LOGGER.info("Saving students from list complete ");
        } catch (SQLException e) {
            LOGGER.error("Saving from list failed", e);
            throw new DataBaseException("Saving from list failed", e);
        }
    }

    @Override
    public void assignStudentToCourses(Student student) {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(ASSIGN_QUERY)) {
            insertForAssign(preparedStatement, student.getStudentId(), student.getCourseIds());
            preparedStatement.executeBatch();
            LOGGER.info("Assigning student with id: " + student.getStudentId() + " to courses complete ");
        } catch (SQLException e) {
            LOGGER.error("Assigning student with id: " + student.getStudentId() + "to courses failed ", e);
            throw new DataBaseException("Assigning student with id: " + student.getStudentId() +
                    "to courses failed ", e);
        }
    }

    @Override
    public void assignStudentsFromListToCourses(List<Student> students) {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(ASSIGN_QUERY)) {
            for (Student student : students){
                insertForAssign(preparedStatement, student.getStudentId(), student.getCourseIds());
            }
            preparedStatement.executeBatch();
            LOGGER.info("Assigning students from list to courses complete ");
        }catch (SQLException e){
            LOGGER.error("Assigning students from list to courses failed", e);
            throw new DataBaseException("Assigning students from list to courses failed", e);
        }
    }

    public void addStudentToSpecifiedCourse(Integer studentId, Integer courseId) {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(ADD_STUDENT_TO_COURSE_QUERY)) {
            preparedStatement.setInt(1, studentId);
            preparedStatement.setInt(2, courseId);
            preparedStatement.executeUpdate();
            LOGGER.info("Assigning student" + studentId + " to course " + courseId + " complete");
        } catch (SQLException e) {
            LOGGER.error("Assigning student" + studentId + " to course " + courseId + " failed", e);
            throw new DataBaseException("Assigning student" + studentId + " to course " + courseId + " failed", e);
        }
    }

    @Override
    public List<Student> findAllStudentsRelatedToCourseByName(String courseName) {
        List<Student> students = new ArrayList<>();
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     FIND_ALL_STUDENTS_RELATED_TO_COURSE_NAME_QUERY)) {
            preparedStatement.setString(1, courseName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    students.add(mapResultSetToEntity(resultSet));
                }
                LOGGER.info("Getting students by course name: " + courseName + " complete");
            }
        } catch (SQLException e) {
            LOGGER.error("Getting students by course name: " + courseName + " failed", e);
            throw new DataBaseException("Getting students by course name: " + courseName + " failed", e);
        }
        return students;
    }

    protected void insertForAssign(PreparedStatement preparedStatement, Integer studentId,
                                   Set<Integer> courseIds) throws SQLException {
        for (int courseIdToAssignWith : courseIds) {
            preparedStatement.setInt(1, studentId);
            preparedStatement.setInt(2, courseIdToAssignWith);
            preparedStatement.addBatch();
        }
    }

    @Override
    protected void insertForSave(PreparedStatement preparedStatement, Student student) throws SQLException {
        preparedStatement.setInt(1, student.getStudentId());
        preparedStatement.setInt(2, student.getGroupId());
        preparedStatement.setString(3, student.getName());
        preparedStatement.setString(4, student.getLastName());
    }

    @Override
    protected Student mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return Student.builder()
                .withStudentId(resultSet.getInt("student_id"))
                .withGroupId(resultSet.getInt("group_id"))
                .withName(resultSet.getString("student_name"))
                .withLastName(resultSet.getString("student_last_name"))
                .build();
    }

    @Override
    protected void updateValues(PreparedStatement preparedStatement, Student student) throws SQLException {
        preparedStatement.setInt(1, student.getGroupId());
        preparedStatement.setString(2, student.getName());
        preparedStatement.setString(3, student.getLastName());
        preparedStatement.setInt(4, student.getStudentId());
    }

}
