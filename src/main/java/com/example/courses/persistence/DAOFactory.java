package com.example.courses.persistence;

import com.example.courses.persistence.postgres.PostgresDAOFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

public interface DAOFactory {
    Logger logger = LogManager.getLogger(DAOFactory.class.getName());


    static DAOFactory getDAOFactory(FactoryType factoryType) {
        if (factoryType.equals(FactoryType.POSTGRES)) {
            return PostgresDAOFactory.getInstance();
        } else {
            logger.error("Invalid factory type: " + factoryType);
            throw new IllegalArgumentException("Factory type: " + factoryType + " is invalid");
        }
    }

    Connection getConnection() throws SQLException;

    UserDAO getUserDao();

    CourseDAO getCourseDao();

    LanguageDAO getLanguageDao();

    StudentCourseDAO getStudentCourseDao();

    SubjectDAO getSubjectDao();

    static void closeResource(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                logger.error("Error while closing resource", e);
            }
        }
    }

    static void rollback(Connection connection) {
        logger.trace("Rollback");
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                logger.error("Error during rollback", e);
            }
        }
    }

    enum FactoryType {
        POSTGRES
    }
}
