package ru.karachev.sqlforschool.service;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.log4j.Logger;
import ru.karachev.sqlforschool.exception.DataBaseException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static org.apache.ibatis.io.Resources.getResourceAsReader;

public class DataBaseGenerator {

    private static final Logger LOGGER = Logger.getLogger(DataBaseGenerator.class);

    private final DBConnector connector;

    public DataBaseGenerator(DBConnector connector) {
        this.connector = connector;
    }

    public void generateDataBase(String scriptFilePath) {
        try (Connection connection = connector.getConnection()) {
            ScriptRunner runner = new ScriptRunner(connection);
            runner.setLogWriter(null);
            runner.runScript(getResourceAsReader(scriptFilePath));
            LOGGER.info("Table creation complete");
        } catch (SQLException e) {
            LOGGER.error("Table creation failed", e);
            throw new DataBaseException("Table creation failed", e);
        } catch (IOException e){
            LOGGER.error("Something wrong with file of script", e);
            throw new DataBaseException("Something wrong with file of script", e);
        }
    }

}
