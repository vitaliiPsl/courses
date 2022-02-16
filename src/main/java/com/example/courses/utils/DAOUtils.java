package com.example.courses.utils;

import com.example.courses.persistence.DAOFactory;
import com.example.courses.persistence.postgres.PostgresUserDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DAOUtils {
    private static final Logger logger = LogManager.getLogger(PostgresUserDAO.class.getName());

    public static long getGeneratedId(PreparedStatement statement) throws SQLException {
        logger.trace("Retrieving generated id from insert statement");

        long generatedId;
        ResultSet generatedKeys = null;

        try {
            generatedKeys = statement.getGeneratedKeys();
            if(generatedKeys.next()){
                generatedId = generatedKeys.getLong(1);
            } else {
                throw new SQLException();
            }
        } catch (SQLException e){
            logger.error("SQLException while retrieving generated id", e);
            throw e;
        } finally {
            DAOFactory.closeResource(generatedKeys);
        }

        return generatedId;
    }

}
