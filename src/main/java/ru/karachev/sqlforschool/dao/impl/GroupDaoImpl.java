package ru.karachev.sqlforschool.dao.impl;

import org.apache.log4j.Logger;
import ru.karachev.sqlforschool.dao.GroupDao;
import ru.karachev.sqlforschool.entity.Group;
import ru.karachev.sqlforschool.exception.DataBaseException;
import ru.karachev.sqlforschool.service.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GroupDaoImpl extends AbstractCrudDaoImpl<Group> implements GroupDao {

    private static final Logger LOGGER = Logger.getLogger(GroupDaoImpl.class);

    private static final String SAVE_QUERY = "INSERT INTO groups(group_id, group_name) VALUES (?, ?)";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM groups WHERE group_id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM groups ORDER BY group_id LIMIT ? OFFSET (?)-1";
    private static final String UPDATE_QUERY = "UPDATE groups SET group_name = ? WHERE group_id = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM groups WHERE group_id=?";

    private static final String FIND_BY_STUDENTS_COUNTER_QUERY =
            "SELECT * FROM groups WHERE group_id IN " +
                    "(SELECT group_id FROM students GROUP BY group_id HAVING count(student_id)>?);";

    public GroupDaoImpl(DBConnector connector) {
        super(connector, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY, UPDATE_QUERY, DELETE_BY_ID_QUERY);
    }

    @Override
    public List<Group> findGroupsWithSelectedCountOfStudents(Integer counter) {
        List<Group> groupsByCounter = new ArrayList<>();
        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     FIND_BY_STUDENTS_COUNTER_QUERY)) {
            statement.setInt(1, counter);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    groupsByCounter.add(mapResultSetToEntity(resultSet));
                }
                LOGGER.info("Finding groups with count of students equals " + counter + " complete");
                return groupsByCounter;
            }
        } catch (SQLException e) {
            LOGGER.error("Finding groups with count of students equals " + counter + " failed", e);
            throw new DataBaseException("Finding groups with count of students equals " + counter + " failed", e);
        }
    }

    @Override
    protected void insertForSave(PreparedStatement preparedStatement, Group group) throws SQLException {
        preparedStatement.setInt(1, group.getGroupId());
        preparedStatement.setString(2, group.getGroupName());
    }

    @Override
    protected Group mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return Group.builder()
                .withGroupId(resultSet.getInt("group_id"))
                .withGroupName(resultSet.getString("group_name"))
                .build();
    }

    @Override
    protected void updateValues(PreparedStatement preparedStatement, Group group) throws SQLException {
        preparedStatement.setString(1, group.getGroupName());
        preparedStatement.setInt(2, group.getGroupId());
    }

}
