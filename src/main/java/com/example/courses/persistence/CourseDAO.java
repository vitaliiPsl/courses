package com.example.courses.persistence;

import com.example.courses.persistence.entity.Course;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface CourseDAO {
    long saveCourse(Connection connection, Course course) throws SQLException;
    void deleteCourseById(Connection connection, long id) throws SQLException;
    void updateCourse(Connection connection, Course course) throws SQLException;

    Course findCourse(Connection connection, long courseId) throws SQLException;

    List<Course> findAll(Connection connection) throws SQLException;
    List<Course> findAvailable(Connection connection) throws SQLException;
    List<Course> findCoursesBySearchQuery(Connection connection, String query) throws SQLException;
    List<Course> findAvailableCoursesBySearchQuery(Connection connection, String query) throws SQLException;
    List<Course> findCoursesByTeacherId(Connection connection, long teacherId) throws SQLException;
    List<Course> findCoursesByLanguageId(Connection connection, long languageId) throws SQLException;
}
