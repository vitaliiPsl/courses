package com.example.courses.persistence.postgres;

import com.example.courses.persistence.*;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class PostgresDAOFactory implements DAOFactory {
    private final DataSource dataSource;
    private static PostgresDAOFactory instance;

    PostgresDAOFactory() {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/postgres");
        } catch (NamingException e) {
            System.out.println("jdbc/postgres is missing in JNDI!");
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
