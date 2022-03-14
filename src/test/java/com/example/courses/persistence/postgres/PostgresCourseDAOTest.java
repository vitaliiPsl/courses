package com.example.courses.persistence.postgres;

import com.example.courses.persistence.CourseDAO;
import com.example.courses.persistence.entity.Course;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PostgresCourseDAOTest {

    static String dbUrl = "jdbc:h2:mem:myDb;DB_CLOSE_DELAY=-1";

    String CREATE_TABLE_COURSE =
            "CREATE TABLE course\n" +
                    "(\n" +
                    "    id          BIGSERIAL,\n" +
                    "    teacher_id  BIGINT       NOT NULL,\n" +
                    "    subject_id  BIGINT       NOT NULL,\n" +
                    "    language_id INT,\n" +
                    "    title       VARCHAR(100) NOT NULL,\n" +
                    "    description VARCHAR(512),\n" +
                    "    max_score   INT,\n" +
                    "    start_date  TIMESTAMP,\n" +
                    "    end_date    TIMESTAMP,\n" +
                    "    image_name  VARCHAR(256),\n" +
                    "    CONSTRAINT pk_course PRIMARY KEY (id)\n" +
                    ")";

    String DROP_TABLE_COURSE = "DROP TABLE course";

    static CourseDAO courseDAO;
    static Connection connection;

    @BeforeAll
    static void globalSetUp() throws SQLException {
        courseDAO = new PostgresCourseDAO();
        connection = DriverManager.getConnection(dbUrl);
    }

    @BeforeEach
    void setUp() throws SQLException {
        connection.createStatement().executeUpdate(CREATE_TABLE_COURSE);
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.createStatement().executeUpdate(DROP_TABLE_COURSE);
    }

    @Test
    void testConnection(){
        assertNotNull(connection);
    }

    @Test
    void findCourse() throws SQLException {
        long courseId = 1;
        final String courseTitle = "Java";

        String insertStatement =
                "INSERT INTO course(id, teacher_id, subject_id, language_id, title, start_date, end_date)" +
                "VALUES(" + courseId + ", 1, 1, 1, '" + courseTitle + "', '2021-12-24 16:21:52', '2022-03-22 20:01:39')";
        connection.createStatement().executeUpdate(insertStatement);

        Course course = courseDAO.findCourse(connection, courseId);
        assertNotNull(course);
        assertEquals(courseTitle, course.getTitle());
    }

    @Test
    void testSaveCourse() throws SQLException {
        Course course = getCourse("Title");

        long id = courseDAO.saveCourse(connection, course);

        Course result = courseDAO.findCourse(connection, id);
        assertEquals(course, result);
    }

    @Test
    void testDeleteCourseById() throws SQLException {
        Course course = getCourse("Title");
        long id = courseDAO.saveCourse(connection, course);

        courseDAO.deleteCourseById(connection, id);

        Course result = courseDAO.findCourse(connection, id);
        assertNull(result);
    }

    @Test
    void testUpdateCourse() throws SQLException {
        Course course = getCourse("Title");
        long id = courseDAO.saveCourse(connection, course);

        String newTitle = "New title";
        course.setTitle(newTitle);
        courseDAO.updateCourse(connection, course);

        Course result = courseDAO.findCourse(connection, id);
        assertEquals(newTitle, result.getTitle());
    }

    @Test
    void testFindAll() throws SQLException {
        int numberOfCourses = 3;
        for(int i = 0; i < numberOfCourses; i++){
            courseDAO.saveCourse(connection, getCourse("Random"));
        }

        List<Course> courseList = courseDAO.findAll(connection);
        assertEquals(numberOfCourses, courseList.size());
    }

    @Test
    void findAvailable() throws SQLException {
        int numberOfCourses = 3;
        for(int i = 0; i < numberOfCourses; i++){
            courseDAO.saveCourse(connection, getCourse("Random"));
        }

        Course course = getCourse("Java");
        course.setStartDate(LocalDateTime.now().minusDays(1));
        courseDAO.saveCourse(connection, course);

        // result should not include last inserted course
        List<Course> courseList = courseDAO.findAvailable(connection);
        assertEquals(numberOfCourses, courseList.size());
        assertFalse(courseList.contains(course));
    }

    @Test
    void findCoursesBySearchQuery() throws SQLException {
        Course course = getCourse("Java");
        Course course1 = getCourse("JavaScript");

        courseDAO.saveCourse(connection, course);
        courseDAO.saveCourse(connection, course1);

        String query = "ava";

        List<Course> courseList = courseDAO.findCoursesBySearchQuery(connection, query);
        assertEquals(2, courseList.size());
        assertTrue(courseList.contains(course));
        assertTrue(courseList.contains(course1));
    }

    @Test
    void findAvailableCoursesBySearchQuery() throws SQLException {
        Course course = getCourse("Java");
        Course course1 = getCourse("JavaScript");
        Course course2 = getCourse("Java. Servlets");
        course2.setStartDate(LocalDateTime.now().minusDays(1));

        courseDAO.saveCourse(connection, course);
        courseDAO.saveCourse(connection, course1);
        courseDAO.saveCourse(connection, course2);

        String query = "ava";

        List<Course> courseList = courseDAO.findAvailableCoursesBySearchQuery(connection, query);
        assertEquals(2, courseList.size());
        assertTrue(courseList.contains(course));
        assertTrue(courseList.contains(course1));
        assertFalse(courseList.contains(course2));
    }

    @Test
    void findCoursesByTeacherId() throws SQLException {
        Course course = getCourse("AWS");
        course.setTeacherId(5);

        Course course1 = getCourse("Google cloud");
        course1.setTeacherId(5);

        Course course2 = getCourse("Azure");
        course2.setTeacherId(3);

        courseDAO.saveCourse(connection, course);
        courseDAO.saveCourse(connection, course1);
        courseDAO.saveCourse(connection, course2);

        List<Course> courseList = courseDAO.findCoursesByTeacherId(connection, 5);
        assertEquals(2, courseList.size());
        assertTrue(courseList.contains(course));
        assertTrue(courseList.contains(course1));
        assertFalse(courseList.contains(course2));
    }

    @Test
    void findCoursesByLanguageId() throws SQLException {
        Course course = getCourse("AWS");
        course.setLanguageId(1);

        Course course1 = getCourse("Google cloud");
        course1.setLanguageId(2);

        Course course2 = getCourse("Azure");
        course2.setLanguageId(2);

        courseDAO.saveCourse(connection, course);
        courseDAO.saveCourse(connection, course1);
        courseDAO.saveCourse(connection, course2);

        List<Course> courseList = courseDAO.findCoursesByLanguageId(connection, 2);
        assertEquals(2, courseList.size());
        assertFalse(courseList.contains(course));
        assertTrue(courseList.contains(course1));
        assertTrue(courseList.contains(course2));
    }

    private Course getCourse(String title) {
        return new Course.Builder()
                .setTitle(title)
                .setTeacherId(1)
                .setSubjectId(1)
                .setLanguageId(1)
                .setStartDate(LocalDateTime.now().plusHours(1))
                .setEndDate(LocalDateTime.now().plusDays(10))
                .build();
    }
}