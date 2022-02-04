package com.example.courses.servlet.admin;

import com.example.courses.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/admin/block")
public class BlockStudentServlet extends HttpServlet {
    private UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String studentId = request.getParameter("student_id");

        if(studentId != null){
            long id = Long.parseLong(studentId);
            try {
                userService.blockUserById(id);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        response.sendRedirect(request.getContextPath() + "/admin/students");
    }
}
