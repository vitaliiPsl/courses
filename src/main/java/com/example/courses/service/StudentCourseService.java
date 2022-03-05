package com.example.courses.service;

import com.example.courses.persistence.DAOFactory;
import com.example.courses.persistence.StudentCourseDAO;
import com.example.courses.persistence.entity.StudentCourse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class StudentCourseService {
    private final DAOFactory daoFactory;
    private final StudentCourseDAO studentCourseDAO;

    private static final Logger logger = LogManager.getLogger(StudentCourseService.class.getName());

    public StudentCourseService() {
        daoFactory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.POSTGRES);
        studentCourseDAO = daoFactory.getStudentCourseDao();
    }

    public void registerStudentForCourse(long studentId, long courseId) throws SQLException {logger.info("Initializing daoFactory and subjectDao");
        logger.trace(String.format("Register student %d for course %d", studentId, courseId));

        StudentCourse studentCourse = new StudentCourse();
        studentCourse.setStudentId(studentId);
        studentCourse.setCourseId(courseId);

        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            studentCourseDAO.saveStudentCourse(connection, studentCourse);
            connection.commit();
        } catch (SQLException e) {
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }
    }

    public void updateStudentCourses(List<StudentCourse> studentCourseList) throws SQLException {
        logger.trace("Update studentCourseS: " + studentCourseList);

        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            for(StudentCourse studentCourse: studentCourseList) {
                studentCourseDAO.updateStudentCourse(connection, studentCourse);
            }
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.closeResource(connection);
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }
    }

    public StudentCourse getStudentCourse(long studentId, long courseId) throws SQLException {
        logger.trace(String.format("Get student course by student id: %d and course id: %d", studentId, courseId));

        StudentCourse studentCourse;
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            studentCourse = studentCourseDAO.findStudentCourse(connection, studentId, courseId);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.closeResource(connection);
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return studentCourse;
    }

    public List<StudentCourse> getStudentsByCourseId(long courseId) throws SQLException {
        logger.trace(String.format("Get students by course id: %d", courseId));

        List<StudentCourse> studentCourseList;
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            studentCourseList = studentCourseDAO.findByCourseId(connection, courseId);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return studentCourseList;
    }

    public List<StudentCourse> getCoursesByStudentId(long studentId) throws SQLException {
        logger.trace(String.format("Get courses by student id: %d", studentId));

        List<StudentCourse> studentCourseList;
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            studentCourseList = studentCourseDAO.findByStudentId(connection, studentId);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return studentCourseList;
    }
}
