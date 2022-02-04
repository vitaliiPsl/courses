package com.example.courses.servlet;

import com.example.courses.DTO.CourseDTO;
import com.example.courses.persistence.entity.Course;
import com.example.courses.persistence.entity.Role;
import com.example.courses.persistence.entity.User;
import com.example.courses.service.CourseDTOService;
import com.example.courses.service.CourseService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/courses")
public class CourseListServlet extends HttpServlet {
    CourseService courseService = new CourseService();
    CourseDTOService courseDTOService = new CourseDTOService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        List<CourseDTO> courseDTOList = null;

        try {
            List<Course> courseList;
            if (user == null || user.getRole().equals(Role.STUDENT)) {
                courseList = courseService.getAvailable();
            } else {
                courseList = courseService.getAll();
            }
            courseDTOList = courseDTOService.getCourseDTOList(courseList);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        request.setAttribute("courses", courseDTOList);

        request.getRequestDispatcher("/WEB-INF/templates/course_list.jsp").forward(request, response);
    }
}
