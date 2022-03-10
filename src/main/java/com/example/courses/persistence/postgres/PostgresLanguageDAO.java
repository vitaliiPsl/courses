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
    public Language findLanguageById(Connection connection, long id, long translationLanguageId) throws SQLException {
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

                Language translation = findLanguageTranslation(connection, language.getId(), translationLanguageId);
                if(translation != null){
                    language.setName(translation.getName());
                }
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
    public List<Language> findAll(Connection connection, long translationLanguageId) throws SQLException {
        logger.trace("Find all languages");

        List<Language> languageList = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.prepareStatement(LanguageDAOConstants.SELECT_ALL);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Language language = parseLanguage(resultSet);

                Language translation = findLanguageTranslation(connection, language.getId(), translationLanguageId);
                if(translation != null){
                    language.setName(translation.getName());
                }

                languageList.add(language);
            }
        } finally {
            DAOFactory.closeResource(resultSet);
            DAOFactory.closeResource(statement);
        }

        return languageList;
    }

    private Language findLanguageTranslation(Connection connection, long languageId, long translationLanguageId) throws SQLException {
        Language language = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try{
            statement = connection.prepareStatement(LanguageDAOConstants.SELECT_LANGUAGE_TRANSLATION);
            statement.setLong(1, languageId);
            statement.setLong(2, translationLanguageId);
            resultSet = statement.executeQuery();

            if(resultSet.next()){
                language = new Language();
                language.setName(resultSet.getString(LanguageDAOConstants.LANGUAGE_TRANSLATION_NAME_TRANSLATION));
            }
        } finally {
            DAOFactory.closeResource(resultSet);
            DAOFactory.closeResource(statement);
        }

        return language;
    }

    private Language parseLanguage(ResultSet resultSet) throws SQLException {
        logger.trace("Parse language");

        Language language = new Language();
        try {
            language.setId(resultSet.getLong(LanguageDAOConstants.LANGUAGE_ID));
            language.setName(resultSet.getString(LanguageDAOConstants.LANGUAGE_NAME));
            language.setLanguageCode(resultSet.getString(LanguageDAOConstants.LANGUAGE_CODE));
        } catch (SQLException e){
            logger.error("Error while parsing language", e);
            throw e;
        }

        return language;
    }

    private static class LanguageDAOConstants {
        static final String TABLE_LANGUAGE = "language";
        static final String TABLE_LANGUAGE_TRANSLATION = "language_translation";

        static final String LANGUAGE_ID = "id";
        static final String LANGUAGE_NAME = "name";
        static final String LANGUAGE_CODE = "code";

        static final String LANGUAGE_TRANSLATION_LANGUAGE_ID = "language_id";
        static final String LANGUAGE_TRANSLATION_TRANSLATION_LANGUAGE_ID = "translation_language_id";
        static final String LANGUAGE_TRANSLATION_NAME_TRANSLATION = "name_translation";
        static final String LANGUAGE_TRANSLATION_CODE_TRANSLATION = "code_translation";

        static final String INSERT_LANGUAGE =
                "INSERT INTO " +
                        TABLE_LANGUAGE + "(" +
                        LANGUAGE_NAME + ", " +
                        LANGUAGE_CODE + " " +
                        ") VALUES(?, ?);";

        static final String DELETE_LANGUAGE_BY_ID =
                "DELETE FROM " +
                        TABLE_LANGUAGE + " " +
                        "WHERE " + LANGUAGE_ID + " = ?;";

        static final String SELECT_LANGUAGE_PROPERTIES =
                LANGUAGE_ID + ", " +
                LANGUAGE_NAME + ", " +
                LANGUAGE_CODE;

        static final String SELECT_LANGUAGE_BY_ID =
                "SELECT " +
                        SELECT_LANGUAGE_PROPERTIES + " " +
                        "FROM " + TABLE_LANGUAGE + " " +
                        "WHERE " + LANGUAGE_ID + " = ?;";

        static final String SELECT_LANGUAGE_BY_CODE =
                "SELECT " +
                        SELECT_LANGUAGE_PROPERTIES + " " +
                        "FROM " + TABLE_LANGUAGE + " " +
                        "WHERE " + LANGUAGE_CODE + " ilike ?;";

        static final String SELECT_LANGUAGE_TRANSLATION =
                "SELECT " +
                        LANGUAGE_TRANSLATION_LANGUAGE_ID + ", " +
                        LANGUAGE_TRANSLATION_TRANSLATION_LANGUAGE_ID + ", " +
                        LANGUAGE_TRANSLATION_NAME_TRANSLATION + " " +
                "FROM " + TABLE_LANGUAGE_TRANSLATION + " " +
                "WHERE " + LANGUAGE_TRANSLATION_LANGUAGE_ID + " = ?" +
                "AND " + LANGUAGE_TRANSLATION_TRANSLATION_LANGUAGE_ID + " = ?;";

        static final String SELECT_ALL =
                "SELECT " +
                        SELECT_LANGUAGE_PROPERTIES + " " +
                        "FROM " + TABLE_LANGUAGE + ";";
    }
}
