package com.example.courses.service;

import com.example.courses.persistence.CourseDAO;
import com.example.courses.persistence.DAOFactory;
import com.example.courses.persistence.entity.Course;
import com.example.courses.utils.CourseValidation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseService {
    private final DAOFactory daoFactory;
    private final CourseDAO courseDao;

    private static final Logger logger = LogManager.getLogger(CourseService.class.getName());

    public CourseService(){
        daoFactory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.POSTGRES);
        courseDao = daoFactory.getCourseDao();
    }

    public long saveNewCourse(Course course) throws SQLException {
        logger.trace("Save new course: " + course);

        if(!CourseValidation.isCourseValid(course)){
            logger.error("Course properties are invalid");
            throw new IllegalArgumentException("You have to provide valid data");
        }

        return saveCourse(course);
    }

    public long saveCourse(Course course) throws SQLException {
        logger.trace("Save course: " + course);

        long courseId;
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            courseId = courseDao.saveCourse(connection, course);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return courseId;
    }

    public void deleteCourse(long id) throws SQLException {
        logger.trace("Delete course by id: " + id);
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            courseDao.deleteCourseById(connection, id);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }
    }

    public void updateCourse(Course course) throws SQLException {
        logger.trace("Update course: " + course);
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            courseDao.updateCourse(connection, course);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }
    }
    public Course getCourseById(long courseId) throws SQLException {
        logger.trace("Get course by id: " + courseId);

        Course course;
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            course = courseDao.findCourse(connection, courseId);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return course;
    }

    public List<Course> getCourses(List<Long> ids) throws SQLException {
        logger.trace("Get list of courses by their ids: " + ids);

        List<Course> courseList = new ArrayList<>();
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            for(long id: ids) {
                courseList.add(courseDao.findCourse(connection, id));
            }
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return courseList;
    }

    public List<Course> getAll() throws SQLException {
        logger.trace("Get all courses");

        List<Course> courseList;
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            courseList = courseDao.findAll(connection);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return courseList;
    }

    public List<Course> getAvailable() throws SQLException {
        logger.trace("Get available courses");

        List<Course> courseList;
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            courseList = courseDao.findAvailable(connection);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return courseList;
    }

    public List<Course> getCoursesBySearchQuery(String query) throws SQLException {
        logger.trace("Get courses by search query: " + query);

        List<Course> courseList;
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            courseList = courseDao.findCoursesBySearchQuery(connection, query);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return courseList;
    }

    public List<Course> getAvailableCoursesBySearchQuery(String query) throws SQLException {
        logger.trace("Get available courses by search query: " + query);

        List<Course> courseList;
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            courseList = courseDao.findAvailableCoursesBySearchQuery(connection, query);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return courseList;
    }

    public List<Course> getCoursesByTeacherId(long teacherId) throws SQLException {
        logger.trace("Get courses by teacher id: " + teacherId);

        List<Course> courseList;
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            courseList = courseDao.findCoursesByTeacherId(connection, teacherId);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return courseList;
    }
}
