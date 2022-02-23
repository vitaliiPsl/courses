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

@WebServlet("/courses/sort")
public class CourseSortingServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(CourseSortingServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.trace("Sorting course dto list");
        HttpSession session = request.getSession();

        // Get applied sorting or sorting order and redirect to course list
        String sortingOption = request.getParameter("sorting");
        String sortingOrder = request.getParameter("sorting_order");
        if(sortingOption != null){
            session.setAttribute("sorting", sortingOption);
        } else if(sortingOrder != null){
            session.setAttribute("sorting_order", sortingOrder);
        }

        response.sendRedirect(request.getContextPath() + "/courses");

    }
}
