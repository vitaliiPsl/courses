package com.example.courses.persistence;

import com.example.courses.persistence.entity.StudentCourse;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface StudentCourseDAO {
    long saveStudentCourse(Connection connection, StudentCourse studentCourse) throws SQLException;
    void deleteStudentCourse(Connection connection, long studentId, long courseId) throws SQLException;
    void updateStudentCourse(Connection connection, List<StudentCourse> studentCourseList) throws SQLException;

    StudentCourse findStudentCourse(Connection connection, long studentId, long courseId) throws SQLException;
    List<StudentCourse> findByStudentId(Connection connection, long studentId) throws SQLException;
    List<StudentCourse> findByCourseId(Connection connection, long courseId) throws SQLException;
}
