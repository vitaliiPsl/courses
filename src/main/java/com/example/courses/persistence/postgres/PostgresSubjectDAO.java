package com.example.courses.persistence.postgres;

import com.example.courses.persistence.DAOFactory;
import com.example.courses.persistence.SubjectDAO;
import com.example.courses.persistence.entity.Subject;
import com.example.courses.utils.DAOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * PostgreSQL implementation of SubjectDAO
 * @see com.example.courses.persistence.SubjectDAO
 */
public class PostgresSubjectDAO implements SubjectDAO {
    private static final Logger logger = LogManager.getLogger(PostgresDAOFactory.class.getName());

    /**
     * Adds new id to subject table and sets subject id
     * @param connection - db connection
     * @param subject - new subject
     * @return generated id
     * @throws SQLException
     */
    @Override
    public long saveSubject(Connection connection, Subject subject) throws SQLException {
        logger.trace("Save subject: " + subject);
        long subjectId;
        PreparedStatement statement = null;

        try{
            statement = connection.prepareStatement(
                    SubjectDAOConstants.INSERT_SUBJECT,
                    PreparedStatement.RETURN_GENERATED_KEYS
            );
            statement.setString(1, subject.getSubject());
            statement.executeUpdate();

            subjectId = DAOUtils.getGeneratedId(statement);
            subject.setId(subjectId);
        } finally {
            DAOFactory.closeResource(statement);
        }

        return subjectId;
    }

