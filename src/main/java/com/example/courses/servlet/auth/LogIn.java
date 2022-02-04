package com.example.courses.servlet.auth;

import com.example.courses.persistence.entity.User;
import com.example.courses.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/auth/log_in")
public class LogIn extends HttpServlet {
    private static final String LOG_IN_JSP = "/WEB-INF/templates/auth/log_in.jsp";

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(LOG_IN_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User existing = null;

        try{
            existing = userService.getUserByEmail(email);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        if(existing == null || !existing.getPassword().equals(password)){
            request.setAttribute("error", "Invalid email or password");
            request.getRequestDispatcher(LOG_IN_JSP).forward(request, response);
        } else if(existing.isBlocked()){
            request.setAttribute("error", "Your account has been blocked");
            request.getRequestDispatcher(LOG_IN_JSP).forward(request, response);
        } else{
            HttpSession session = request.getSession();
            session.setAttribute("user", existing);
            response.sendRedirect(request.getContextPath() + "/");
        }
    }
}
