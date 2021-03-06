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

/**
 * PostgreSQL implementation of UserDAO
 *
 * @see com.example.courses.persistence.UserDAO
 */
public class PostgresUserDAO implements UserDAO {
    private static final Logger logger = LogManager.getLogger(PostgresUserDAO.class.getName());

    @Override
    public long saveUser(Connection connection, User user) throws SQLException {
        logger.info("Saving new user:" + user);

        long generatedId;
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(
                    UserDAOConstants.INSERT_PERSON,
                    Statement.RETURN_GENERATED_KEYS
            );

            setUserProperties(user, statement);
            statement.executeUpdate();

            generatedId = DAOUtils.getGeneratedId(statement);
            user.setId(generatedId);
        } finally {
            DAOFactory.closeResource(statement);
        }

        return generatedId;
    }

    @Override
    public void deleteUserById(Connection connection, long id) throws SQLException {
        logger.trace("Deleting user by id: " + id);

        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(UserDAOConstants.DELETE_PERSON_BY_ID);
            statement.setLong(1, id);
            statement.executeUpdate();
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

        try {
            statement = connection.prepareStatement(UserDAOConstants.SELECT_PERSON_BY_ID);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user = parsePerson(resultSet);
            }
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
        try {
            statement = connection.prepareStatement(UserDAOConstants.SELECT_PERSON_BY_EMAIL);
            System.out.println(statement);
            statement.setString(1, email);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user = parsePerson(resultSet);
            }
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

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(UserDAOConstants.SELECT_ALL);

            while (resultSet.next()) {
                userList.add(parsePerson(resultSet));
            }
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

        try {
            statement = connection.prepareStatement(UserDAOConstants.SELECT_ALL_BY_ROLE_NAME);
            statement.setString(1, role.getRole());
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                userList.add(parsePerson(resultSet));
            }
        } finally {
            DAOFactory.closeResource(resultSet);
            DAOFactory.closeResource(statement);
        }

        return userList;
    }

    private void setUserProperties(User user, PreparedStatement statement) throws SQLException {
        statement.setString(1, user.getFirstName());
        statement.setString(2, user.getLastName());
        statement.setString(3, user.getEmail());
        statement.setString(4, user.getPassword());
        statement.setBoolean(5, user.isBlocked());
        statement.setString(6, user.getImageName());
        statement.setInt(7, user.getRole().ordinal() + 1);
    }

    private User parsePerson(ResultSet resultSet) throws SQLException {
        logger.trace("Parsing user");

        User.Builder userBuilder = new User.Builder();

        userBuilder.setId(resultSet.getLong(UserDAOConstants.PERSON_ID))
                .setFirstName(resultSet.getString(UserDAOConstants.PERSON_FIRST_NAME))
                .setLastName(resultSet.getString(UserDAOConstants.PERSON_LAST_NAME))
                .setEmail(resultSet.getString(UserDAOConstants.PERSON_EMAIL))
                .setPassword(resultSet.getString(UserDAOConstants.PERSON_PASSWORD))
                .setBlocked(resultSet.getBoolean(UserDAOConstants.PERSON_IS_BLOCKED))
                .setImageName(resultSet.getString(UserDAOConstants.PERSON_IMAGE_NAME))
                .setRole(parseRole(resultSet.getString(UserDAOConstants.ROLE_NAME)));


        return userBuilder.build();
    }

    /**
     * Makes Role based on retrieved role name
     *
     * @param roleStr - role name
     * @return Role
     * @throws SQLException if role name is invalid
     */
    private Role parseRole(String roleStr) throws SQLException {
        Role role;

        switch (roleStr) {
            case "admin":
                role = Role.ADMIN;
                break;
            case "teacher":
                role = Role.TEACHER;
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

    private static class UserDAOConstants {

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
                        PERSON_LAST_NAME + ", " +
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
                        PERSON_LAST_NAME + " = ?," +
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
