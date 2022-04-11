package com.example.courses.servlet.course;

import com.example.courses.dto.CourseDTO;
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

/**
 * This servlet handles courses search request and writes response in JSON format
 */
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
        User user = (User) request.getSession().getAttribute("user");

        String query = request.getParameter("query");
        logger.debug("Search query: " + query);

        List<CourseDTO> courseDTOList = getCourses(lang, query, user);
        writeResponse(response, courseDTOList);
    }

    private List<CourseDTO> getCourses(String lang, String query, User user) {
        List<CourseDTO> courseDTOList = null;
        
        if(query != null && !query.trim().isEmpty()){
            try{
                List<Course> courseList = getCoursesBasedOnUserRole(query, user);
                courseDTOList = courseDTOService.getCourseDTOList(courseList, lang);
                logger.debug("List of courses that match search query: " + courseDTOList);
            } catch (SQLException e) {
                logger.error("Exception while searching for courses by query: " + query + "." + e.getMessage(), e);
            }
        }
        
        return courseDTOList;
    }

    private List<Course> getCoursesBasedOnUserRole(String query, User user) throws SQLException {
        List<Course> courseList;
        if(user != null && user.getRole().equals(Role.ADMIN)) {
            courseList = courseService.getBySearchQuery(query);
        } else {
            courseList = courseService.getAvailableBySearchQuery(query);
        }
        return courseList;
    }

    private void writeResponse(HttpServletResponse response, List<CourseDTO> courseList) throws IOException {
        logger.info("Initializing Jackson");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        String json = objectMapper.writeValueAsString(courseList);
        logger.debug("List of courses in json: " + json);

        response.setContentType("application/json");
        response.getWriter().write(json);
    }
}
