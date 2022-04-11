package com.example.courses.servlet.auth;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This servlet handles log out requests
 */
@WebServlet("/auth/log_out")
public class LogOut extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(LogOut.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("User: " + request.getSession().getAttribute("user") + "logged out");
        request.getSession().invalidate();
        response.sendRedirect(request.getContextPath() + "/");
    }
}
