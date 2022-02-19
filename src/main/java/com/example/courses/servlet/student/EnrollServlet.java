package com.example.courses.servlet.student;

import com.example.courses.exception.NotFoundException;
import com.example.courses.exception.ServerErrorException;
import com.example.courses.persistence.entity.User;
import com.example.courses.service.StudentCourseService;
import com.example.courses.servlet.auth.LogIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.rmi.ServerError;
import java.sql.SQLException;


@WebServlet(urlPatterns = {"/enroll"})
public class EnrollServlet extends HttpServlet {
    private static final StudentCourseService studentCourseService = new StudentCourseService();

    private final static Logger logger = LogManager.getLogger(EnrollServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.trace("Enroll: post");
        String courseId = request.getParameter("course_id");

        if(courseId == null) {
            logger.warn("Course id is null");
            throw new NotFoundException();
        }

        User user = (User) request.getSession().getAttribute("user");

        try {
            long id = Long.parseLong(courseId);
            logger.info("Register student" + user.getId() + " for a course: " + id);
            studentCourseService.registerStudentForCourse(user.getId(), id);
        } catch (SQLException e) {
            logger.error("SQLException during registration of student: " + user.getId() + " for course: " + courseId, e);
            throw new ServerErrorException();
        } catch (NumberFormatException e) {
            logger.error("Invalid course id: " + courseId, e);
            throw new NotFoundException();
        }

        response.sendRedirect(request.getContextPath() + "/course?course_id=" + courseId);
    }
}
