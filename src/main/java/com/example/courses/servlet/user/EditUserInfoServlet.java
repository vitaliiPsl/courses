package com.example.courses.servlet.user;

import com.example.courses.persistence.entity.User;
import com.example.courses.service.UserService;
import com.example.courses.servlet.Constants;
import com.example.courses.utils.UserValidation;
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

@WebServlet("/user/edit")
public class EditUserInfoServlet extends HttpServlet {
    private static final UserService userService = new UserService();

    private static final Logger logger = LogManager.getLogger(EditUserInfoServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.trace("EditUserInfo: get");
        request.getRequestDispatcher(Constants.TEMPLATES_CONSTANTS.EDIT_USER_INFO).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.trace("EditUserInfo: post");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        String firstName = request.getParameter("first_name");
        String lastName = request.getParameter("last_name");
        String email = request.getParameter("email");

        if(!UserValidation.isNameValid(firstName) || !UserValidation.isNameValid(lastName) || !UserValidation.isEmailValid(email)){
            logger.warn("Provided properties aren't valid");
            request.setAttribute("error", "Properties are invalid");
            this.doGet(request, response);
            return;
        }

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);

        try {
            userService.updateUser(user);
        } catch (SQLException e) {
            logger.error("SQLException while updating new user", e);
            response.sendRedirect(request.getContextPath() + "/error_handler?type=500");
            return;
        }

        response.sendRedirect(request.getContextPath() + "/user?user_id=" + user.getId());
    }
}
