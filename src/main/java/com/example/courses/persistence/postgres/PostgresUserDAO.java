package com.example.courses.persistence.postgres;

import com.example.courses.persistence.DAOFactory;
import com.example.courses.persistence.UserDAO;
import com.example.courses.persistence.entity.Role;
import com.example.courses.persistence.entity.User;
import com.example.courses.utils.DAOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresUserDAO implements UserDAO {
    private static final Logger logger = LogManager.getLogger(PostgresUserDAO.class.getName());

    @Override
    public long saveUser(Connection connection, User user) throws SQLException {
        logger.info("Saving new user:" + user);

        long userId;
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(
                    UserDAOConstants.INSERT_PERSON,
                    Statement.RETURN_GENERATED_KEYS
            );

            setUserProperties(user, statement);
            statement.executeUpdate();

            userId = DAOUtils.getGeneratedId(statement);
        } catch (SQLException e) {
            logger.error("SQLException while saving user", e);
            throw e;
        } finally {
            DAOFactory.closeResource(statement);
        }

        return userId;
    }

    @Override
    public void deleteUserById(Connection connection, long id) throws SQLException {
        logger.trace("Deleting user by id: " + id);

        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(UserDAOConstants.DELETE_PERSON_BY_ID);
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("SQLException while deleting by user id", e);
            throw e;
        } finally {
            DAOFactory.closeResource(statement);
        }
    }

    @Override
    public void updateUser(Connection connection, User user) throws SQLException {
        logger.trace("Updating user: " + user);
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(UserDAOConstants.UPDATE_PERSON_BY_ID);

            setUserProperties(user, statement);
            statement.setLong(8, user.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("SQLException while updating user", e);
            throw e;
        } finally {
            DAOFactory.closeResource(statement);
        }
    }

    @Override
    public User findUser(Connection connection, long id) throws SQLException {
        logger.trace("Selecting user by id: " + id);

        User user = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try{
            statement = connection.prepareStatement(UserDAOConstants.SELECT_PERSON_BY_ID);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            if(resultSet.next()) {
                user = parsePerson(resultSet);
            }
        } catch (SQLException e) {
            logger.error("SQLException while selecting user by id", e);
            throw e;
        } finally {
            DAOFactory.closeResource(resultSet);
            DAOFactory.closeResource(statement);
        }

        return user;
    }

    @Override
    public User findUser(Connection connection, String email) throws SQLException {
        logger.trace("Selecting user by email: " + email);

        User user = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try{
            statement = connection.prepareStatement(UserDAOConstants.SELECT_PERSON_BY_EMAIL);
            System.out.println(statement);
            statement.setString(1, email);
            resultSet = statement.executeQuery();
            if(resultSet.next()) {
                user = parsePerson(resultSet);
            }
        } catch (SQLException e) {
            logger.error("SQLException while selecting user by email", e);
            throw e;
        } finally {
            DAOFactory.closeResource(resultSet);
            DAOFactory.closeResource(statement);
        }

        return user;
    }

    @Override
    public List<User> findAll(Connection connection) throws SQLException {
        logger.trace("Selecting all users");

        List<User> userList = new ArrayList<>();
        Statement statement = null;
        ResultSet resultSet = null;

        try{
            statement = connection.createStatement();
            resultSet = statement.executeQuery(UserDAOConstants.SELECT_ALL);

            while(resultSet.next()){
                userList.add(parsePerson(resultSet));
            }
        } catch (SQLException e) {
            logger.error("SQLException while selecting all users", e);
            throw e;
        } finally {
            DAOFactory.closeResource(resultSet);
            DAOFactory.closeResource(statement);
        }

        return userList;
    }

    @Override
    public List<User> findAllByRole(Connection connection, Role role) throws SQLException {
        logger.trace("Selecting users with role: " + role.getRole());

        List<User> userList = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try{
            statement = connection.prepareStatement(UserDAOConstants.SELECT_ALL_BY_ROLE_NAME);
            statement.setString(1, role.getRole());
            resultSet = statement.executeQuery();

            while(resultSet.next()){
                userList.add(parsePerson(resultSet));
            }
        } catch (SQLException e) {
            logger.error("SQLException while selecting users by role: " + role.getRole(), e);
            throw e;
        } finally {
            DAOFactory.closeResource(resultSet);
            DAOFactory.closeResource(statement);
        }

        return userList;
    }

    private void setUserProperties(User user, PreparedStatement statement) throws SQLException {
        try {
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPassword());
            statement.setBoolean(5, user.isBlocked());
            statement.setString(6, user.getImageName());
            statement.setInt(7, user.getRole().ordinal() + 1);
        } catch (SQLException e){
            logger.error("SQLException while setting user's properties", e);
            throw e;
        }
    }

    private User parsePerson(ResultSet resultSet) throws SQLException {
        logger.trace("Parsing user");

        User user = new User();

        try {
            user.setId(resultSet.getLong(UserDAOConstants.PERSON_ID));
            user.setFirstName(resultSet.getString(UserDAOConstants.PERSON_FIRST_NAME));
            user.setLastName(resultSet.getString(UserDAOConstants.PERSON_LAST_NAME));
            user.setEmail(resultSet.getString(UserDAOConstants.PERSON_EMAIL));
            user.setPassword(resultSet.getString(UserDAOConstants.PERSON_PASSWORD));
            user.setBlocked(resultSet.getBoolean(UserDAOConstants.PERSON_IS_BLOCKED));
            user.setImageName(resultSet.getString(UserDAOConstants.PERSON_IMAGE_NAME));
            user.setRole(parseRole(resultSet.getString(UserDAOConstants.ROLE_NAME)));
        } catch (SQLException e){
            logger.error("SQLException while building users", e);
            throw e;
        }

        return user;
    }

    private Role parseRole(String roleStr) throws SQLException {
        Role role;

        switch (roleStr){
            case "admin":
                role = Role.ADMIN;
                break;
            case "teacher":
                role =  Role.TEACHER;
                break;
            case "student":
                role = Role.STUDENT;
                break;
            default:
                logger.warn("Unknown role:" + roleStr);
                throw new SQLException("Unknown role: " + roleStr);
        }

        return role;
    }

    private static class UserDAOConstants{

        static final String TABLE_PERSON = "person";
        static final String TABLE_ROLE = "role";
        static final String PERSON_ID = "id";
        static final String PERSON_ROLE_ID = "role_id";
        static final String PERSON_FIRST_NAME = "first_name";
        static final String PERSON_LAST_NAME = "last_name";
        static final String PERSON_EMAIL = "email";
        static final String PERSON_PASSWORD = "password";
        static final String PERSON_IS_BLOCKED = "is_blocked";
        static final String PERSON_IMAGE_NAME = "image_name";
        static final String ROLE_ID = "id";
        static final String ROLE_NAME = "name";

        static final String INSERT_PERSON =
                "INSERT INTO " +
                        TABLE_PERSON + "(" +
                        PERSON_FIRST_NAME + ", " +
                        PERSON_LAST_NAME  + ", " +
                        PERSON_EMAIL + ", " +
                        PERSON_PASSWORD + ", " +
                        PERSON_IS_BLOCKED + ", " +
                        PERSON_IMAGE_NAME + ", " +
                        PERSON_ROLE_ID +
                        ") VALUES(?, ?, ?, ?, ?, ?, ?)";

        static final String UPDATE_PERSON_BY_ID =
                "UPDATE " + TABLE_PERSON + " " +
                        "SET " +
                        PERSON_FIRST_NAME + " = ?," +
                        PERSON_LAST_NAME  + " = ?," +
                        PERSON_EMAIL + " = ?," +
                        PERSON_PASSWORD + " = ?," +
                        PERSON_IS_BLOCKED + " = ?," +
                        PERSON_IMAGE_NAME + " = ?," +
                        PERSON_ROLE_ID + " = ? " +
                        "WHERE " + PERSON_ID + " =?";

        static final String DELETE_PERSON_BY_ID =
                "DELETE " +
                        "FROM " + TABLE_PERSON + " " +
                        "WHERE " + PERSON_ID + " = ?";

        static final String PERSON_PROPERTIES =
                "p." + PERSON_ID + ", " +
                        "p." + PERSON_FIRST_NAME + ", " +
                        "p." + PERSON_LAST_NAME + ", " +
                        "p." + PERSON_EMAIL + ", " +
                        "p." + PERSON_PASSWORD + ", " +
                        "p." + PERSON_IS_BLOCKED + ", " +
                        "p." + PERSON_IMAGE_NAME + ", " +
                        "r." + ROLE_NAME;

        static final String JOIN_PERSON_ROLE =
                "FROM " + TABLE_PERSON + " AS p " +
                        "JOIN " + TABLE_ROLE + " AS r " +
                        "ON " + "p." + PERSON_ROLE_ID + "= r." + ROLE_ID;

        static final String SELECT_PERSON_BY_ID =
                "SELECT " +
                        PERSON_PROPERTIES + " " +
                        JOIN_PERSON_ROLE + " " +
                        "WHERE p." + PERSON_ID + "= ?";

        static final String SELECT_PERSON_BY_EMAIL =
                "SELECT " +
                        PERSON_PROPERTIES + " " +
                        JOIN_PERSON_ROLE + " " +
                        "WHERE " + PERSON_EMAIL + " ilike ?";

        static final String SELECT_ALL =
                "SELECT " +
                        PERSON_PROPERTIES + " " +
                        JOIN_PERSON_ROLE + " " +
                        "ORDER BY p." + PERSON_ID;

        static final String SELECT_ALL_BY_ROLE_NAME =
                "SELECT " +
                        PERSON_PROPERTIES + " " +
                        JOIN_PERSON_ROLE + " " +
                        "WHERE r." + ROLE_NAME + "=? " +
                        "ORDER BY p." + PERSON_ID;
    }
}
