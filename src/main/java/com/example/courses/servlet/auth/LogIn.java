package com.example.courses.servlet.auth;

import com.example.courses.exception.ServerErrorException;
import com.example.courses.persistence.entity.User;
import com.example.courses.service.UserService;
import com.example.courses.servlet.Constants;
import com.example.courses.utils.HashingUtils;
import com.example.courses.utils.ReCaptchaUtils;
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

/**
 * This servlet handles log in request
 */
@WebServlet("/auth/log_in")
public class LogIn extends HttpServlet {
    private final UserService userService = new UserService();

    private final static Logger logger = LogManager.getLogger(LogIn.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.trace("get: log in");
        request.getRequestDispatcher(Constants.TEMPLATES_CONSTANTS.LOG_IN_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.trace("post: log in");

        // Login request parameters
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String recaptchaToken = request.getParameter("g-recaptcha-response");

        logger.info("Log in attempt. Provided email: " + email);

        // check if user provided all required parameters
        if(email == null || email.isBlank() || password == null || password.isBlank()){
            logger.error("Log in credentials are empty");

            request.setAttribute("error", "You have to provide email and password");
            this.doGet(request, response);
            return;
        }

        // verify captcha test
        if(!ReCaptchaUtils.verifyCaptcha(recaptchaToken)){
            logger.error("Failed captcha test");

            request.setAttribute("error", "You have failed captcha test");
            this.doGet(request, response);
            return;
        }

        // get existing user by provided email
        User existing = getExisting(email);

        // check if user with provided email exists and request password match stored
        if(existing == null || !HashingUtils.checkPassword(password, existing.getPassword())){
            logger.info("Invalid email(" + email + ") or password. Log in failed");
            request.setAttribute("error", "Invalid email or password");
            this.doGet(request, response);
        } else if(existing.isBlocked()){
            logger.info("User: " + existing + " is blocked");
            request.setAttribute("error", "Your account has been blocked");
            this.doGet(request, response);
        } else{
            logger.info("User: " + existing + " logged in");
            HttpSession session = request.getSession();
            session.setAttribute("user", existing);

            response.sendRedirect(request.getContextPath() + "/");
        }
    }

    private User getExisting(String email) {
        User existing;
        try{
            existing = userService.getUserByEmail(email);
        } catch (SQLException e) {
            logger.error("SQLException while getting existing user by provided email", e);
            throw new ServerErrorException();
        }
        return existing;
    }
}
