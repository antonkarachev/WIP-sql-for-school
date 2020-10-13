package ru.karachev.sqlforschool.dao.impl;

import org.apache.log4j.Logger;
import ru.karachev.sqlforschool.dao.CrudDao;
import ru.karachev.sqlforschool.exception.DataBaseException;
import ru.karachev.sqlforschool.service.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    private final String findAllPaginationQuery;
    private final String updateQuery;
    private final String deleteByIdQuery;

    public AbstractCrudDaoImpl(DBConnector connector, String saveQuery, String findByIdQuery,
                               String findAllQuery, String findAllPaginationQuery, String updateQuery, String deleteByIdQuery) {
        this.connector = connector;
        this.saveQuery = saveQuery;
        this.findByIdQuery = findByIdQuery;
        this.findAllQuery = findAllQuery;
        this.findAllPaginationQuery = findAllPaginationQuery;
        this.updateQuery = updateQuery;
        this.deleteByIdQuery = deleteByIdQuery;
    }

    @Override
    public void save(E entity) {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(saveQuery)) {
            insert(preparedStatement, entity);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Save failed", e);
            throw new DataBaseException("Save failed", e);
        }
    }

    @Override
    public void saveAll(List<E> entities) {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(saveQuery)) {
            for (E entity : entities) {
                insert(preparedStatement, entity);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            LOGGER.info("Saving from list complete ");
        } catch (SQLException e) {
            LOGGER.error("Saving from list failed", e);
            throw new DataBaseException("Saving from list failed", e);
        }
    }

    @Override
    public Optional<E> findById(Integer id) {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(findByIdQuery)) {
            INTEGER_CONSUMER.accept(preparedStatement, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next() ? Optional.ofNullable(mapResultSetToEntity(resultSet)) : Optional.empty();
            }
        } catch (SQLException e) {
            LOGGER.error("Find by id failed", e);
            throw new DataBaseException("Find by id failed", e);
        }
    }

    @Override
    public List<E> findAll() {
        try (Connection connection = connector.getConnection();
             Statement statement = connection.createStatement()) {
            List<E> entities = new ArrayList<>();
            try (ResultSet resultSet = statement.executeQuery(findAllQuery)) {
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
    public List<E> findAll(int page, int itemPerPage) {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(findAllPaginationQuery)) {
            preparedStatement.setInt(1, itemPerPage);
            preparedStatement.setInt(2, ((itemPerPage * page) - itemPerPage));
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

    protected List<E> findAllByIntParameter(Integer parameter, String query) {
        return findAllByParameter(parameter, query, INTEGER_CONSUMER);
    }

    protected List<E> findAllByStringParam(String parameter, String query) {
        return findAllByParameter(parameter, query, STRING_CONSUMER);
    }

    protected <P> List<E> findAllByParameter(P parameter, String query,
                                             BiConsumer<PreparedStatement, P> consumer) {
        List<E> entities = new ArrayList<>();
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            consumer.accept(preparedStatement, parameter);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    entities.add(mapResultSetToEntity(resultSet));
                }
                return entities;
            }
        } catch (SQLException e) {
            LOGGER.error("Find all by parameter failed", e);
            throw new DataBaseException("Find all by parameter failed", e);
        }
    }

    protected abstract E mapResultSetToEntity(ResultSet resultSet) throws SQLException;

    protected abstract void insert(PreparedStatement preparedStatement, E entity) throws SQLException;

    protected abstract void updateValues(PreparedStatement preparedStatement, E entity) throws SQLException;

}
