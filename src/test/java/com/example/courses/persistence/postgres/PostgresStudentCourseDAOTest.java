package com.example.courses.persistence.postgres;

import com.example.courses.persistence.StudentCourseDAO;
import com.example.courses.persistence.entity.StudentCourse;
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

class PostgresStudentCourseDAOTest {

    static String dbUrl = "jdbc:h2:mem:myDb;DB_CLOSE_DELAY=-1";

    String CREATE_TABLE_STUDENT_COURSE =
            "CREATE TABLE student_course\n" +
                    "(\n" +
                    "    student_id        BIGINT NOT NULL,\n" +
                    "    course_id         BIGINT NOT NULL,\n" +
                    "    score             INT,\n" +
                    "    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n" +
                    "    CONSTRAINT pk_student_course PRIMARY KEY (student_id, course_id)\n" +
                    ")";

    String DROP_TABLE_STUDENT_COURSE =
            "DROP TABLE student_course";

    static StudentCourseDAO studentCourseDAO;
    static Connection connection;

    @BeforeAll
    static void globalSetUp() throws SQLException {
        studentCourseDAO = new PostgresStudentCourseDAO();
        connection = DriverManager.getConnection(dbUrl);
    }

    @BeforeEach
    void setUp() throws SQLException {
        connection.createStatement().executeUpdate(CREATE_TABLE_STUDENT_COURSE);
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.createStatement().executeUpdate(DROP_TABLE_STUDENT_COURSE);
    }

    @Test
    void testConnection(){
        assertNotNull(connection);
    }

    @Test
    void testFindStudentCourse() throws SQLException {
        long studentId = 1;
        long courseId = 2;

        String insertStatement =
                "INSERT INTO student_course(student_id, course_id) " +
                "VALUES(" + studentId + ", " + courseId + ")";
        connection.createStatement().executeUpdate(insertStatement);

        StudentCourse studentCourse = studentCourseDAO.findStudentCourse(connection, studentId ,courseId);
        assertNotNull(studentCourse);
    }

    @Test
    void saveStudentCourse() throws SQLException {
        long studentId = 1;
        long courseId = 2;

        StudentCourse studentCourse = getStudentCourse(studentId, courseId);
        studentCourseDAO.saveStudentCourse(connection, studentCourse);

        StudentCourse result = studentCourseDAO.findStudentCourse(connection, studentId, courseId);
        assertNotNull(result);
    }

    @Test
    void testDeleteStudentCourse() throws SQLException {
        long studentId = 1;
        long courseId = 2;

        StudentCourse studentCourse = getStudentCourse(studentId, courseId);
        studentCourseDAO.saveStudentCourse(connection, studentCourse);

        studentCourseDAO.deleteStudentCourse(connection, studentId, courseId);

        StudentCourse result = studentCourseDAO.findStudentCourse(connection, studentId, courseId);
        assertNull(result);
    }

    @Test
    void testUpdateStudentCourse() throws SQLException {
        long studentId = 1;
        long courseId = 2;

        StudentCourse studentCourse = getStudentCourse(studentId, courseId);
        studentCourseDAO.saveStudentCourse(connection, studentCourse);

        int newScore = 90;
        studentCourse.setScore(newScore);
        studentCourseDAO.updateStudentCourse(connection, studentCourse);

        StudentCourse result = studentCourseDAO.findStudentCourse(connection, studentId, courseId);
        assertEquals(newScore, result.getScore());
    }

    @Test
    void testFindByStudentId() throws SQLException {
        long studentId = 3;
        int numberOfStudentCourses = 2;

        for(int i = 0; i < numberOfStudentCourses; i++) {
            studentCourseDAO.saveStudentCourse(connection, getStudentCourse(studentId, i));
        }

        StudentCourse sc = getStudentCourse(7, 4);
        studentCourseDAO.saveStudentCourse(connection, sc);

        List<StudentCourse> studentCourseList = studentCourseDAO.findByStudentId(connection, studentId);
        assertEquals(numberOfStudentCourses, studentCourseList.size());
        assertFalse(studentCourseList.contains(sc));
    }

    @Test
    void testFindByCourseId() throws SQLException {
        long courseId = 4;
        int numberOfStudentCourses = 3;

        for(int i = 0; i < numberOfStudentCourses; i++) {
            studentCourseDAO.saveStudentCourse(connection, getStudentCourse(i, courseId));
        }

        StudentCourse sc = getStudentCourse(7, 1);
        studentCourseDAO.saveStudentCourse(connection, sc);

        List<StudentCourse> studentCourseList = studentCourseDAO.findByCourseId(connection, courseId);
        assertEquals(numberOfStudentCourses, studentCourseList.size());
        assertFalse(studentCourseList.contains(sc));
    }

    private StudentCourse getStudentCourse(long studentId, long courseId) {
        StudentCourse studentCourse = new StudentCourse();
        studentCourse.setStudentId(studentId);
        studentCourse.setCourseId(courseId);
        studentCourse.setRegistrationDate(LocalDateTime.now());
        return studentCourse;
    }
}