package com.example.courses.service;

import com.example.courses.persistence.DAOFactory;
import com.example.courses.persistence.UserDAO;
import com.example.courses.persistence.entity.Role;
import com.example.courses.persistence.entity.User;
import com.example.courses.utils.UserValidation;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private final DAOFactory daoFactory;
    private final UserDAO userDAO;

    public UserService(){
        daoFactory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.POSTGRES);
        userDAO = daoFactory.getUserDao();
    }

    public void registerUser(User user) throws SQLException {
        if(!UserValidation.isUserValid(user)){
            throw new IllegalArgumentException("You have to provide valid data");
        }

        User existing = getUserByEmail(user.getEmail());
        if(existing != null){
            throw new IllegalArgumentException(
                    "User with email " + user.getEmail() + " already exists"
            );
        }

        saveUser(user);
    }

    public long saveUser(User user) throws SQLException {
        long userId;
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            userId = userDAO.saveUser(connection, user);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            System.out.println(e.getMessage());
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return userId;
    }

    public void deleteUser(User user) throws SQLException{
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            userDAO.deleteUserById(connection, user.getId());
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            System.out.println(e.getMessage());
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }
    }

    public void updateUser(User user) throws SQLException{
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            userDAO.updateUser(connection, user);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            System.out.println(e.getMessage());
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }
    }

    public User getUserById(long id) throws SQLException {
        User user;
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            user = userDAO.findUser(connection, id);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            System.out.println(e.getMessage());
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return user;
    }

    public User getUserByEmail(String email) throws SQLException {
        User user;
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            user = userDAO.findUser(connection, email);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            System.out.println(e.getMessage());
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return user;
    }

    public List<User> getUsers(List<Long> ids) throws SQLException {
        List<User> userList = new ArrayList<>();
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            for(long id: ids) {
                userList.add(userDAO.findUser(connection, id));
            }
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            System.out.println(e.getMessage());
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return userList;
    }

    public List<User> getUsersByRole(Role role) throws SQLException {
        List<User> userList = null;
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            userList = userDAO.findAllByRole(connection, role);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            System.out.println(e.getMessage());
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return userList;
    }

    public void blockUserById(long id) throws SQLException {
        User user = getUserById(id);
        if(user != null) {
            user.setBlocked(true);
            updateUser(user);
        }
    }

    public void unblockUserById(long id) throws SQLException {
        User user = getUserById(id);
        if(user != null) {
            user.setBlocked(false);
            updateUser(user);
        }
    }
}
