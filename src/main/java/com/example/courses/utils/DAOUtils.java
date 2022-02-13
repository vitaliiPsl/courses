package com.example.courses.utils;

import com.example.courses.persistence.DAOFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DAOUtils {

    public static long getGeneratedId(PreparedStatement statement) throws SQLException {
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
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            DAOFactory.closeResource(generatedKeys);
        }

        return generatedId;
    }

}
