package com.example.courses.servlet;

import com.example.courses.persistence.entity.Course;
import com.example.courses.persistence.entity.Role;
import com.example.courses.persistence.entity.User;
import com.example.courses.service.CourseService;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/courses/search")
public class CourseSearchServlet extends HttpServlet {
    private static final CourseService courseService = new CourseService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        User user = (User) request.getSession().getAttribute("user");

        if(query != null && !query.trim().isEmpty()){
            List<Course> courseList = null;
            try{
                if(user != null && user.getRole().equals(Role.ADMIN)) {
                    courseList = courseService.getCoursesBySearchQuery(query);
                } else {
                    courseList = courseService.getAvailableCoursesBySearchQuery(query);
                }

                writeResponse(response, courseList);
            } catch (SQLException | IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void writeResponse(HttpServletResponse response, List<Course> courseList) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        String json = objectMapper.writeValueAsString(courseList);

        response.setContentType("application/json");
        response.getWriter().write(json);
    }
}