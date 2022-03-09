package com.example.courses.service;

import com.example.courses.persistence.DAOFactory;
import com.example.courses.persistence.UserDAO;
import com.example.courses.persistence.entity.Role;
import com.example.courses.persistence.entity.User;
import com.example.courses.utils.HashingUtils;
import com.example.courses.utils.UserValidation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private final DAOFactory daoFactory;
    private final UserDAO userDAO;

    private static final Logger logger = LogManager.getLogger(UserService.class.getName());

    public UserService(){
        daoFactory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.POSTGRES);
        userDAO = daoFactory.getUserDao();
    }

    public UserService(DAOFactory daoFactory, UserDAO userDAO) {
        this.daoFactory = daoFactory;
        this.userDAO = userDAO;
    }

    /**
     * Registers new user
     * @param user - new user
     * @throws SQLException
     * @throws IllegalArgumentException if user is invalid or already exists
     */
    public long registerUser(User user) throws SQLException {
        logger.trace("Register user: " + user);

        // check if user is valid
        if(!UserValidation.isUserValid(user)){
            logger.warn("User's properties are invalid: " + user);
            throw new IllegalArgumentException("You have to provide valid data");
        }

        // check if user already exists
        User existing = getUserByEmail(user.getEmail());
        if(existing != null){
            logger.warn("User's with email: " + user.getEmail() + " already exists");
            throw new IllegalArgumentException("User with email " + user.getEmail() + " already exists");
        }

        // hash password
        String hashedPassword = HashingUtils.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);

        return saveUser(user);
    }

    /**
     * Saves user to db
     * @param user - user to save
     * @return generated id
     * @throws SQLException
     */
    public long saveUser(User user) throws SQLException {
        logger.trace("Save user: " + user);

        long userId;
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            userId = userDAO.saveUser(connection, user);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return userId;
    }

    /**
     * Deletes user from db
     * @param user - user to delete
     * @throws SQLException
     */
    public void deleteUser(User user) throws SQLException{
        logger.trace("Delete user: " + user);

        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            userDAO.deleteUserById(connection, user.getId());
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }
    }

    /**
     * Updates user info
     * @param user - user to update
     * @throws SQLException
     */
    public void updateUser(User user) throws SQLException{
        logger.trace("Update user: " + user);

        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            userDAO.updateUser(connection, user);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }
    }

    /**
     * Retrieves user by id
     * @param id - id of user to retrieve
     * @return user or null if there is no user in db with provided id
     * @throws SQLException
     */
    public User getUserById(long id) throws SQLException {
        logger.trace("Get user by id: " + id);

        User user;
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            user = userDAO.findUser(connection, id);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return user;
    }

    /**
     * Retrieves user by email
     * @param email - email of user to retrieve
     * @return user or null if there is no user in db with provided email
     * @throws SQLException
     */
    public User getUserByEmail(String email) throws SQLException {
        logger.trace("Get user by email: " + email);

        User user;
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            user = userDAO.findUser(connection, email);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return user;
    }

    /**
     * Retrieves users by their ids
     * @param ids - list of users ids
     * @return list of retrieved users
     * @throws SQLException
     */
    public List<User> getUsers(List<Long> ids) throws SQLException {
        logger.trace("Get list of users by ids: " + ids);

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
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return userList;
    }

    /**
     * Get users from db by their role
     * @param role - users role
     * @return list of retrieved users
     * @throws SQLException
     */
    public List<User> getUsersByRole(Role role) throws SQLException {
        logger.trace("Get list of users by role: " + role.getRole());

        List<User> userList = null;
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            userList = userDAO.findAllByRole(connection, role);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return userList;
    }

    /**
     * Blocks user by id
     * @param id - id of user to block
     * @throws SQLException
     */
    public void blockUser(long id) throws SQLException {
        logger.trace("Block user by id: " + id);

        User user = getUserById(id);
        if(user != null) {
            user.setBlocked(true);
            updateUser(user);
            logger.info("User with id: " + id + " is now blocked");
        }
    }

    /**
     * Unblocks user by id
     * @param id - id of user to unblock
     * @throws SQLException
     */
    public void unblockUser(long id) throws SQLException {
        logger.trace("Unblock user by id: " + id);

        User user = getUserById(id);
        if(user != null) {
            user.setBlocked(false);
            updateUser(user);
            logger.info("User with id: " + id + " is now unblocked");
        }
    }
}
