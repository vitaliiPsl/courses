package com.example.courses.servlet;

import com.example.courses.persistence.entity.Course;
import com.example.courses.persistence.entity.Role;
import com.example.courses.persistence.entity.StudentCourse;
import com.example.courses.persistence.entity.User;
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
import java.util.stream.Collectors;

@WebServlet("/user")
public class UserProfileServlet extends HttpServlet {
    private final UserService userService = new UserService();
    private final StudentCourseService studentCourseService = new StudentCourseService();
    private final CourseService courseService = new CourseService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = null;
        String userId = request.getParameter("user_id");
        List<Course> courseList = null;

        try{
            long id = Long.parseLong(userId);
            user = userService.getUserById(id);

            if(user.getRole().equals(Role.STUDENT)){
                List<StudentCourse> studentCourseList = studentCourseService.getCoursesByStudentId(id);
                List<Long> coursesIds = studentCourseList.stream().map(StudentCourse::getCourseId).collect(Collectors.toList());
                courseList = courseService.getCourses(coursesIds);
            } else if(user.getRole().equals(Role.TEACHER)){
                courseList = courseService.getCoursesByTeacherId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        request.setAttribute("user", user);
        request.setAttribute("courses", courseList);

        request.getRequestDispatcher(Constants.TEMPLATES_CONSTANTS.USER_PROFILE).forward(request, response);
    }
}
