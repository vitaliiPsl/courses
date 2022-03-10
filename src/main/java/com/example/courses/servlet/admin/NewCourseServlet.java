package com.example.courses.servlet.admin;

import com.example.courses.exception.ServerErrorException;
import com.example.courses.persistence.entity.*;
import com.example.courses.service.CourseService;
import com.example.courses.service.LanguageService;
import com.example.courses.service.SubjectService;
import com.example.courses.service.UserService;
import com.example.courses.servlet.Constants;
import com.example.courses.utils.CourseUtils;
import com.example.courses.utils.ImageUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin/course/new")
@MultipartConfig(
        fileSizeThreshold=1024*1024*2,
        maxFileSize=1024*1024*10,
        maxRequestSize=1024*1024*50
)
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
            Language locale = languageService.getLanguageByCode(lang);
            languages = languageService.getAllLanguages(locale.getId());
            subjects = subjectService.getAll(locale.getId());
        } catch (SQLException e){
            logger.error("SQLException while retrieving data for new course page", e);
            throw new ServerErrorException();
        }

        request.setAttribute("teachers", teachers);
        request.setAttribute("languages", languages);
        request.setAttribute("subjects", subjects);

        request.getRequestDispatcher(Constants.TEMPLATES_CONSTANTS.NEW_COURSE_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.trace("New course: post");

        Course course = null;
        try {
            String imageName = ImageUtils.saveCourseImage(request);
            if (imageName == null) {
                imageName = ImageUtils.DEFAULT_IMAGE;
            }

            course = CourseUtils.buildCourse(request);
            course.setImageName(imageName);

            logger.info("Saving new course: " + course);
            courseService.saveNewCourse(course);
        } catch (SQLException e) {
            logger.error("SQLException while saving edited course", e);
            throw new ServerErrorException();
        } catch (IllegalArgumentException e){
            logger.error("Invalid properties: " + course);
            request.setAttribute("error", e.getMessage());
            this.doGet(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/courses");
    }
}
