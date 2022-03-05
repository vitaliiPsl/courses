package com.example.courses.persistence.postgres;

import com.example.courses.persistence.DAOFactory;
import com.example.courses.persistence.StudentCourseDAO;
import com.example.courses.persistence.entity.StudentCourse;
import com.example.courses.utils.DAOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresStudentCourseDAO implements StudentCourseDAO {
    private static final Logger logger = LogManager.getLogger(PostgresStudentCourseDAO.class.getName());

    @Override
    public long saveStudentCourse(Connection connection, StudentCourse studentCourse) throws SQLException {
        logger.trace("Save record: " + studentCourse);

        long generatedId;
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(
                    StudentCourseDAOConstants.INSERT_STUDENT_COURSE,
                    Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, studentCourse.getStudentId());
            statement.setLong(2, studentCourse.getCourseId());
            statement.setTimestamp(3, Timestamp.valueOf(studentCourse.getRegistrationDate()));
            statement.setInt(4, studentCourse.getScore());
            statement.executeUpdate();

            generatedId = DAOUtils.getGeneratedId(statement);
        } finally {
            DAOFactory.closeResource(statement);
        }

        return generatedId;
    }

    @Override
    public void deleteStudentCourse(Connection connection, long studentId, long courseId) throws SQLException {
        logger.trace("Delete student course. Student id: " + studentId + ". Course id: " + courseId);
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(StudentCourseDAOConstants.DELETE_STUDENT_COURSE);
            statement.setLong(1, studentId);
            statement.setLong(2, courseId);
            statement.executeUpdate();
        } finally {
            DAOFactory.closeResource(statement);
        }
    }

    @Override
    public void updateStudentCourse(Connection connection, StudentCourse studentCourse) throws SQLException {
        logger.trace("Update student course: " + studentCourse);
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(StudentCourseDAOConstants.UPDATE_STUDENT_SCORE);

            statement.setInt(1, studentCourse.getScore());
            statement.setLong(2, studentCourse.getStudentId());
            statement.setLong(3, studentCourse.getCourseId());

            statement.executeUpdate();
        } finally {
            DAOFactory.closeResource(statement);
        }
    }

    @Override
    public StudentCourse findStudentCourse(Connection connection, long studentId, long courseId) throws SQLException {
        logger.trace("Find studentCourse. Student id: " + studentId + ". Course id: " + courseId);

        StudentCourse studentCourse = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.prepareStatement(StudentCourseDAOConstants.SELECT_STUDENT_COURSE);
            statement.setLong(1, studentId);
            statement.setLong(2, courseId);

            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                studentCourse = parseStudentCourse(resultSet);
            }
        } finally {
            DAOFactory.closeResource(resultSet);
            DAOFactory.closeResource(statement);
        }

        return studentCourse;
    }

    @Override
    public List<StudentCourse> findByStudentId(Connection connection, long studentId) throws SQLException {
        logger.trace("Find studentCourse by student id: " + studentId);

        List<StudentCourse> studentCourseList = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.prepareStatement(StudentCourseDAOConstants.SELECT_STUDENT_COURSE_BY_STUDENT_ID);
            statement.setLong(1, studentId);

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                studentCourseList.add(parseStudentCourse(resultSet));
            }
        } finally {
            DAOFactory.closeResource(resultSet);
            DAOFactory.closeResource(statement);
        }

        return studentCourseList;
    }

    @Override
    public List<StudentCourse> findByCourseId(Connection connection, long courseId) throws SQLException {
        logger.trace("Find studentCourse by course id: " + courseId);

        List<StudentCourse> studentCourseList = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.prepareStatement(StudentCourseDAOConstants.SELECT_STUDENT_COURSE_BY_COURSE_ID);
            statement.setLong(1, courseId);

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                studentCourseList.add(parseStudentCourse(resultSet));
            }
        } finally {
            DAOFactory.closeResource(resultSet);
            DAOFactory.closeResource(statement);
        }

        return studentCourseList;
    }


    private StudentCourse parseStudentCourse(ResultSet resultSet) throws SQLException {
        logger.trace("Parse studentCourse");

        StudentCourse studentCourse = new StudentCourse();

        try {
            studentCourse.setStudentId(resultSet.getLong(StudentCourseDAOConstants.STUDENT_COURSE_STUDENT_ID));
            studentCourse.setCourseId(resultSet.getLong(StudentCourseDAOConstants.STUDENT_COURSE_COURSE_ID));
            studentCourse.setRegistrationDate(
                    resultSet.getTimestamp(StudentCourseDAOConstants.STUDENT_COURSE_REGISTRATION_DATE)
                            .toLocalDateTime()
            );
            studentCourse.setScore(resultSet.getInt(StudentCourseDAOConstants.STUDENT_COURSE_SCORE));
        } catch (SQLException e){
            logger.error("Error while parsing studentCourse record");
        }

        return studentCourse;
    }

    static class StudentCourseDAOConstants {
        static final String TABLE_STUDENT_COURSE = "student_course";
        static final String STUDENT_COURSE_ID = "id";
        static final String STUDENT_COURSE_STUDENT_ID = "student_id";
        static final String STUDENT_COURSE_COURSE_ID = "course_id";
        static final String STUDENT_COURSE_REGISTRATION_DATE = "registration_date";
        static final String STUDENT_COURSE_SCORE = "SCORE";

        static final String INSERT_STUDENT_COURSE =
                "INSERT INTO " +
                        TABLE_STUDENT_COURSE + "(" +
                        STUDENT_COURSE_STUDENT_ID + ", " +
                        STUDENT_COURSE_COURSE_ID + ", " +
                        STUDENT_COURSE_REGISTRATION_DATE + ", " +
                        STUDENT_COURSE_SCORE +
                        ") VALUES(?, ?, ?, ?);";

        static final String DELETE_STUDENT_COURSE =
                "DELETE FROM " +
                        TABLE_STUDENT_COURSE + " " +
                        "WHERE " + STUDENT_COURSE_STUDENT_ID + "= ? " +
                        "AND " + STUDENT_COURSE_COURSE_ID + "= ?;";

        static final String UPDATE_STUDENT_SCORE =
                "UPDATE " + TABLE_STUDENT_COURSE + " " +
                        "SET " + STUDENT_COURSE_SCORE + " = ? " +
                        "WHERE " + STUDENT_COURSE_STUDENT_ID + " = ? " +
                        "AND " + STUDENT_COURSE_COURSE_ID + " = ?;";

        static final String SELECT_STUDENT_COURSE_PROPERTIES =
                STUDENT_COURSE_ID + ", " +
                        STUDENT_COURSE_STUDENT_ID + ", " +
                        STUDENT_COURSE_COURSE_ID + ", " +
                        STUDENT_COURSE_REGISTRATION_DATE + ", " +
                        STUDENT_COURSE_SCORE;

        static final String SELECT_STUDENT_COURSE =
                "SELECT " +
                        SELECT_STUDENT_COURSE_PROPERTIES + " " +
                        "FROM " + TABLE_STUDENT_COURSE + " " +
                        "WHERE " + STUDENT_COURSE_STUDENT_ID + " = ? " +
                        "AND " + STUDENT_COURSE_COURSE_ID + " = ?;";

        static final String SELECT_STUDENT_COURSE_BY_STUDENT_ID =
                "SELECT " +
                        SELECT_STUDENT_COURSE_PROPERTIES + " " +
                        "FROM " + TABLE_STUDENT_COURSE + " " +
                        "WHERE " + STUDENT_COURSE_STUDENT_ID + " = ?;";

        static final String SELECT_STUDENT_COURSE_BY_COURSE_ID =
                "SELECT " +
                        SELECT_STUDENT_COURSE_PROPERTIES + " " +
                        "FROM " + TABLE_STUDENT_COURSE + " " +
                        "WHERE " + STUDENT_COURSE_COURSE_ID + " = ?;";

    }
}
