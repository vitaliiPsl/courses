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

@WebServlet("/courses/filter")
public class CourseFilterServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(CourseFilterServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get filters stored in session
        HttpSession session = request.getSession();
        Map<String, List<String>> sessionFilters = (Map<String, List<String>>) session.getAttribute("filters");
        logger.debug("Filters stored in session: " + sessionFilters);

        // Get applied / removed filterKey and option and redirect to course list
        String subjectOption = request.getParameter("subject");
        String teacherOption = request.getParameter("teacher");
        if(subjectOption != null){
            toggleOptions(sessionFilters, "subject", subjectOption);
        } else if(teacherOption != null){
            toggleOptions(sessionFilters, "teacher", teacherOption);
        }

        // save appliedFilters in session
        session.setAttribute("applied_filters", sessionFilters);
        response.sendRedirect(request.getContextPath() + "/courses");
    }

    // Set filter option or remove if already exists
    private void toggleOptions(Map<String, List<String>> sessionFilters, String filterKey, String filterOption) {
        List<String> appliedOptions = sessionFilters.get(filterKey);
        if(appliedOptions.contains(filterOption)){
           appliedOptions.remove(filterOption);
        } else {
            appliedOptions.add(filterOption);
        }
    }
}
