package com.example.courses.servlet.admin;

import com.example.courses.exception.NotFoundException;
import com.example.courses.exception.ServerErrorException;
import com.example.courses.persistence.entity.*;
import com.example.courses.service.CourseService;
import com.example.courses.service.LanguageService;
import com.example.courses.service.SubjectService;
import com.example.courses.service.UserService;
import com.example.courses.servlet.Constants;
import com.example.courses.utils.CourseUtils;
import com.example.courses.service.AwsImageService;
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

@WebServlet("/admin/course/edit")
@MultipartConfig(
        fileSizeThreshold=1024*1024*2,
        maxFileSize=1024*1024*10,
        maxRequestSize=1024*1024*50
)
public class EditCourseServlet extends HttpServlet {
    private final UserService userService = new UserService();
    private final LanguageService languageService = new LanguageService();
    private final SubjectService subjectService = new SubjectService();
    private final CourseService courseService = new CourseService();
    private final AwsImageService imageUtils = new AwsImageService();

    private static final Logger logger = LogManager.getLogger(EditCourseServlet.class.getName());

    public EditCourseServlet() throws IOException {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.trace("Edit course: post");

        HttpSession session = request.getSession();
        String lang = (String) session.getAttribute("lang");

        String courseId = request.getParameter("course_id");

        if (courseId == null) {
            logger.warn("Course id is null");
            throw new NotFoundException();
        }

        logger.debug("Course id: " + courseId);

        Course course;
        List<User> teachers;
        List<Language> languages;
        List<Subject> subjects;

        try {
            long id = Long.parseLong(courseId);
            course = courseService.getCourseById(id);
            teachers = userService.getUsersByRole(Role.TEACHER);
            Language locale = languageService.getLanguageByCode(lang);
            languages = languageService.getAllLanguages(locale.getId());
            subjects = subjectService.getAll(locale.getId());
        } catch (SQLException e) {
            logger.error("SQLException while getting course", e);
            throw new ServerErrorException();
        } catch (NumberFormatException e) {
            logger.error("Invalid course id: " + courseId, e);
            throw new NotFoundException();
        }

        request.setAttribute("course", course);
        request.setAttribute("teachers", teachers);
        request.setAttribute("languages", languages);
        request.setAttribute("subjects", subjects);

        request.getRequestDispatcher(Constants.TEMPLATES_CONSTANTS.EDIT_COURSE_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.trace("Edit course: post");
        String courseId = request.getParameter("course_id");

        if (courseId == null) {
            logger.warn("Course id is null");
            throw new NotFoundException();
        }

        logger.info("Edit course by id: " + courseId);
        try {
            long id = Long.parseLong(courseId);
            Course original = courseService.getCourseById(id);

            Course course = CourseUtils.buildCourse(request);
            course.setId(id);

            String imageName = imageUtils.saveCourseImage(request);
            if (imageName != null) {
                course.setImageName(imageName);
            } else {
                course.setImageName(original.getImageName());
            }

            courseService.updateCourse(course);
        } catch (SQLException e) {
            logger.error("SQLException while saving edited course", e);
            throw new ServerErrorException();
        } catch (NumberFormatException e) {
            logger.error("Invalid course id: " + courseId, e);
            throw new NotFoundException();
        }

        response.sendRedirect(request.getContextPath() + "/courses");
    }
}
