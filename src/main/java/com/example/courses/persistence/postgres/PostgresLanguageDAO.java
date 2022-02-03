package com.example.courses.persistence.postgres;

import com.example.courses.persistence.DAOFactory;
import com.example.courses.persistence.LanguageDAO;
import com.example.courses.persistence.entity.Language;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresLanguageDAO implements LanguageDAO {
    @Override
    public long saveLanguage(Connection connection, Language language) throws SQLException {
        long languageId;
        PreparedStatement statement = null;
        ResultSet generatedKey = null;

        try {
            statement = connection.prepareStatement(
                    LanguageDAOConstants.INSERT_LANGUAGE,
                    Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, language.getName());
            statement.setString(2, language.getLanguageCode());
            statement.executeUpdate();

            generatedKey = statement.getGeneratedKeys();
            if (generatedKey.next()) {
                languageId = generatedKey.getLong(1);
            } else {
                throw new SQLException();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            DAOFactory.closeResource(generatedKey);
            DAOFactory.closeResource(statement);
        }

        return languageId;
    }

    @Override
    public void deleteLanguageById(Connection connection, long id) throws SQLException {
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(LanguageDAOConstants.DELETE_LANGUAGE_BY_ID);
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            DAOFactory.closeResource(statement);
        }
    }

    @Override
    public Language findLanguageById(Connection connection, long id) throws SQLException {
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
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            DAOFactory.closeResource(resultSet);
            DAOFactory.closeResource(statement);
        }

        return language;
    }

    @Override
    public Language findLanguageByName(Connection connection, String name) throws SQLException {
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
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            DAOFactory.closeResource(resultSet);
            DAOFactory.closeResource(statement);
        }

        return language;
    }

    @Override
    public Language findLanguageByCode(Connection connection, String code) throws SQLException {
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
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            DAOFactory.closeResource(resultSet);
            DAOFactory.closeResource(statement);
        }

        return language;
    }

    @Override
    public List<Language> findAll(Connection connection) throws SQLException {
        List<Language> languageList = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.prepareStatement(LanguageDAOConstants.SELECT_ALL);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                languageList.add(parseLanguage(resultSet));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            DAOFactory.closeResource(resultSet);
            DAOFactory.closeResource(statement);
        }

        return languageList;
    }

    private Language parseLanguage(ResultSet resultSet) throws SQLException {
        Language language = new Language();
        language.setId(resultSet.getLong(LanguageDAOConstants.LANGUAGE_ID));
        language.setName(resultSet.getString(LanguageDAOConstants.LANGUAGE_NAME));
        language.setLanguageCode(resultSet.getString(LanguageDAOConstants.LANGUAGE_CODE));

        return language;
    }

    private static class LanguageDAOConstants {
        static final String TABLE_LANGUAGE = "language";
        static final String LANGUAGE_ID = "id";
        static final String LANGUAGE_NAME = "name";
        static final String LANGUAGE_CODE = "code";

        static final String INSERT_LANGUAGE =
                "INSERT INTO " +
                        TABLE_LANGUAGE + "(" +
                        LANGUAGE_NAME + ", " +
                        LANGUAGE_CODE +
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

        static final String SELECT_ALL =
                "SELECT " +
                        SELECT_LANGUAGE_PROPERTIES + " " +
                        "FROM " + TABLE_LANGUAGE + ";";
    }
}
