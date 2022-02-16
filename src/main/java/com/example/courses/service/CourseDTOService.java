package com.example.courses.service;

import com.example.courses.DTO.CourseDTO;
import com.example.courses.persistence.*;
import com.example.courses.persistence.entity.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseDTOService {
    private final DAOFactory daoFactory;
    private final LanguageDAO languageDAO;
    private final UserDAO userDAO;
    private final SubjectDAO subjectDAO;
    private final StudentCourseDAO studentCourseDAO;

    private static final Logger logger = LogManager.getLogger(CourseDTOService.class.getName());

    public CourseDTOService() {
        daoFactory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.POSTGRES);
        languageDAO = daoFactory.getLanguageDao();
        userDAO = daoFactory.getUserDao();
        subjectDAO = daoFactory.getSubjectDao();
        studentCourseDAO = daoFactory.getStudentCourseDao();
    }

    public CourseDTO getCourseDTO(Course course, String languageCode) throws SQLException {
        logger.trace("Get course");
        logger.debug("Get course. Localization language: " + languageCode);

        CourseDTO courseDTO;
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            courseDTO = makeCourseDTO(connection, course, languageCode);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            logger.error("SQLException while getting courseDTO", e);
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return courseDTO;
    }

    public List<CourseDTO> getCourseDTOList(List<Course> courseList, String languageCode) throws SQLException {
        logger.trace("Get courseDTO list based on course list: " + courseList);
        logger.debug("Get course. Language of localization: " + languageCode);

        List<CourseDTO> courseDTOList = new ArrayList<>();
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            for(Course course : courseList) {
                courseDTOList.add(makeCourseDTO(connection, course, languageCode));
            }
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            logger.error("SQLException while getting courseDTO list", e);
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return courseDTOList;
    }

    private CourseDTO makeCourseDTO(Connection connection, Course course, String languageCode) throws SQLException {
        logger.trace("Make courseDTO");
        logger.debug("Make courseDTO based on: " + course + ". Localization language: " + languageCode);

        CourseDTO courseDTO = new CourseDTO();

        try {
            Language locale = languageDAO.findLanguageByCode(connection, languageCode);

            Language courseLanguage = languageDAO.findLanguageById(connection, course.getLanguageId());
            Subject subject = subjectDAO.findSubject(connection, course.getSubjectId(), locale.getId());
            User teacher = userDAO.findUser(connection, course.getTeacherId());
            List<User> students = getStudents(connection, course);

            courseDTO.setCourse(course);
            courseDTO.setSubject(subject);
            courseDTO.setLanguage(courseLanguage);
            courseDTO.setTeacher(teacher);
            courseDTO.setStudents(students);
        } catch (SQLException e){
            logger.error("SQLException while building courseDTO", e);
            throw e;
        }

        return courseDTO;
    }

    private List<User> getStudents(Connection connection, Course course) throws SQLException {
        logger.trace("Get list of students that takes: " + course);

        List<User> students = new ArrayList<>();
        try{
            List<StudentCourse> studentCourseList = studentCourseDAO.findByCourseId(connection, course.getId());
            for(StudentCourse studentCourse: studentCourseList){
                students.add(userDAO.findUser(connection, studentCourse.getStudentId()));
            }
        } catch (SQLException e){
            logger.error("SQLException while retrieving students", e);
            throw e;
        }

        return students;
    }
}
