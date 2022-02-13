package com.example.courses.service;

import com.example.courses.persistence.DAOFactory;
import com.example.courses.persistence.SubjectDAO;
import com.example.courses.persistence.entity.Subject;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SubjectService {
    private final DAOFactory daoFactory;
    private final SubjectDAO subjectDAO;

    public SubjectService(){
        daoFactory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.POSTGRES);
        subjectDAO = daoFactory.getSubjectDao();
    }

    public long saveSubject(Subject subject) throws SQLException {
        long subjectId;
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            subjectId = subjectDAO.saveSubject(connection, subject);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            System.out.println(e.getMessage());
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return subjectId;
    }

    public void saveSubjectDescription(Subject subject) throws SQLException {
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            subjectDAO.saveSubjectDescription(connection, subject);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            System.out.println(e.getMessage());
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }
    }

    public Subject getSubject(long subjectId, long languageId) throws SQLException {
        Subject subject = new Subject();
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            subject = subjectDAO.findSubject(connection, subjectId, languageId);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            System.out.println(e.getMessage());
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return subject;
    }

    public List<Subject> getAll(long languageId) throws SQLException {
        List<Subject> subjectList = new ArrayList<>();
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            subjectList = subjectDAO.findAll(connection, languageId);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            System.out.println(e.getMessage());
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return subjectList;
    }

    public void deleteSubjectById(long subjectId) throws SQLException {
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            subjectDAO.deleteSubjectById(connection, subjectId);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            System.out.println(e.getMessage());
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }
    }
}
