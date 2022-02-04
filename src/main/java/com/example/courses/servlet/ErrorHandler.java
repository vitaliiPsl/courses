package com.example.courses.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/error_handler")
public class ErrorHandler extends HttpServlet {
    private final String FORBIDDEN = "forbidden";
    private final String NOT_FOUND = "not_found";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String errorType = req.getParameter("type");

        switch (errorType){
            case FORBIDDEN:
                req.getRequestDispatcher("/WEB-INF/templates/error_pages/forbidden.jsp").forward(req, resp);
            case NOT_FOUND:
                req.getRequestDispatcher("/WEB-INF/templates/error_pages/not_found.jsp").forward(req, resp);
        }
    }
}
