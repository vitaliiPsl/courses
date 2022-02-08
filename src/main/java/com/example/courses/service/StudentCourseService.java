package com.example.courses.service;

import com.example.courses.persistence.DAOFactory;
import com.example.courses.persistence.StudentCourseDAO;
import com.example.courses.persistence.entity.StudentCourse;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class StudentCourseService {
    private final DAOFactory daoFactory;
    private final StudentCourseDAO studentCourseDAO;

    public StudentCourseService() {
        daoFactory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.POSTGRES);
        studentCourseDAO = daoFactory.getStudentCourseDao();
    }

    public void registerStudentForCourse(long studentId, long courseId) throws SQLException {
        StudentCourse studentCourse = new StudentCourse();
        studentCourse.setStudentId(studentId);
        studentCourse.setCourseId(courseId);

        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            studentCourseDAO.saveStudentCourse(connection, studentCourse);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            System.out.println(e.getMessage());
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }
    }

    public void updateStudentCourse(List<StudentCourse> studentCourseList) throws SQLException {
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            studentCourseDAO.updateStudentCourse(connection, studentCourseList);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.closeResource(connection);
            System.out.println(e.getMessage());
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }
    }

    public StudentCourse getStudentCourse(long studentId, long courseId) throws SQLException {
        StudentCourse studentCourse = null;
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            studentCourse = studentCourseDAO.findStudentCourse(connection, studentId, courseId);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.closeResource(connection);
            System.out.println(e.getMessage());
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return studentCourse;
    }

    public List<StudentCourse> getStudentsByCourseId(long courseId) throws SQLException {
        List<StudentCourse> studentCourseList = null;
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            studentCourseList = studentCourseDAO.findByCourseId(connection, courseId);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            System.out.println(e.getMessage());
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return studentCourseList;
    }

    public List<StudentCourse> getCoursesByStudentId(long studentId) throws SQLException {
        List<StudentCourse> studentCourseList = null;
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            studentCourseList = studentCourseDAO.findByStudentId(connection, studentId);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            System.out.println(e.getMessage());
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return studentCourseList;
    }
}
