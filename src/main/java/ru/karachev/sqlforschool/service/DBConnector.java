package ru.karachev.sqlforschool.service;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.log4j.Logger;
import ru.karachev.sqlforschool.exception.DataBaseException;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnector {

    private static final Logger LOGGER = Logger.getLogger(DBConnector.class);

    private final HikariDataSource dataSource;

    public DBConnector(String configFilepath) {
        HikariConfig config = new HikariConfig(configFilepath);
        this.dataSource = new HikariDataSource(config);
    }

    public Connection getConnection() {
        try {
            LOGGER.info("Connection complete");
            return dataSource.getConnection();
        } catch (SQLException e) {
            LOGGER.error("Connection failed", e);
            throw new DataBaseException("Connection failed", e);
        }
    }

}
