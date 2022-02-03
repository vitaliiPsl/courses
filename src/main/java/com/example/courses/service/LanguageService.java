package com.example.courses.service;

import com.example.courses.persistence.DAOFactory;
import com.example.courses.persistence.LanguageDAO;
import com.example.courses.persistence.entity.Language;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class LanguageService {
    private final DAOFactory daoFactory;
    private final LanguageDAO languageDAO;

    public LanguageService() {
        daoFactory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.POSTGRES);
        languageDAO = daoFactory.getLanguageDao();
    }

    public Language getLanguageById(long languageId) throws SQLException {
        Language language = null;
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            language = languageDAO.findLanguageById(connection, languageId);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            System.out.println(e.getMessage());
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return language;
    }

    public Language getLanguageByName(String name) throws SQLException {
        Language language = null;
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            language = languageDAO.findLanguageByName(connection, name);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            System.out.println(e.getMessage());
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return language;
    }

    public List<Language> getAllLanguages() throws SQLException {
        List<Language> languageList = null;
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            languageList = languageDAO.findAll(connection);

            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            System.out.println(e.getMessage());
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return languageList;
    }
}
