package ru.karachev.sqlforschool.dao.impl;

import org.apache.log4j.Logger;
import ru.karachev.sqlforschool.dao.CrudDao;
import ru.karachev.sqlforschool.exception.DataBaseException;
import ru.karachev.sqlforschool.service.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

public abstract class AbstractCrudDaoImpl<E> implements CrudDao<E, Integer> {

    private static final Logger LOGGER = Logger.getLogger(AbstractCrudDaoImpl.class);

    private static final BiConsumer<PreparedStatement, Integer> INTEGER_CONSUMER
            = (PreparedStatement preparedStatement, Integer parameter) -> {
        try {
            preparedStatement.setInt(1, parameter);
        } catch (SQLException e) {
            LOGGER.error("Integer Consumer error", e);
            throw new DataBaseException("Integer Consumer error", e);
        }
    };

    private static final BiConsumer<PreparedStatement, String> STRING_CONSUMER
            = (PreparedStatement preparedStatement, String parameter) -> {
        try {
            preparedStatement.setString(1, parameter);
        } catch (SQLException e) {
            LOGGER.error("Integer Consumer error", e);
            throw new DataBaseException("Integer Consumer error", e);
        }
    };

    protected final DBConnector connector;
    private final String saveQuery;
    private final String findByIdQuery;
    private final String findAllQuery;
    private final String updateQuery;
    private final String deleteByIdQuery;

    public AbstractCrudDaoImpl(DBConnector connector, String saveQuery, String findByIdQuery,
                               String findAllQuery, String updateQuery, String deleteByIdQuery) {
        this.connector = connector;
        this.saveQuery = saveQuery;
        this.findByIdQuery = findByIdQuery;
        this.findAllQuery = findAllQuery;
        this.updateQuery = updateQuery;
        this.deleteByIdQuery = deleteByIdQuery;
    }

    @Override
    public void save(E entity) {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(saveQuery)) {
            insertForSave(preparedStatement, entity);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Save failed", e);
            throw new DataBaseException("Save failed", e);
        }
    }

    @Override
    public Optional<E> findById(Integer id) {
        return findByIntegerParam(id, findByIdQuery);
    }

    @Override
    public List<E> findAll(Integer startId, Integer countOfIds) {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(findAllQuery)) {
            preparedStatement.setInt(1, countOfIds);
            preparedStatement.setInt(2, startId - 1);
            List<E> entities = new ArrayList<>();
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    entities.add(mapResultSetToEntity(resultSet));
                }
                return entities;
            }
        } catch (SQLException e) {
            LOGGER.error("Find all failed", e);
            throw new DataBaseException("Find all failed", e);
        }
    }

    @Override
    public void deleteById(Integer id) {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteByIdQuery)) {
            INTEGER_CONSUMER.accept(preparedStatement, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Deleting by id failed", e);
            throw new DataBaseException("Deleting by id failed", e);
        }
    }

    @Override
    public void update(E entity) {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            updateValues(preparedStatement, entity);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Updating failed", e);
            throw new DataBaseException("Updating failed", e);
        }
    }

    protected Optional<E> findByIntegerParam(Integer id, String query) {
        return findByParam(id, query, INTEGER_CONSUMER);
    }

    protected Optional<E> findByStringParam(Integer name, String query) {
        return findByParam(name, query, INTEGER_CONSUMER);
    }

    private <P> Optional<E> findByParam(P parameter, String query, BiConsumer<PreparedStatement, P> consumer) {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            consumer.accept(preparedStatement, parameter);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next() ? Optional.ofNullable(mapResultSetToEntity(resultSet)) : Optional.empty();
            }
        } catch (SQLException e) {
            LOGGER.error("Find by parameter failed", e);
            throw new DataBaseException("Find by parameter failed", e);
        }
    }

    protected abstract E mapResultSetToEntity(ResultSet resultSet) throws SQLException;

    protected abstract void insertForSave(PreparedStatement preparedStatement, E entity) throws SQLException;

    protected abstract void updateValues(PreparedStatement preparedStatement, E entity) throws SQLException;

}
