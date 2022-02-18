package com.example.courses.servlet.user;

import com.example.courses.persistence.entity.User;
import com.example.courses.service.UserService;
import com.example.courses.servlet.Constants;
import com.example.courses.utils.HashingUtils;
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

@WebServlet("/user/password")
public class ChangePasswordServlet extends HttpServlet {
    private static final UserService userService = new UserService();

    private static final Logger logger = LogManager.getLogger(ChangePasswordServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.trace("Change password: get");
        request.getRequestDispatcher(Constants.TEMPLATES_CONSTANTS.CHANGE_PASSWORD).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.trace("Change password: post");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        String currentPassword = request.getParameter("current_password");
        String newPassword = request.getParameter("password");

        if(!UserValidation.isPasswordValid(currentPassword) || !UserValidation.isPasswordValid(newPassword)){
            logger.warn("Invalid password");
            request.setAttribute("error", "Provided data is invalid");
            this.doGet(request, response);
            return;
        }

        if(!HashingUtils.checkPassword(currentPassword, user.getPassword())){
            logger.warn("Password don't match");
            request.setAttribute("error", "Current password is invalid");
            this.doGet(request, response);
            return;
        }

        String hashedPassword = HashingUtils.hashPassword(newPassword);
        user.setPassword(hashedPassword);

        try{
            userService.updateUser(user);
        } catch (SQLException e) {
            logger.error("SQLException while updating user", e);
            response.sendRedirect(request.getContextPath() + "/error_handler?type=500");
            return;
        }

        response.sendRedirect(request.getContextPath() + "/user?user_id=" + user.getId());
    }
}
