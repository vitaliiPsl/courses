package com.example.courses.servlet;

import com.example.courses.exception.ForbiddenException;
import com.example.courses.persistence.entity.Course;
import com.example.courses.persistence.entity.Role;
import com.example.courses.persistence.entity.User;
import com.example.courses.persistence.postgres.StudentCourse;
import com.example.courses.service.CourseService;
import com.example.courses.service.StudentCourseService;

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

@WebServlet("/user_courses")
public class UserCoursesServlet extends HttpServlet {
    private static final StudentCourseService studentCourseService = new StudentCourseService();
    private static final CourseService courseService = new CourseService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        try {
            if (user.getRole().equals(Role.STUDENT)) {
                List<StudentCourse> studentCourseList = studentCourseService.getCoursesByStudentId(user.getId());

                Map<Long, Integer> scores = getScores(studentCourseList);
                List<Course> courseList = getStudentCourses(studentCourseList);

                request.setAttribute("courses", courseList);
                request.setAttribute("scores", scores);
            } else if (user.getRole().equals(Role.TEACHER)) {
                List<Course> teacherCourses = courseService.getCoursesByTeacherId(user.getId());
                request.setAttribute("courses", teacherCourses);
            } else {
                throw new ForbiddenException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        request.getRequestDispatcher(Constants.TEMPLATES_CONSTANTS.USER_COURSES_JSP).forward(request, response);
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
