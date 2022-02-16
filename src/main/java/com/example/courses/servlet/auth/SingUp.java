package com.example.courses.servlet.auth;

import com.example.courses.persistence.entity.Role;
import com.example.courses.persistence.entity.User;
import com.example.courses.service.UserService;
import com.example.courses.servlet.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/auth/sign_up")
public class SingUp extends HttpServlet {
    private final UserService userService = new UserService();

    private final static Logger logger = LogManager.getLogger(SingUp.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.trace("get: sign up");
        request.getRequestDispatcher(Constants.TEMPLATES_CONSTANTS.SIGN_UP_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.trace("post: sign up");
        String firstName = request.getParameter("first_name");
        String lastName = request.getParameter("last_name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(Role.STUDENT);

        logger.info("Sign up request: " + user);

        try{
            userService.registerUser(user);
            logger.info("Sing up went successfully");
        } catch (SQLException e) {
            logger.error("SQLException while saving new user", e);
            this.doGet(request, response);
        } catch (IllegalArgumentException e){
            logger.warn("Provided data is invalid: " + user, e);

            request.setAttribute("error", e.getMessage());
            this.doGet(request, response);
        }

        response.sendRedirect(request.getContextPath() + "/auth/log_in");
    }
}
