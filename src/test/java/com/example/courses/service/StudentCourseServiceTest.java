package com.example.courses.service;

import com.example.courses.persistence.DAOFactory;
import com.example.courses.persistence.StudentCourseDAO;
import com.example.courses.persistence.entity.StudentCourse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentCourseServiceTest {

    StudentCourseService studentCourseService;

    @Mock
    DAOFactory daoFactory;

    @Mock
    StudentCourseDAO studentCourseDAO;

    @Mock
    Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        studentCourseService = new StudentCourseService(daoFactory, studentCourseDAO);
        lenient().when(daoFactory.getConnection()).thenReturn(connection);
    }

    @Test
    void testRegisterStudentForCourse() throws SQLException {
        long studentId = 1;
        long courseId = 1;

        StudentCourse studentCourse = new StudentCourse();
        studentCourse.setCourseId(courseId);
        studentCourse.setStudentId(studentId);

        studentCourseService.registerStudentForCourse(studentId, courseId);
        verify(studentCourseDAO).saveStudentCourse(connection, studentCourse);
        doThrow(new SQLException()).when(studentCourseDAO).saveStudentCourse(connection, studentCourse);

        assertThrows(SQLException.class, () -> studentCourseService.registerStudentForCourse(studentId, courseId));
    }

    @Test
    void testUpdateStudentCourses() throws SQLException {
        List<StudentCourse> studentCourseList = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            studentCourseList.add(new StudentCourse());
        }

        studentCourseService.updateStudentCourses(studentCourseList);
        verify(studentCourseDAO, times(studentCourseList.size())).updateStudentCourse(any(), any());

        doThrow(new SQLException()).when(studentCourseDAO).updateStudentCourse(any(), any());
        assertThrows(SQLException.class, () -> studentCourseService.updateStudentCourses(studentCourseList));
    }

    @Test
    void testGetStudentCourse() throws SQLException {
        long studentId = 1;
        long courseId = 1;

        studentCourseService.getStudentCourse(studentId, courseId);
        verify(studentCourseDAO).findStudentCourse(connection, studentId, courseId);

        doThrow(new SQLException()).when(studentCourseDAO).findStudentCourse(connection, studentId, courseId);
        assertThrows(SQLException.class, () -> studentCourseService.getStudentCourse(studentId, courseId));
    }

    @Test
    void testGetStudentsByCourseId() throws SQLException {
        long courseId = 1;

        studentCourseService.getStudentsByCourseId(courseId);
        verify(studentCourseDAO).findByCourseId(connection, courseId);

        doThrow(new SQLException()).when(studentCourseDAO).findByCourseId(connection, courseId);
        assertThrows(SQLException.class, () -> studentCourseService.getStudentsByCourseId(courseId));
    }


    @Test
    void testGetCoursesByStudentId() throws SQLException {
        long studentId = 1;

        studentCourseService.getCoursesByStudentId(studentId);
        verify(studentCourseDAO).findByStudentId(connection, studentId);

        doThrow(new SQLException()).when(studentCourseDAO).findByStudentId(connection, studentId);
        assertThrows(SQLException.class, () -> studentCourseService.getCoursesByStudentId(studentId));
    }
}