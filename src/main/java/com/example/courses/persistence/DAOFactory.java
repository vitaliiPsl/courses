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

    enum FactoryType {
        POSTGRES
    }
}
