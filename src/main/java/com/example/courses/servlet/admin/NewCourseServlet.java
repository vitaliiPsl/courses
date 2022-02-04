package com.example.courses.servlet.admin;

import com.example.courses.persistence.entity.Course;
import com.example.courses.persistence.entity.Language;
import com.example.courses.persistence.entity.Role;
import com.example.courses.persistence.entity.User;
import com.example.courses.service.CourseService;
import com.example.courses.service.LanguageService;
import com.example.courses.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/admin/course/new")
public class NewCourseServlet extends HttpServlet {
    private static final UserService userService = new UserService();
    private static final LanguageService languageService = new LanguageService();
    private static final CourseService courseService = new CourseService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<User> teachers = null;
        List<Language> languages = null;

        try {
            teachers = userService.getUsersByRole(Role.TEACHER);
            languages = languageService.getAllLanguages();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }

        request.setAttribute("teachers", teachers);
        request.setAttribute("languages", languages);
        request.getRequestDispatcher("/WEB-INF/templates/admin/new_course.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Course course = buildCourse(request);

        try {
            courseService.saveNewCourse(course);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/courses");
    }

    private Course buildCourse(HttpServletRequest request) {
        String name = request.getParameter("course_title");
        String subject = request.getParameter("course_subject");
        String description = request.getParameter("course_description");
        long teacherId = Long.parseLong(request.getParameter("teacher_id"));
        long languageId = Long.parseLong(request.getParameter("language_id"));
        int maxScore = Integer.parseInt(request.getParameter("max_score"));
        LocalDateTime startDate = LocalDateTime.parse(request.getParameter("start_date"));
        LocalDateTime endDate = LocalDateTime.parse(request.getParameter("end_date"));

        Course course = new Course();
        course.setTitle(name);
        course.setSubject(subject);
        course.setDescription(description);
        course.setMaxScore(maxScore);
        course.setStartDate(startDate);
        course.setEndDate(endDate);
        course.setTeacherId(teacherId);
        course.setLanguageId(languageId);

        return course;
    }


}
