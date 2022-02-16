package com.example.courses.servlet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/error_handler")
public class ErrorHandler extends HttpServlet {
    private final String FORBIDDEN = "403";
    private final String NOT_FOUND = "404";
    private final String SERVER_ERROR = "500";

    private static final Logger logger = LogManager.getLogger(ErrorHandler.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.trace("get: error handler");
        String errorType = req.getParameter("type");

        logger.debug("Error: type " + errorType);

        switch (errorType){
            case FORBIDDEN:
                req.getRequestDispatcher(Constants.TEMPLATES_CONSTANTS.ERROR_403).forward(req, resp);
                break;
            case SERVER_ERROR:
                req.getRequestDispatcher(Constants.TEMPLATES_CONSTANTS.ERROR_500).forward(req, resp);
                break;
            default:
                req.getRequestDispatcher(Constants.TEMPLATES_CONSTANTS.ERROR_404).forward(req, resp);;
        }
    }
}
