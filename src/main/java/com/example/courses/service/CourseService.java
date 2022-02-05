package com.example.courses.service;

import com.example.courses.persistence.CourseDAO;
import com.example.courses.persistence.DAOFactory;
import com.example.courses.persistence.entity.Course;
import com.example.courses.utils.CourseValidation;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseService {
    private final DAOFactory daoFactory;
    private final CourseDAO courseDao;

    public CourseService(){
        daoFactory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.POSTGRES);
        courseDao = daoFactory.getCourseDao();
    }

    public long saveNewCourse(Course course) throws SQLException {
        if(!CourseValidation.isCourseValid(course)){
            throw new IllegalArgumentException("You have to provide valid data");
        }

        return saveCourse(course);
    }

    public long saveCourse(Course course) throws SQLException {
        long courseId;
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            courseId = courseDao.saveCourse(connection, course);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            System.out.println(e.getMessage());
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return courseId;
    }

    public void deleteCourse(long id) throws SQLException {
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            courseDao.deleteCourseById(connection, id);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            System.out.println(e.getMessage());
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }
    }

    public void updateCourse(Course course) throws SQLException {
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            courseDao.updateCourse(connection, course);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            System.out.println(e.getMessage());
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }
    }
    public Course getCourseById(long courseId) throws SQLException {
        Course course = null;
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            course = courseDao.findCourse(connection, courseId);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            System.out.println(e.getMessage());
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return course;
    }

    public List<Course> getCourses(List<Long> ids) throws SQLException {
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
            System.out.println(e.getMessage());
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return courseList;
    }

    public List<Course> getAll() throws SQLException {
        List<Course> courseList = null;
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            courseList = courseDao.findAll(connection);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            System.out.println(e.getMessage());
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return courseList;
    }

    public List<Course> getAvailable() throws SQLException {
        List<Course> courseList = null;
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            courseList = courseDao.findAvailable(connection);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            System.out.println(e.getMessage());
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return courseList;
    }

    public List<Course> getCoursesBySearchQuery(String query) throws SQLException {
        List<Course> courseList;
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            courseList = courseDao.findCoursesBySearchQuery(connection, query);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            System.out.println(e.getMessage());
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return courseList;
    }

    public List<Course> getAvailableCoursesBySearchQuery(String query) throws SQLException {
        List<Course> courseList;
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            courseList = courseDao.findAvailableCoursesBySearchQuery(connection, query);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            System.out.println(e.getMessage());
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return courseList;
    }

    public List<Course> getCoursesByTeacherId(long teacherId) throws SQLException {
        List<Course> courseList = null;
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            courseList = courseDao.findCoursesByTeacherId(connection, teacherId);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            System.out.println(e.getMessage());
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return courseList;
    }

    public List<Course> getCoursesByLanguageId(long languageId) throws SQLException {
        List<Course> courseList = null;
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            courseList = courseDao.findCoursesByLanguageId(connection, languageId);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            System.out.println(e.getMessage());
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return courseList;
    }
}
