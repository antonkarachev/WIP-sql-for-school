package ru.karachev.sqlforschool.dao.impl;

import ru.karachev.sqlforschool.dao.GroupDao;
import ru.karachev.sqlforschool.entity.Group;
import ru.karachev.sqlforschool.service.DBConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GroupDaoImpl extends AbstractCrudDaoImpl<Group> implements GroupDao {

    private static final String SAVE_QUERY = "INSERT INTO groups(group_id, group_name) VALUES (?, ?)";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM groups WHERE group_id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM groups ORDER BY group_id";
    private static final String FIND_ALL_PAGINATION_QUERY =
            "SELECT * FROM groups ORDER BY group_id LIMIT ? OFFSET ?";
    private static final String UPDATE_QUERY = "UPDATE groups SET group_name = ? WHERE group_id = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM groups WHERE group_id=?";

    private static final String FIND_ALL_BY_STUDENT_COUNT_QUERY =
            "SELECT * FROM groups WHERE group_id IN " +
                    "(SELECT group_id FROM students GROUP BY group_id HAVING count(student_id)>=?);";

    public GroupDaoImpl(DBConnector connector) {
        super(connector, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY, FIND_ALL_PAGINATION_QUERY,
                UPDATE_QUERY, DELETE_BY_ID_QUERY);
    }

    @Override
    public List<Group> findAllByStudentCount(int count) {
        return findAllByIntParameter(count, FIND_ALL_BY_STUDENT_COUNT_QUERY);
    }

    @Override
    protected void insert(PreparedStatement preparedStatement, Group group) throws SQLException {
        preparedStatement.setInt(1, group.getId());
        preparedStatement.setString(2, group.getName());
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
        preparedStatement.setString(1, group.getName());
        preparedStatement.setInt(2, group.getId());
    }

}
