package com.example.courses.servlet.admin;

import com.example.courses.exception.ServerErrorException;
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

/**
 * This servlet allows to register new subject
 */
@WebServlet("/admin/new_user")
public class NewUserServlet extends HttpServlet {
    private static final UserService userService = new UserService();

    private static final Logger logger = LogManager.getLogger(NewUserServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.trace("New user: get");
        request.getRequestDispatcher(Constants.TEMPLATES_CONSTANTS.NEW_USER_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.trace("New user: post");

        String firstName = request.getParameter("first_name");
        String lastName = request.getParameter("last_name");
        Role role = parseRole(request.getParameter("role"));
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User.Builder builder = new User.Builder();
        User user = builder.setFirstName(firstName)
                .setLastName(lastName)
                .setEmail(email)
                .setPassword(password)
                .setRole(role)
                .build();

        logger.info("Saving new user: " + user);

        try {
            userService.registerUser(user);
        } catch (IllegalArgumentException e) {
            logger.error("Properties are invalid: " + user, e);
            request.setAttribute("error", e.getMessage());
            this.doGet(request, response);
        } catch (SQLException e) {
            logger.error("SQLException while saving new user", e);
            throw new ServerErrorException();
        }

        response.sendRedirect(request.getContextPath() + "/");
    }

    private Role parseRole(String roleName) {
        switch (roleName){
            case "admin":
                return Role.ADMIN;
            case "teacher":
                return Role.TEACHER;
            case "student":
                return Role.STUDENT;
            default:
                logger.error("Invalid user role: " + roleName);
                throw new IllegalArgumentException("There is no such role: '" + roleName + "'");
        }
    }
}
