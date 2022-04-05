package com.example.courses.servlet.admin;

import com.example.courses.exception.ServerErrorException;
import com.example.courses.persistence.entity.Role;
import com.example.courses.persistence.entity.User;
import com.example.courses.service.UserService;
import com.example.courses.servlet.Constants;
import com.example.courses.utils.PaginationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    private static final Logger logger = LogManager.getLogger(StudentListServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.trace("students: get");

        List<User> studentList;

        try{
            studentList = userService.getUsersByRole(Role.STUDENT);
            studentList = PaginationUtils.applyPagination(studentList, request);
        } catch (SQLException e) {
            logger.error("SQLException while retrieving students", e);
            throw new ServerErrorException();
        }

        request.setAttribute("students", studentList);
        request.getRequestDispatcher(Constants.TEMPLATES_CONSTANTS.STUDENT_LIST_JSP).forward(request, response);
    }
}
