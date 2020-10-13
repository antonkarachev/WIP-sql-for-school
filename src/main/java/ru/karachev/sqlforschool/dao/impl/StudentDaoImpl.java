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
import java.util.List;

public class StudentDaoImpl extends AbstractCrudDaoImpl<Student> implements StudentDao {

    private static final Logger LOGGER = Logger.getLogger(StudentDaoImpl.class);

    private static final String SAVE_QUERY = "INSERT INTO students VALUES (?, ?, ?, ?)";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM students WHERE student_id=?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM students ORDER BY student_id";
    private static final String FIND_ALL_PAGINATION_QUERY = "SELECT * FROM students ORDER BY student_id LIMIT ? OFFSET ?";
    private static final String UPDATE_QUERY = "UPDATE students SET group_id = ?, student_name=?, " +
            "student_last_name=? WHERE student_id = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM students WHERE student_id=?";

    private static final String FIND_ALL_RELATED_TO_COURSE_NAME =
            "SELECT * FROM students WHERE student_id IN " +
                    "(SELECT student_id FROM students_to_courses WHERE course_id IN" +
                    "(SELECT course_id FROM courses WHERE course_name = ?)) " +
                    "ORDER BY student_id";
    private static final String ASSIGN_STUDENT_TO_COURSE_QUERY = "INSERT into students_to_courses VALUES (?,?)";

    public StudentDaoImpl(DBConnector connector) {
        super(connector, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY, FIND_ALL_PAGINATION_QUERY,
                UPDATE_QUERY, DELETE_BY_ID_QUERY);
    }

    @Override
    public List<Student> findAllByCourseName(String courseName) {
        return findAllByStringParam(courseName, FIND_ALL_RELATED_TO_COURSE_NAME);
    }

    @Override
    public void assignToCourse(Integer studentId, Integer courseId) {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(ASSIGN_STUDENT_TO_COURSE_QUERY)) {
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
    protected void insert(PreparedStatement preparedStatement, Student student) throws SQLException {
        preparedStatement.setInt(1, student.getId());
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
        preparedStatement.setInt(4, student.getId());
    }

}
