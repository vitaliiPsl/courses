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
import java.util.List;

@WebServlet("/admin/students")
public class StudentListServlet extends HttpServlet {
    private static final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<User> studentList = null;

        try{
            studentList = userService.getUsersByRole(Role.STUDENT);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        request.setAttribute("students", studentList);
        request.getRequestDispatcher("/WEB-INF/templates/admin/student_list.jsp").forward(request, response);
    }
}
