package com.example.courses.servlet.admin;

import com.example.courses.persistence.entity.*;
import com.example.courses.service.CourseService;
import com.example.courses.service.LanguageService;
import com.example.courses.service.SubjectService;
import com.example.courses.service.UserService;
import com.example.courses.servlet.Constants;
import com.example.courses.utils.CourseUtils;

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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String lang = (String) session.getAttribute("lang");

        List<User> teachers = null;
        List<Language> languages = null;
        List<Subject> subjects = null;

        try {
            teachers = userService.getUsersByRole(Role.TEACHER);
            languages = languageService.getAllLanguages();

            Language locale = languageService.getLanguageByCode(lang);
            subjects = subjectService.getAll(locale.getId());
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }

        request.setAttribute("teachers", teachers);
        request.setAttribute("languages", languages);
        request.setAttribute("subjects", subjects);
        request.getRequestDispatcher(Constants.TEMPLATES_CONSTANTS.NEW_COURSE_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        try {
            Course course = CourseUtils.buildCourse(request);
            courseService.saveNewCourse(course);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/courses");
    }
}
