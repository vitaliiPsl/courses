package com.example.courses.service;

import com.example.courses.persistence.DAOFactory;
import com.example.courses.persistence.LanguageDAO;
import com.example.courses.persistence.entity.Language;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class LanguageService {
    private final DAOFactory daoFactory;
    private final LanguageDAO languageDAO;

    private static final Logger logger = LogManager.getLogger(LanguageService.class.getName());

    public LanguageService() {
        daoFactory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.POSTGRES);
        languageDAO = daoFactory.getLanguageDao();
    }

    public Language getLanguageById(long languageId) throws SQLException {
        logger.trace("Get language by id: " + languageId);

        Language language;
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            language = languageDAO.findLanguageById(connection, languageId);
            connection.commit();
        } catch (SQLException e) {
            logger.error("SQLException while retrieving language by id", e);
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return language;
    }

    public Language getLanguageByCode(String code) throws SQLException {
        logger.trace("Get language by code: " + code);

        Language language;
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            language = languageDAO.findLanguageByCode(connection, code);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            logger.error("SQLException while retrieving language by code", e);
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return language;
    }

    public List<Language> getAllLanguages() throws SQLException {
        logger.trace("Get all languages");

        List<Language> languageList;
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            languageList = languageDAO.findAll(connection);

            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            logger.error("SQLException while retrieving all languages", e);
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return languageList;
    }
}
