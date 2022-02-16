package com.example.courses.persistence.postgres;

import com.example.courses.persistence.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class PostgresDAOFactory implements DAOFactory {
    private static final Logger logger = LogManager.getLogger(PostgresDAOFactory.class.getName());
    private final DataSource dataSource;
    private static PostgresDAOFactory instance;

    PostgresDAOFactory() {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/postgres");
        } catch (NamingException e) {
            logger.fatal("jdbc/postgres is missing in JNDI!", e);
            throw new IllegalStateException("jdbc/postgres" + " is missing in JNDI!", e);
        }
    }

    public static synchronized PostgresDAOFactory getInstance() {
        if (instance == null) {
            instance = new PostgresDAOFactory();
        }
        return instance;
    }

    @Override
    public synchronized Connection getConnection() throws SQLException {
        logger.trace("Getting connection");
        return dataSource.getConnection();
    }

    @Override
    public UserDAO getUserDao() {
        return new PostgresUserDAO();
    }

    @Override
    public CourseDAO getCourseDao() {
        return new PostgresCourseDAO();
    }

    @Override
    public LanguageDAO getLanguageDao() {
        return new PostgresLanguageDAO();
    }

    @Override
    public StudentCourseDAO getStudentCourseDao() {
        return new PostgresStudentCourseDAO();
    }

    @Override
    public SubjectDAO getSubjectDao() {
        return new PostgresSubjectDAO();
    }
}
