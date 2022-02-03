package com.example.courses.persistence;

import com.example.courses.persistence.entity.Role;
import com.example.courses.persistence.entity.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserDAO {
    long saveUser(Connection connection, User user) throws SQLException;

    void deleteUserById(Connection connection, long id) throws SQLException;
    void updateUser(Connection connection, User user) throws SQLException;

    User findUser(Connection connection, long id) throws SQLException;
    User findUser(Connection connection, String email) throws SQLException;

    List<User> findAll(Connection connection) throws SQLException;
    List<User> findAllByRole(Connection connection, Role role) throws SQLException;
}
