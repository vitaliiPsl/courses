package com.example.courses.servlet.admin;

import com.example.courses.persistence.entity.*;
import com.example.courses.service.CourseService;
import com.example.courses.service.LanguageService;
import com.example.courses.service.SubjectService;
import com.example.courses.service.UserService;
import com.example.courses.servlet.Constants;
import com.example.courses.utils.CourseUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin/course/new")
public class NewCourseServlet extends HttpServlet {
    private static final UserService userService = new UserService();
    private static final LanguageService languageService = new LanguageService();
    private static final SubjectService subjectService = new SubjectService();
    private static final CourseService courseService = new CourseService();

    private static final Logger logger = LogManager.getLogger(NewCourseServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.trace("New course: get");

        HttpSession session = request.getSession();
        String lang = (String) session.getAttribute("lang");

        List<User> teachers;
        List<Language> languages;
        List<Subject> subjects;

        try {
            teachers = userService.getUsersByRole(Role.TEACHER);
            languages = languageService.getAllLanguages();

            Language locale = languageService.getLanguageByCode(lang);
            subjects = subjectService.getAll(locale.getId());
        } catch (SQLException e){
            logger.error("SQLException while retrieving data for new course page", e);
            response.sendRedirect(request.getContextPath() + "/error_handler?type=500");
            return;
        }

        request.setAttribute("teachers", teachers);
        request.setAttribute("languages", languages);
        request.setAttribute("subjects", subjects);
        request.getRequestDispatcher(Constants.TEMPLATES_CONSTANTS.NEW_COURSE_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.trace("New course: post");

        Course course = null;
        try {
            course = CourseUtils.buildCourse(request);
            logger.info("Saving new course: " + course);
            courseService.saveNewCourse(course);
        } catch (SQLException e) {
            logger.error("SQLException while saving edited course", e);
            response.sendRedirect(request.getContextPath() + "/error_handler?type=500");
            return;
        } catch (IllegalArgumentException e){
            logger.error("Invalid properties: " + course);
            response.sendRedirect(request.getContextPath() + "/error_handler?type=500");
            return;
        }

        response.sendRedirect(request.getContextPath() + "/courses");
    }
}
