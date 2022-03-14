package com.example.courses.service;

import com.example.courses.persistence.CourseDAO;
import com.example.courses.persistence.DAOFactory;
import com.example.courses.persistence.entity.Course;
import com.example.courses.persistence.entity.CourseStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    CourseService courseService;

    @Mock
    DAOFactory daoFactory;

    @Mock
    CourseDAO courseDAO;

    @Mock
    Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        courseService = new CourseService(daoFactory, courseDAO);
        lenient().when(daoFactory.getConnection()).thenReturn(connection);
    }

    @Test
    void testSaveNewCourse() throws SQLException {
        Course course = getCourse();

        courseService.saveNewCourse(course);
        verify(courseDAO).saveCourse(connection, course);
    }

    @Test
    void testSaveNewCourseInvalidCourse(){
        Course course = getCourse();
        course.setTitle("");

        assertThrows(IllegalArgumentException.class, () -> courseService.saveNewCourse(course));
    }

    @Test
    void testSaveCourse() throws SQLException {
        Course course = getCourse();

        courseService.saveCourse(course);
        verify(courseDAO).saveCourse(connection, course);

        doThrow(SQLException.class).when(courseDAO).saveCourse(connection, course);
        assertThrows(SQLException.class, () -> courseService.saveCourse(course));
    }

    @Test
    void testDeleteCourse() throws SQLException {
        long courseId = 1;

        courseService.deleteCourse(courseId);
        verify(courseDAO).deleteCourseById(connection, courseId);

        doThrow(SQLException.class).when(courseDAO).deleteCourseById(connection, courseId);
        assertThrows(SQLException.class, () -> courseService.deleteCourse(courseId));
    }

    @Test
    void testUpdateCourse() throws SQLException {
        Course course = getCourse();

        courseService.updateCourse(course);
        verify(courseDAO).updateCourse(connection, course);

        doThrow(SQLException.class).when(courseDAO).updateCourse(connection, course);
        assertThrows(SQLException.class, () -> courseService.updateCourse(course));
    }

    @Test
    void testGetCourseById() throws SQLException {
        long courseId = 1;

        courseService.getCourseById(courseId);
        verify(courseDAO).findCourse(connection, courseId);

        doThrow(SQLException.class).when(courseDAO).findCourse(connection, courseId);
        assertThrows(SQLException.class, () -> courseService.getCourseById(courseId));
    }

    @Test
    void testGetCoursesByIds() throws SQLException {
        List<Long> ids = LongStream.range(1, 6).boxed().collect(Collectors.toList());

        courseService.getCourses(ids);
        verify(courseDAO, times(ids.size())).findCourse(any(), anyLong());

        doThrow(SQLException.class).when(courseDAO).findCourse(any(), anyLong());
        assertThrows(SQLException.class, () -> courseService.getCourses(ids));
    }

    @Test
    void testGetAll() throws  SQLException{

        courseService.getAll();
        verify(courseDAO).findAll(connection);

        doThrow(SQLException.class).when(courseDAO).findAll(connection);
        assertThrows(SQLException.class, () -> courseService.getAll());
    }

    @Test
    void testGetAvailable() throws  SQLException{
        courseService.getAvailable();
        verify(courseDAO).findAvailable(connection);

        doThrow(SQLException.class).when(courseDAO).findAvailable(connection);
        assertThrows(SQLException.class, () -> courseService.getAvailable());
    }

    @Test
    void testGetBySearchQuery() throws  SQLException{
        String query = "java";

        courseService.getBySearchQuery(query);
        verify(courseDAO).findCoursesBySearchQuery(connection, query);

        doThrow(SQLException.class).when(courseDAO).findCoursesBySearchQuery(connection, query);
        assertThrows(SQLException.class, () -> courseService.getBySearchQuery(query));
    }

    @Test
    void testGetAvailableBySearchQuery() throws  SQLException{
        String query = "java";

        courseService.getAvailableBySearchQuery(query);
        verify(courseDAO).findAvailableCoursesBySearchQuery(connection, query);

        doThrow(SQLException.class).when(courseDAO).findAvailableCoursesBySearchQuery(connection, query);
        assertThrows(SQLException.class, () -> courseService.getAvailableBySearchQuery(query));
    }


    @Test
    void testGetByTeacherId() throws  SQLException{
        long teacherId = 1;

        courseService.getByTeacherId(teacherId);
        verify(courseDAO).findCoursesByTeacherId(connection, teacherId);

        doThrow(SQLException.class).when(courseDAO).findCoursesByTeacherId(connection, teacherId);
        assertThrows(SQLException.class, () -> courseService.getByTeacherId(teacherId));
    }

    private static Course getCourse(){
        return new Course.Builder()
                .setTitle("Git")
                .setStartDate(LocalDateTime.now().plusHours(1))
                .setEndDate(LocalDateTime.now().plusDays(5))
                .setCourseStatus(CourseStatus.NOT_STARTED)
                .build();
    }
}