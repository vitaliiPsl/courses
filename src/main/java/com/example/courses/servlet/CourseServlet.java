package com.example.courses.servlet;

import com.example.courses.persistence.entity.Course;
import com.example.courses.persistence.entity.Role;
import com.example.courses.persistence.entity.User;
import com.example.courses.persistence.postgres.StudentCourse;
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
    private static final UserService userService = new UserService();
    private static final StudentCourseService studentCourseService = new StudentCourseService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            long courseId = Long.parseLong(request.getParameter("course_id"));
            Course course = courseService.getCourseById(courseId);
            request.setAttribute("course", course);

            User user = (User) request.getSession().getAttribute("user");

            if (user != null) {
                if (user.getRole().equals(Role.STUDENT)) {
                    getStudentEnrollmentInfo(request, courseId, user);
                } else if (user.getRole().equals(Role.TEACHER)) {
                    getTeacherCourse(request, courseId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        request.getRequestDispatcher(COURSE_JSP).forward(request, response);
    }

    private void getTeacherCourse(HttpServletRequest request, long courseId) throws SQLException {
        List<StudentCourse> studentCourseList = studentCourseService.getStudentsByCourseId(courseId);
        List<Long> studentIds = studentCourseList.stream().map(StudentCourse::getStudentId).collect(Collectors.toList());
        Map<Long, Integer> studentsScores = studentCourseList.stream().collect(Collectors.toMap(StudentCourse::getStudentId, StudentCourse::getScore));
        List<User> studentList = userService.getUsers(studentIds);

        request.setAttribute("students", studentList);
        request.setAttribute("scores", studentsScores);
    }

    private void getStudentEnrollmentInfo(HttpServletRequest request, long courseId, User user) throws SQLException {
        StudentCourse studentCourse = studentCourseService.getStudentCourse(user.getId(), courseId);
        request.setAttribute("enrolled", studentCourse != null);
    }
}
