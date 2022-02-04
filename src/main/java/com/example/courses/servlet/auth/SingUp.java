package com.example.courses.servlet.auth;

import com.example.courses.persistence.entity.Role;
import com.example.courses.persistence.entity.User;
import com.example.courses.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/auth/sign_up")
public class SingUp extends HttpServlet {
    private static final String SING_UP_JSP = "/WEB-INF/templates/auth/sign_up.jsp";

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(SING_UP_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

        try{
            userService.registerUser(user);
        } catch (SQLException e) {
            request.getRequestDispatcher(SING_UP_JSP).forward(request, response);
        } catch (IllegalArgumentException e){
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher(SING_UP_JSP).forward(request, response);
        }

        response.sendRedirect(request.getContextPath() + "/auth/log_in");
    }
}
