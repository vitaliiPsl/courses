package com.example.courses.service;

import com.example.courses.persistence.DAOFactory;
import com.example.courses.persistence.SubjectDAO;
import com.example.courses.persistence.entity.Subject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Service to work with the Subject entity
 * @see com.example.courses.persistence.entity.Subject
 */
public class SubjectService {
    private final DAOFactory daoFactory;
    private final SubjectDAO subjectDAO;

    private static final Logger logger = LogManager.getLogger(SubjectService.class.getName());

    public SubjectService(){
        daoFactory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.POSTGRES);
        subjectDAO = daoFactory.getSubjectDao();
    }

    public SubjectService(DAOFactory daoFactory, SubjectDAO subjectDAO){
        this.daoFactory = daoFactory;
        this.subjectDAO = subjectDAO;
    }

    /**
     * Saves new subject
     * @param subject - subject to save
     * @return generated id
     * @throws SQLException
     */
    public long saveSubject(Subject subject) throws SQLException {
        logger.info("Save subject: " + subject);

        long subjectId;
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            subjectId = subjectDAO.saveSubject(connection, subject);

            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return subjectId;
    }

    public void saveSubjectTranslation(Subject subject) throws SQLException {
        logger.info("Save subject translation: " + subject);

        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            subjectDAO.saveSubjectTranslation(connection, subject);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }
    }

    /**
     * Retrieves subject by id and language id
     * @param subjectId - subject id
     * @param languageId - id of translation language
     * @return subject or null if it doesn't exist
     * @throws SQLException
     */
    public Subject getSubject(long subjectId, long languageId) throws SQLException {
        logger.trace("Get subject. Subject id: " + subjectId + ". Language id: " + languageId);

        Subject subject;
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            subject = subjectDAO.findSubject(connection, subjectId, languageId);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return subject;
    }

    /**
     * Retrieves all subject
     * @param languageId - id of translation language
     * @return
     * @throws SQLException
     */
    public List<Subject> getAll(long languageId) throws SQLException {
        logger.trace("Get all subjects");

        List<Subject> subjectList;
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            subjectList = subjectDAO.findAll(connection, languageId);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return subjectList;
    }

    /**
     * Deletes subject by id
     * @param subjectId - id of subject to delete
     * @throws SQLException
     */
    public void deleteSubjectById(long subjectId) throws SQLException {
        logger.trace("Delete subject by id: " + subjectId);

        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            subjectDAO.deleteSubjectById(connection, subjectId);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }
    }
}
