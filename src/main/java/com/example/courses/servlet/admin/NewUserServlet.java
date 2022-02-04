package com.example.courses.servlet.admin;

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

@WebServlet("/admin/new_user")
public class NewUserServlet extends HttpServlet {
    private static final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/templates/admin/new_user.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String firstName = request.getParameter("first_name");
        String lastName = request.getParameter("last_name");
        Role role = parseRole(request.getParameter("role"));
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRole(role);
        user.setEmail(email);
        user.setPassword(password);

        try {
            userService.registerUser(user);
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/templates/admin/new_user.jsp").forward(request, response);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
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
        }

        throw new IllegalArgumentException("There is no such role '" + roleName + "'");
    }
}
