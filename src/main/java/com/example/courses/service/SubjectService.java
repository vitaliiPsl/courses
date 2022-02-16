package com.example.courses.service;

import com.example.courses.persistence.DAOFactory;
import com.example.courses.persistence.SubjectDAO;
import com.example.courses.persistence.entity.Subject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SubjectService {
    private final DAOFactory daoFactory;
    private final SubjectDAO subjectDAO;

    private static final Logger logger = LogManager.getLogger(SubjectService.class.getName());

    public SubjectService(){
        daoFactory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.POSTGRES);
        subjectDAO = daoFactory.getSubjectDao();
    }

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
            logger.error("SQLException while saving subject", e);
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return subjectId;
    }

    public void saveSubjectDescription(Subject subject) throws SQLException {
        logger.info("Save subject description: " + subject);

        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            subjectDAO.saveSubjectDescription(connection, subject);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            logger.error("SQLException while saving subject description", e);
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }
    }

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
            logger.error(String.format("SQLException while retrieving subject with id: %d and language id: %d", subjectId, languageId), e);
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return subject;
    }

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
            logger.error("SQLException while retrieving all subjects", e);
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return subjectList;
    }

    public void deleteSubjectById(long subjectId) throws SQLException {
        logger.trace("Delete subject by id: " + subjectId);

        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            subjectDAO.deleteSubjectById(connection, subjectId);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            logger.error(String.format("SQLException while deleting subjects with id: %d", subjectId), e);
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }
    }
}
