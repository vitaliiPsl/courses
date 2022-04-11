package com.example.courses.servlet.course;

import com.example.courses.dto.CourseDTO;
import com.example.courses.exception.NotFoundException;
import com.example.courses.exception.ServerErrorException;
import com.example.courses.persistence.entity.Course;
import com.example.courses.persistence.entity.Role;
import com.example.courses.persistence.entity.User;
import com.example.courses.persistence.entity.StudentCourse;
import com.example.courses.service.CourseDTOService;
import com.example.courses.service.CourseService;
import com.example.courses.service.StudentCourseService;
import com.example.courses.servlet.Constants;
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

/**
 * CourseServlet display information about particular course
 */
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

        String courseIdStr = request.getParameter("course_id");

        try {
            long courseId = Long.parseLong(courseIdStr);
            logger.trace("Get course by id: " + courseId);

            Course course = courseService.getCourseById(courseId);
            logger.debug("Retrieved course:" + course);

            // Throw not found if a course is null
            if (course == null) {
                logger.trace("Course not found");
                throw new NotFoundException();
            }

            courseDTO = courseDTOService.getCourseDTO(course, lang);
            request.setAttribute("course", courseDTO);

            if (user != null) {
                // if user is student than get info about student enrollment in this course
                if (user.getRole().equals(Role.STUDENT)) {
                    StudentCourse studentCourse = studentCourseService.getStudentCourse(user.getId(), courseId);
                    request.setAttribute("student_course", studentCourse);
                } else if (user.getRole().equals(Role.TEACHER)) {
                    Map<Long, Integer> studentsScores = getStudentsScores(courseId);
                    request.setAttribute("scores", studentsScores);
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException: " + e.getMessage(), e);
            throw new ServerErrorException();
        } catch (NumberFormatException e) {
            logger.error("Invalid course id: " + courseDTO, e);
            throw new NotFoundException();
        } catch (Exception e){
            logger.error("", e);
            throw new ServletException();
        }

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
