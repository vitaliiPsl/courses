package com.example.courses.servlet;

import com.example.courses.DTO.CourseDTO;
import com.example.courses.persistence.entity.Course;
import com.example.courses.persistence.entity.Role;
import com.example.courses.persistence.entity.User;
import com.example.courses.service.CourseDTOService;
import com.example.courses.service.CourseFilterService;
import com.example.courses.service.CourseService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@WebServlet("/courses")
public class CourseListServlet extends HttpServlet {
    private static final CourseService courseService = new CourseService();
    private static final CourseDTOService courseDTOService = new CourseDTOService();
    private static final CourseFilterService courseFilterService = new CourseFilterService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        List<CourseDTO> courseDTOList = null;
        Map<String, List<String>> availableFilters = null;
        Map<String, List<String>> requestFilters = null;

        try {
            List<Course> courseList;
            String query = request.getParameter("query");
            if (query != null && !query.trim().isEmpty()) {
                courseList = courseService.getCoursesBySearchQuery(query);
                request.setAttribute("query", query);
            } else {
                if (user == null || user.getRole().equals(Role.STUDENT)) {
                    courseList = courseService.getAvailable();
                } else {
                    courseList = courseService.getAll();
                }
            }
            courseDTOList = courseDTOService.getCourseDTOList(courseList);

            availableFilters = courseFilterService.getAvailableFilters();
            requestFilters = courseFilterService.getRequestFilters(request);
            courseFilterService.applyFilters(courseDTOList, requestFilters);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        request.setAttribute("courses", courseDTOList);
        request.setAttribute("filters", availableFilters);
        request.setAttribute("applied_filters", requestFilters);

        request.getRequestDispatcher("/WEB-INF/templates/course_list.jsp").forward(request, response);
    }
}
