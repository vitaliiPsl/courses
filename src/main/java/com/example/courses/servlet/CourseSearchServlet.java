package com.example.courses.servlet;

import com.example.courses.DTO.CourseDTO;
import com.example.courses.persistence.entity.Course;
import com.example.courses.persistence.entity.Role;
import com.example.courses.persistence.entity.User;
import com.example.courses.service.CourseDTOService;
import com.example.courses.service.CourseService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

@WebServlet("/courses/search")
public class CourseSearchServlet extends HttpServlet {
    private static final CourseService courseService = new CourseService();
    private static final CourseDTOService courseDTOService = new CourseDTOService();

    private final static Logger logger = LogManager.getLogger(CourseSearchServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.trace("Search by query");

        HttpSession session = request.getSession();
        String lang = (String) session.getAttribute("lang");

        String query = request.getParameter("query");
        logger.debug("Search query: " + query);

        User user = (User) request.getSession().getAttribute("user");
        List<CourseDTO> courseDTOList = null;

        if(query != null && !query.trim().isEmpty()){
            List<Course> courseList = null;
            try{
                if(user != null && user.getRole().equals(Role.ADMIN)) {
                    courseList = courseService.getCoursesBySearchQuery(query);
                } else {
                    courseList = courseService.getAvailableCoursesBySearchQuery(query);
                }
                courseDTOList = courseDTOService.getCourseDTOList(courseList,lang);
                writeResponse(response, courseDTOList);
            } catch (SQLException | IOException e) {
                logger.error("Exception while searching for courses by query: " + query + "." + e.getMessage(), e);
            }
        }
    }

    private void writeResponse(HttpServletResponse response, List<CourseDTO> courseList) throws IOException {
        logger.info("Initializing Jackson");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        String json = objectMapper.writeValueAsString(courseList);

        response.setContentType("application/json");
        response.getWriter().write(json);
    }
}
