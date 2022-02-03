package com.example.courses.persistence;

import com.example.courses.persistence.postgres.PostgresDAOFactory;

import java.sql.Connection;
import java.sql.SQLException;

public interface DAOFactory {

    static DAOFactory getDAOFactory(FactoryType factoryType) {
        if (factoryType.equals(FactoryType.POSTGRES)) {
            return PostgresDAOFactory.getInstance();
        } else {
            System.out.println("Invalid factory type");
            throw new IllegalArgumentException("Factory type: " + factoryType + " is invalid");
        }
    }

    Connection getConnection() throws SQLException;

    UserDAO getUserDao();

    static void closeResource(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    static void rollback(Connection connection) {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                System.out.println();
            }
        }
    }

    enum FactoryType {
        POSTGRES
    }
}
