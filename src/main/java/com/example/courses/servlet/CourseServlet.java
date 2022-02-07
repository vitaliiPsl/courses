package com.example.courses.servlet;

import com.example.courses.DTO.CourseDTO;
import com.example.courses.persistence.entity.Course;
import com.example.courses.persistence.entity.Role;
import com.example.courses.persistence.entity.User;
import com.example.courses.persistence.postgres.StudentCourse;
import com.example.courses.service.CourseDTOService;
import com.example.courses.service.CourseService;
import com.example.courses.service.StudentCourseService;
import com.example.courses.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/course")
public class CourseServlet extends HttpServlet {
    private static final String COURSE_JSP = "/WEB-INF/templates/course.jsp";
    private static final CourseService courseService = new CourseService();
    private static final CourseDTOService courseDTOService = new CourseDTOService();
    private static final StudentCourseService studentCourseService = new StudentCourseService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");

        CourseDTO courseDTO = null;
        StudentCourse studentCourse = null;
        Map<Long, Integer> studentsScores = null;

        try {
            long courseId = Long.parseLong(request.getParameter("course_id"));

            Course course = courseService.getCourseById(courseId);
            if (course == null) {
                request.getRequestDispatcher("/WEB-INF/templates/error_pages/not_found.jsp").forward(request, response);
            }
            courseDTO = courseDTOService.getCourseDTO(course);

            if (user != null) {
                if (user.getRole().equals(Role.STUDENT)) {
                    studentCourse = studentCourseService.getStudentCourse(user.getId(), courseId);
                } else if (user.getRole().equals(Role.TEACHER)) {
                    studentsScores = getStudentsScores(courseId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        request.setAttribute("course", courseDTO);
        request.setAttribute("student_course", studentCourse);
        request.setAttribute("scores", studentsScores);

        request.getRequestDispatcher(COURSE_JSP).forward(request, response);
    }

    private Map<Long, Integer> getStudentsScores(long courseId) throws SQLException {
        List<StudentCourse> studentCourseList = studentCourseService.getStudentsByCourseId(courseId);
        return studentCourseList.stream().collect(Collectors.toMap(
                StudentCourse::getStudentId,
                StudentCourse::getScore
        ));
    }
}
