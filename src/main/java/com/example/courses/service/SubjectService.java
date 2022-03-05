package com.example.courses.service;

import com.example.courses.persistence.DAOFactory;
import com.example.courses.persistence.SubjectDAO;
import com.example.courses.persistence.entity.Language;
import com.example.courses.persistence.entity.Subject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class SubjectService {
    private final DAOFactory daoFactory;
    private final SubjectDAO subjectDAO;

    private static final Logger logger = LogManager.getLogger(SubjectService.class.getName());

    public SubjectService(){
        daoFactory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.POSTGRES);
        subjectDAO = daoFactory.getSubjectDao();
    }

    public long saveSubject(Map<Language, String> subjectTranslations) throws SQLException {
        logger.info("Save subject: " + subjectTranslations);

        long subjectId;
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();

            Subject subject = new Subject();
            subjectId = subjectDAO.saveSubject(connection, subject);

            for(var entry: subjectTranslations.entrySet()){
                subject.setLanguageId(entry.getKey().getId());
                subject.setSubject(entry.getValue());
                subjectDAO.saveSubjectDescription(connection, subject);
            }

            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return subjectId;
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
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }
    }
}
