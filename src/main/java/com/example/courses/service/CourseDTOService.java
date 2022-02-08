package com.example.courses.service;

import com.example.courses.DTO.CourseDTO;
import com.example.courses.persistence.*;
import com.example.courses.persistence.entity.Course;
import com.example.courses.persistence.entity.Language;
import com.example.courses.persistence.entity.User;
import com.example.courses.persistence.entity.StudentCourse;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseDTOService {
    private DAOFactory daoFactory;
    private LanguageDAO languageDAO;
    private UserDAO userDAO;
    private StudentCourseDAO studentCourseDAO;

    public CourseDTOService() {
        daoFactory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.POSTGRES);
        languageDAO = daoFactory.getLanguageDao();
        userDAO = daoFactory.getUserDao();
        studentCourseDAO = daoFactory.getStudentCourseDao();
    }

    public CourseDTO getCourseDTO(Course course) throws SQLException {
        CourseDTO courseDTO;
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            courseDTO = makeCourseDTO(connection, course);
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            System.out.println(e.getMessage());
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return courseDTO;
    }

    public List<CourseDTO> getCourseDTOList(List<Course> courseList) throws SQLException {
        List<CourseDTO> courseDTOList = new ArrayList<>();
        Connection connection = null;

        try{
            connection = daoFactory.getConnection();
            for(Course course : courseList) {
                courseDTOList.add(makeCourseDTO(connection, course));
            }
            connection.commit();
        } catch (SQLException e) {
            DAOFactory.rollback(connection);
            System.out.println(e.getMessage());
            throw e;
        } finally {
            DAOFactory.closeResource(connection);
        }

        return courseDTOList;
    }

    private CourseDTO makeCourseDTO(Connection connection, Course course) throws SQLException {
        Language language = languageDAO.findLanguageById(connection, course.getLanguageId());
        User teacher = userDAO.findUser(connection, course.getTeacherId());

        List<User> students = getStudents(connection, course);

        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setCourse(course);
        courseDTO.setLanguage(language);
        courseDTO.setTeacher(teacher);
        courseDTO.setStudents(students);

        return courseDTO;
    }

    private List<User> getStudents(Connection connection, Course course) throws SQLException {
        List<User> students = new ArrayList<>();
        List<StudentCourse> studentCourseList = studentCourseDAO.findByCourseId(connection, course.getId());
        for(StudentCourse studentCourse: studentCourseList){
            students.add(userDAO.findUser(connection, studentCourse.getStudentId()));
        }
        return students;
    }
}
