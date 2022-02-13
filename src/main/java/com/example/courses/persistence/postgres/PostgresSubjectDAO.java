package com.example.courses.persistence.postgres;

import com.example.courses.persistence.DAOFactory;
import com.example.courses.persistence.SubjectDAO;
import com.example.courses.persistence.entity.Subject;
import com.example.courses.utils.DAOUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostgresSubjectDAO implements SubjectDAO {

    @Override
    public long saveSubject(Connection connection, Subject subject) throws SQLException {
        long subjectId;

        PreparedStatement statement = null;

        try{
            statement = connection.prepareStatement(
                    SubjectDAOConstants.INSERT_SUBJECT,
                    PreparedStatement.RETURN_GENERATED_KEYS
            );
            statement.executeUpdate();

            subjectId = DAOUtils.getGeneratedId(statement);
            subject.setId(subjectId);
        } catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            DAOFactory.closeResource(statement);
        }

        return subjectId;
    }

    public void saveSubjectDescription(Connection connection, Subject subject) throws SQLException {
        PreparedStatement statement = null;

        try{
            statement = connection.prepareStatement(SubjectDAOConstants.INSERT_SUBJECT_DESCRIPTION);
            statement.setLong(1, subject.getId());
            statement.setString(2, subject.getSubject());
            statement.setLong(3, subject.getLanguageId());
            statement.executeUpdate();
        } catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            DAOFactory.closeResource(statement);
        }
    }

    @Override
    public void deleteSubjectById(Connection connection, long id) throws SQLException {
        PreparedStatement statement = null;

        try{
            statement = connection.prepareStatement(SubjectDAOConstants.DELETE_SUBJECT);
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            DAOFactory.closeResource(statement);
        }
    }

    @Override
    public void updateSubject(Connection connection, Subject subject) throws SQLException {
        PreparedStatement statement = null;

        try{
            statement = connection.prepareStatement(SubjectDAOConstants.UPDATE_SUBJECT);
            statement.setString(1, subject.getSubject());
            statement.setLong(2, subject.getId());
            statement.setLong(3, subject.getLanguageId());
            statement.executeUpdate();
        } catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            DAOFactory.closeResource(statement);
        }
    }

    @Override
    public Subject findSubject(Connection connection, long id, long languageId) throws SQLException {
        Subject subject = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try{
            statement = connection.prepareStatement(SubjectDAOConstants.SELECT_SUBJECT);
            statement.setLong(1, id);
            statement.setLong(2, languageId);
            resultSet = statement.executeQuery();
            if(resultSet.next()) {
                subject = parseSubjectDescription(resultSet);
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            DAOFactory.closeResource(resultSet);
            DAOFactory.closeResource(statement);
        }

        return subject;
    }

    @Override
    public List<Subject> findAll(Connection connection, long languageId) throws SQLException {
        List<Subject> subjectList = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try{
            statement = connection.prepareStatement(SubjectDAOConstants.SELECT_ALL);
            statement.setLong(1, languageId);
            resultSet = statement.executeQuery();
            if(resultSet.next()) {
                subjectList.add(parseSubjectDescription(resultSet));
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            DAOFactory.closeResource(resultSet);
            DAOFactory.closeResource(statement);
        }

        return subjectList;
    }

    private Subject parseSubjectDescription(ResultSet resultSet) throws SQLException {
        Subject subject = new Subject();
        subject.setId(resultSet.getLong(SubjectDAOConstants.SUBJECT_DESCRIPTION_SUBJECT_ID));
        subject.setSubject(resultSet.getString(SubjectDAOConstants.SUBJECT_DESCRIPTION_SUBJECT_NAME));
        subject.setLanguageId(resultSet.getLong(SubjectDAOConstants.SUBJECT_DESCRIPTION_LANGUAGE_ID));

        return subject;
    }

    static class SubjectDAOConstants {
        static final String TABLE_SUBJECT = "subject";
        static final String TABLE_SUBJECT_DESCRIPTION = "subject_description";
        static final String SUBJECT_ID = "id";
        static final String SUBJECT_DESCRIPTION_SUBJECT_ID = "subject_id";
        static final String SUBJECT_DESCRIPTION_SUBJECT_NAME = "name";
        static final String SUBJECT_DESCRIPTION_LANGUAGE_ID = "language_id";

        static final String INSERT_SUBJECT =
                "INSERT INTO " +
                        TABLE_SUBJECT + "(" +
                            SUBJECT_ID +
                        ") " +
                        "VALUES (DEFAULT);";

        static final String INSERT_SUBJECT_DESCRIPTION =
                "INSERT INTO " +
                        TABLE_SUBJECT_DESCRIPTION + "(" +
                            SUBJECT_DESCRIPTION_SUBJECT_ID + ", " +
                            SUBJECT_DESCRIPTION_SUBJECT_NAME + ", " +
                            SUBJECT_DESCRIPTION_LANGUAGE_ID +
                        ") VALUES (?, ?, ?);";


        static final String DELETE_SUBJECT =
                "DELETE FROM " +
                        TABLE_SUBJECT + " " +
                        "WHERE " + SUBJECT_ID + "=?;";

        static final String UPDATE_SUBJECT =
                "UPDATE " + TABLE_SUBJECT_DESCRIPTION + " " +
                "SET " + SUBJECT_DESCRIPTION_SUBJECT_NAME + " = ? " +
                "WHERE " + SUBJECT_DESCRIPTION_SUBJECT_ID + " = ? " +
                "AND " + SUBJECT_DESCRIPTION_LANGUAGE_ID + " = ?;";

        static final String SELECT_SUBJECT =
                "SELECT " +
                        SUBJECT_DESCRIPTION_SUBJECT_ID + ", " +
                        SUBJECT_DESCRIPTION_SUBJECT_NAME + ", " +
                        SUBJECT_DESCRIPTION_LANGUAGE_ID + " " +
                "FROM " + TABLE_SUBJECT_DESCRIPTION + " " +
                "WHERE " + SUBJECT_DESCRIPTION_SUBJECT_ID + " = ? " +
                "AND " + SUBJECT_DESCRIPTION_LANGUAGE_ID + " = ?;";

        static final String SELECT_ALL =
                "SELECT " +
                        SUBJECT_DESCRIPTION_SUBJECT_ID + ", " +
                        SUBJECT_DESCRIPTION_SUBJECT_NAME + ", " +
                        SUBJECT_DESCRIPTION_LANGUAGE_ID + " " +
                "FROM " + TABLE_SUBJECT_DESCRIPTION + " " +
                "WHERE " + SUBJECT_DESCRIPTION_LANGUAGE_ID + " = ?;";
    }
}
