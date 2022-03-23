package com.example.courses.servlet.course;

import com.example.courses.dto.CourseDTO;
import com.example.courses.exception.ForbiddenException;
import com.example.courses.exception.ServerErrorException;
import com.example.courses.persistence.entity.*;
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

@WebServlet("/user/courses")
public class UserCoursesServlet extends HttpServlet {
    private static final StudentCourseService studentCourseService = new StudentCourseService();
    private static final CourseService courseService = new CourseService();
    private static final CourseDTOService courseDTOService = new CourseDTOService();

    private static final Logger logger = LogManager.getLogger(UserCoursesServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.trace("User's courses: get");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String lang = (String) session.getAttribute("lang");
        String requestStatus = request.getParameter("status");

        try {
            List<Course> courseList;
            List<CourseDTO> courseDTOList;

            if (user.getRole().equals(Role.STUDENT)) {
                List<StudentCourse> studentCourseList = studentCourseService.getCoursesByStudentId(user.getId());

                Map<Long, Integer> scores = getScores(studentCourseList);
                courseList = getStudentCourses(studentCourseList);

                request.setAttribute("scores", scores);
            } else if (user.getRole().equals(Role.TEACHER)) {
                courseList = courseService.getByTeacherId(user.getId());
            } else {
                throw new ForbiddenException();
            }

            filterByStatus(requestStatus, courseList);
            request.setAttribute("status", requestStatus);

            courseDTOList = courseDTOService.getCourseDTOList(courseList, lang);
            request.setAttribute("courses", courseDTOList);
        } catch (SQLException e) {
            logger.error("SQLException: " + e.getMessage(), e);
            throw new ServerErrorException();
        }

        request.getRequestDispatcher(Constants.TEMPLATES_CONSTANTS.USER_COURSES_JSP).forward(request, response);
    }

    private void filterByStatus(String requestStatus, List<Course> courseList) {
        if (requestStatus == null) {
            return;
        }

        CourseStatus courseStatus = CourseStatus.valueOf(requestStatus);
        courseList.removeIf(course -> !course.getCourseStatus().equals(courseStatus));
    }

    private Map<Long, Integer> getScores(List<StudentCourse> studentCourseList) {
        return studentCourseList.stream().collect(Collectors.toMap(
                StudentCourse::getCourseId,
                StudentCourse::getScore
        ));
    }

    private List<Course> getStudentCourses(List<StudentCourse> studentCourseList) throws SQLException {
        List<Long> coursesIds = studentCourseList.stream()
                .map(StudentCourse::getCourseId)
                .collect(Collectors.toList());

        return courseService.getCourses(coursesIds);
    }
}