    /**
     * Saves subject translation
     * @param connection - db connection
     * @param subject - subject translation
     * @throws SQLException
     */
    @Override
    public void saveSubjectTranslation(Connection connection, Subject subject) throws SQLException {
        logger.trace("Save subject description: " + subject);
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(SubjectDAOConstants.INSERT_SUBJECT_TRANSLATION);
            statement.setLong(1, subject.getId());
            statement.setString(2, subject.getSubject());
            statement.setLong(3, subject.getLanguageId());
            statement.executeUpdate();
        } finally {
            DAOFactory.closeResource(statement);
        }
    }

    @Override
    public void deleteSubjectById(Connection connection, long id) throws SQLException {
        logger.trace("Delete subject by id: " + id);
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(SubjectDAOConstants.DELETE_SUBJECT);
            statement.setLong(1, id);
            statement.executeUpdate();
        } finally {
            DAOFactory.closeResource(statement);
        }
    }

    @Override
    public void updateSubjectDescription(Connection connection, Subject subject) throws SQLException {
        logger.trace("Update subject: " + subject);
        PreparedStatement statement = null;

        try{
            statement = connection.prepareStatement(SubjectDAOConstants.UPDATE_SUBJECT);
            statement.setString(1, subject.getSubject());
            statement.setLong(2, subject.getId());
            statement.setLong(3, subject.getLanguageId());
            statement.executeUpdate();
        } finally {
            DAOFactory.closeResource(statement);
        }
    }

    @Override
    public Subject findSubject(Connection connection, long subjectId, long languageId) throws SQLException {
        Subject subject = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try{
            statement = connection.prepareStatement(SubjectDAOConstants.SELECT_SUBJECT);
            statement.setLong(1, subjectId);
            resultSet = statement.executeQuery();

            if(resultSet.next()){
                subject = parseSubject(resultSet);
                Subject translation = getSubjectTranslation(connection, subjectId, languageId);

                if(translation != null){
                    subject.setSubject(translation.getSubject());
                }
            }
        } finally {
            DAOFactory.closeResource(resultSet);
            DAOFactory.closeResource(statement);
        }

        return subject;
    }

    @Override
    public List<Subject> findAll(Connection connection, long languageId) throws SQLException {
        logger.trace("Find all subject by language id: " + languageId);

        Subject subject = null;
        List<Subject> subjectList = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try{
            statement = connection.prepareStatement(SubjectDAOConstants.SELECT_ALL);
            resultSet = statement.executeQuery();

            while(resultSet.next()) {
                subject = parseSubject(resultSet);
                Subject translation = getSubjectTranslation(connection, subject.getId(), languageId);

                if(translation != null){
                    subject.setSubject(translation.getSubject());
                }
                subjectList.add(subject);
            }
        } finally {
            DAOFactory.closeResource(resultSet);
            DAOFactory.closeResource(statement);
        }

        return subjectList;
    }

    private Subject getSubjectTranslation(Connection connection, long subjectId, long languageId) throws SQLException {
        Subject subject = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try{
            statement = connection.prepareStatement(SubjectDAOConstants.SELECT_SUBJECT_TRANSLATION);
            statement.setLong(1, subjectId);
            statement.setLong(2, languageId);
            resultSet = statement.executeQuery();

            if(resultSet.next()){
                subject = new Subject();
                subject.setId(resultSet.getLong(SubjectDAOConstants.SUBJECT_TRANSLATION_SUBJECT_ID));
                subject.setLanguageId(resultSet.getLong(SubjectDAOConstants.SUBJECT_TRANSLATION_LANGUAGE_ID));
                subject.setSubject(resultSet.getString(SubjectDAOConstants.SUBJECT_TRANSLATION_SUBJECT_NAME));
            }
        } finally {
            DAOFactory.closeResource(resultSet);
            DAOFactory.closeResource(statement);
        }

        return subject;
    }

    private Subject parseSubject(ResultSet resultSet) throws SQLException {
        logger.trace("Parse subject description");

        Subject subject = new Subject();
        subject.setId(resultSet.getLong(SubjectDAOConstants.SUBJECT_ID));
        subject.setSubject(resultSet.getString(SubjectDAOConstants.SUBJECT_NAME));

        return subject;
    }

    static class SubjectDAOConstants {
        static final String TABLE_SUBJECT = "subject";
        static final String TABLE_SUBJECT_TRANSLATION = "subject_translation";
        static final String SUBJECT_ID = "id";
        static final String SUBJECT_NAME = "name";
        static final String SUBJECT_TRANSLATION_SUBJECT_ID = "subject_id";
        static final String SUBJECT_TRANSLATION_SUBJECT_NAME = "name_translation";
        static final String SUBJECT_TRANSLATION_LANGUAGE_ID = "language_id";

        static final String INSERT_SUBJECT =
                "INSERT INTO " + TABLE_SUBJECT + "(" +
                            SUBJECT_NAME +
                ") " + "VALUES (?);";

        static final String INSERT_SUBJECT_TRANSLATION =
                "INSERT INTO " + TABLE_SUBJECT_TRANSLATION + "(" +
                        SUBJECT_TRANSLATION_SUBJECT_ID + ", " +
                        SUBJECT_TRANSLATION_SUBJECT_NAME + ", " +
                        SUBJECT_TRANSLATION_LANGUAGE_ID +
                ") VALUES (?, ?, ?);";


        static final String DELETE_SUBJECT =
                "DELETE FROM " +
                        TABLE_SUBJECT + " " +
                "WHERE " + SUBJECT_ID + "=?;";

        static final String UPDATE_SUBJECT =
                "UPDATE " + TABLE_SUBJECT_TRANSLATION + " " +
                "SET " + SUBJECT_TRANSLATION_SUBJECT_NAME + " = ? " +
                "WHERE " + SUBJECT_TRANSLATION_SUBJECT_ID + " = ? " +
                "AND " + SUBJECT_TRANSLATION_LANGUAGE_ID + " = ?;";

        static final String SELECT_SUBJECT =
                "SELECT " +
                        SUBJECT_ID + ", " +
                        SUBJECT_NAME + " " +
                "FROM " + TABLE_SUBJECT + " " +
                "WHERE " + SUBJECT_ID + " = ?;";

        static final String SELECT_SUBJECT_TRANSLATION =
                "SELECT " +
                        SUBJECT_TRANSLATION_SUBJECT_ID + ", " +
                        SUBJECT_TRANSLATION_LANGUAGE_ID + ", " +
                        SUBJECT_TRANSLATION_SUBJECT_NAME + " " +
                "FROM " + TABLE_SUBJECT_TRANSLATION + " " +
                "WHERE " + SUBJECT_TRANSLATION_SUBJECT_ID + " = ? " +
                "AND " + SUBJECT_TRANSLATION_LANGUAGE_ID + " = ?;";

        static final String SELECT_ALL =
                "SELECT " +
                        SUBJECT_ID + ", " +
                        SUBJECT_NAME + " " +
                "FROM " + TABLE_SUBJECT + ";";
    }
}
