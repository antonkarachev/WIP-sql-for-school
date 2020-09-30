package ru.karachev.sqlforschool.creator;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.log4j.Logger;
import ru.karachev.sqlforschool.exception.DataBaseException;
import ru.karachev.sqlforschool.service.DBConnector;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static org.apache.ibatis.io.Resources.getResourceAsReader;

public class TableCreator {

    private static final Logger LOGGER = Logger.getLogger(TableCreator.class);

    private final DBConnector connector;

    public TableCreator(DBConnector connector) {
        this.connector = connector;
    }

    public void createTables(String file) {
        ScriptRunner runner = null;
        try (Connection connection = connector.getConnection()) {
            runner = new ScriptRunner(connection);
            runner.setLogWriter(null);
            runner.runScript(getResourceAsReader(file));
        } catch (SQLException | IOException e) {
            LOGGER.error("Table creation failed", e);
            throw new DataBaseException("Table creation failed", e);
        } finally {
            if (runner != null) {
                runner.closeConnection();
            }
        }
    }

}
