package com.example.courses.service;

import com.example.courses.persistence.DAOFactory;
import com.example.courses.persistence.StudentCourseDAO;
import com.example.courses.persistence.entity.StudentCourse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Service to work with the StudentCourse entity
 * @see com.example.courses.persistence.entity.StudentCourse
 */
public class StudentCourseService {
    private final DAOFactory daoFactory;
    private final StudentCourseDAO studentCourseDAO;

    private static final Logger logger = LogManager.getLogger(StudentCourseService.class.getName());

    public StudentCourseService() {
        daoFactory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.POSTGRES);
        studentCourseDAO = daoFactory.getStudentCourseDao();
    }

    public StudentCourseService(DAOFactory daoFactory, StudentCourseDAO studentCourseDAO){
        this.daoFactory = daoFactory;
        this.studentCourseDAO = studentCourseDAO;
    }

    /**
     * Register student for a course by making new entry in corresponding db table
     * @param studentId - student id
     * @param courseId - course id
     * @throws SQLException
     */
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
            DAOFactory.rollback(connection);
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }
    }

    /**
     * Updates StudentCourse records.
     * Method takes list of student course records, so there will be fewer calls to db
     * @param studentCourseList - list of studentCourse record
     * @throws SQLException
     */
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
            DAOFactory.rollback(connection);
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }
    }

    /**
     * Retrieves student course record by student and course id
     * @param studentId - student id
     * @param courseId - course id
     * @return StudentCourse record
     * @throws SQLException
     */
    public StudentCourse getStudentCourse(long studentId, long courseId) throws SQLException {
        logger.trace(String.format("Get student course by student id: %d and course id: %d", studentId, courseId));

        StudentCourse studentCourse;
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            studentCourse = studentCourseDAO.findStudentCourse(connection, studentId, courseId);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return studentCourse;
    }

    /**
     * Gets list of StudentsCourse records by course id
     * @param courseId - course id
     * @return list of StudentCourse records
     * @throws SQLException
     */
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

    /**
     * Gets list of StudentsCourse records by student id
     * @param studentId - student id
     * @return list of StudentCourse records
     * @throws SQLException
     */
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
