package com.example.courses.servlet.course;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

/**
 * This servlet handles filter selection requests
 */
@WebServlet("/courses/filter")
public class CourseFilterServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(CourseFilterServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get filters stored in session
        HttpSession session = request.getSession();
        Map<String, List<Long>> sessionFilters = (Map<String, List<Long>>) session.getAttribute("filters");
        logger.debug("Filters stored in session: " + sessionFilters);

        // Get applied / removed filterKey and option and redirect to course list
        String subjectOption = request.getParameter("subject");
        String teacherOption = request.getParameter("teacher");
        if(subjectOption != null){
            long subject = Long.parseLong(subjectOption);
            toggleOptions(sessionFilters, "subject", subject);
        } else if(teacherOption != null){
            long teacher = Long.parseLong(teacherOption);
            toggleOptions(sessionFilters, "teacher", teacher);
        }

        // save appliedFilters in session
        session.setAttribute("applied_filters", sessionFilters);
        response.sendRedirect(request.getContextPath() + "/courses");
    }

    // Set filter option or remove if already exists
    private void toggleOptions(Map<String, List<Long>> sessionFilters, String filterKey, long filterOption) {
        List<Long> appliedOptions = sessionFilters.get(filterKey);
        if(appliedOptions.contains(filterOption)){
           appliedOptions.remove(filterOption);
        } else {
            appliedOptions.add(filterOption);
        }
    }
}
