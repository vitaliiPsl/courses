package com.example.courses.persistence.postgres;

import com.example.courses.persistence.DAOFactory;
import com.example.courses.persistence.LanguageDAO;
import com.example.courses.persistence.entity.Language;
import com.example.courses.utils.DAOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * PostgreSQL implementation of LanguageDAO
 * @see com.example.courses.persistence.LanguageDAO
 */
public class PostgresLanguageDAO implements LanguageDAO {
    private static final Logger logger = LogManager.getLogger(PostgresDAOFactory.class.getName());

    @Override
    public long saveLanguage(Connection connection, Language language) throws SQLException {
        logger.trace("Save language: " + language);

        long generatedId;
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(
                    LanguageDAOConstants.INSERT_LANGUAGE,
                    Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, language.getName());
            statement.setString(2, language.getLanguageCode());
            statement.setBoolean(3, language.isDefault());
            statement.executeUpdate();

            generatedId = DAOUtils.getGeneratedId(statement);
            language.setId(generatedId);
        } finally {
            DAOFactory.closeResource(statement);
        }

        return generatedId;
    }

    @Override
    public void deleteLanguageById(Connection connection, long id) throws SQLException {
        logger.trace("Delete language by id: " + id);
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(LanguageDAOConstants.DELETE_LANGUAGE_BY_ID);
            statement.setLong(1, id);
            statement.executeUpdate();
        } finally {
            DAOFactory.closeResource(statement);
        }
    }

    @Override
    public Language findLanguageById(Connection connection, long id) throws SQLException {
        logger.trace("Find language by id: " + id);

        Language language = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.prepareStatement(LanguageDAOConstants.SELECT_LANGUAGE_BY_ID);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                language = parseLanguage(resultSet);
            }
        } finally {
            DAOFactory.closeResource(resultSet);
            DAOFactory.closeResource(statement);
        }

        return language;
    }

    @Override
    public Language findLanguageByName(Connection connection, String name) throws SQLException {
        logger.trace("Find language by name: " + name);
        Language language = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.prepareStatement(LanguageDAOConstants.SELECT_LANGUAGE_BY_NAME);
            statement.setString(1, name);

            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                language = parseLanguage(resultSet);
            }
        } finally {
            DAOFactory.closeResource(resultSet);
            DAOFactory.closeResource(statement);
        }

        return language;
    }

    @Override
    public Language findLanguageByCode(Connection connection, String code) throws SQLException {
        logger.trace("Find language by code: " + code);
        Language language = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.prepareStatement(LanguageDAOConstants.SELECT_LANGUAGE_BY_CODE);
            statement.setString(1, code);

            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                language = parseLanguage(resultSet);
            }
        } finally {
            DAOFactory.closeResource(resultSet);
            DAOFactory.closeResource(statement);
        }

        return language;
    }

    @Override
    public Language findDefaultLanguage(Connection connection) throws SQLException {
        logger.trace("Find default language");
        Language language = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.prepareStatement(LanguageDAOConstants.SELECT_DEFAULT);

            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                language = parseLanguage(resultSet);
            }
        } finally {
            DAOFactory.closeResource(resultSet);
            DAOFactory.closeResource(statement);
        }

        return language;
    }

    @Override
    public List<Language> findAll(Connection connection) throws SQLException {
        logger.trace("Find all languages");

        List<Language> languageList = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.prepareStatement(LanguageDAOConstants.SELECT_ALL);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                languageList.add(parseLanguage(resultSet));
            }
        } finally {
            DAOFactory.closeResource(resultSet);
            DAOFactory.closeResource(statement);
        }

        return languageList;
    }

    private Language parseLanguage(ResultSet resultSet) throws SQLException {
        logger.trace("Parse language");

        Language language = new Language();
        try {
            language.setId(resultSet.getLong(LanguageDAOConstants.LANGUAGE_ID));
            language.setName(resultSet.getString(LanguageDAOConstants.LANGUAGE_NAME));
            language.setLanguageCode(resultSet.getString(LanguageDAOConstants.LANGUAGE_CODE));
            language.setDefault(resultSet.getBoolean(LanguageDAOConstants.LANGUAGE_IS_DEFAULT));
        } catch (SQLException e){
            logger.error("Error while parsing language", e);
            throw e;
        }

        return language;
    }

    private static class LanguageDAOConstants {
        static final String TABLE_LANGUAGE = "language";
        static final String LANGUAGE_ID = "id";
        static final String LANGUAGE_NAME = "name";
        static final String LANGUAGE_CODE = "code";
        static final String LANGUAGE_IS_DEFAULT = "is_default";

        static final String INSERT_LANGUAGE =
                "INSERT INTO " +
                        TABLE_LANGUAGE + "(" +
                        LANGUAGE_NAME + ", " +
                        LANGUAGE_CODE + ", " +
                        LANGUAGE_IS_DEFAULT +
                        ") VALUES(?, ?, ?);";

        static final String DELETE_LANGUAGE_BY_ID =
                "DELETE FROM " +
                        TABLE_LANGUAGE + " " +
                        "WHERE " + LANGUAGE_ID + " = ?;";

        static final String SELECT_LANGUAGE_PROPERTIES =
                LANGUAGE_ID + ", " +
                LANGUAGE_NAME + ", " +
                LANGUAGE_CODE + ", " +
                LANGUAGE_IS_DEFAULT;

        static final String SELECT_LANGUAGE_BY_ID =
                "SELECT " +
                        SELECT_LANGUAGE_PROPERTIES + " " +
                        "FROM " + TABLE_LANGUAGE + " " +
                        "WHERE " + LANGUAGE_ID + " = ?;";

        static final String SELECT_LANGUAGE_BY_NAME =
                "SELECT " +
                        SELECT_LANGUAGE_PROPERTIES + " " +
                        "FROM " + TABLE_LANGUAGE + " " +
                        "WHERE " + LANGUAGE_NAME + " ilike ?;";

        static final String SELECT_LANGUAGE_BY_CODE =
                "SELECT " +
                        SELECT_LANGUAGE_PROPERTIES + " " +
                        "FROM " + TABLE_LANGUAGE + " " +
                        "WHERE " + LANGUAGE_CODE + " ilike ?;";

        static final String SELECT_DEFAULT =
                "SELECT " +
                        SELECT_LANGUAGE_PROPERTIES + " " +
                        "FROM " + TABLE_LANGUAGE + " " +
                        "WHERE " + LANGUAGE_IS_DEFAULT + " = true;";

        static final String SELECT_ALL =
                "SELECT " +
                        SELECT_LANGUAGE_PROPERTIES + " " +
                        "FROM " + TABLE_LANGUAGE + ";";
    }
}
