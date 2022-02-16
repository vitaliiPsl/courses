package com.example.courses.servlet;

import com.example.courses.DTO.CourseDTO;
import com.example.courses.persistence.entity.Course;
import com.example.courses.persistence.entity.Role;
import com.example.courses.persistence.entity.User;
import com.example.courses.persistence.entity.StudentCourse;
import com.example.courses.service.CourseDTOService;
import com.example.courses.service.CourseService;
import com.example.courses.service.StudentCourseService;
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
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/course")
public class CourseServlet extends HttpServlet {
    private static final CourseService courseService = new CourseService();
    private static final CourseDTOService courseDTOService = new CourseDTOService();
    private static final StudentCourseService studentCourseService = new StudentCourseService();

    private static final Logger logger = LogManager.getLogger(CourseServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String lang = (String) session.getAttribute("lang");

        CourseDTO courseDTO = null;
        StudentCourse studentCourse = null;
        Map<Long, Integer> studentsScores = null;

        String courseId = request.getParameter("course_id");
        try {
            long id = Long.parseLong(courseId);
            logger.trace("Get course by id: " + id);

            Course course = courseService.getCourseById(id);

            // Redirect to 'not found' if course is null
            if (course == null) {
                logger.trace("Course not found");
                response.sendRedirect(request.getContextPath() + "/error_handler?type=404");
                return;
            }

            courseDTO = courseDTOService.getCourseDTO(course, lang);

            if (user != null) {
                if (user.getRole().equals(Role.STUDENT)) {
                    studentCourse = studentCourseService.getStudentCourse(user.getId(), id);
                } else if (user.getRole().equals(Role.TEACHER)) {
                    studentsScores = getStudentsScores(id);
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException: " + e.getMessage(), e);
            response.sendRedirect(request.getContextPath() + "/error_handler?type=500");
            return;
        } catch (NumberFormatException e) {
            logger.error("Invalid course id: " + courseDTO, e);
            response.sendRedirect(request.getContextPath() + "/error_handler?type=404");
            return;
        }

        request.setAttribute("course", courseDTO);
        request.setAttribute("student_course", studentCourse);
        request.setAttribute("scores", studentsScores);

        request.getRequestDispatcher(Constants.TEMPLATES_CONSTANTS.COURSE_JSP).forward(request, response);
    }

    private Map<Long, Integer> getStudentsScores(long courseId) throws SQLException {
        List<StudentCourse> studentCourseList = studentCourseService.getStudentsByCourseId(courseId);
        return studentCourseList.stream().collect(Collectors.toMap(
                StudentCourse::getStudentId,
                StudentCourse::getScore
        ));
    }
}
