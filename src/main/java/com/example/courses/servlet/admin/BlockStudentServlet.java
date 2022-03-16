package com.example.courses.servlet.admin;

import com.example.courses.exception.NotFoundException;
import com.example.courses.exception.ServerErrorException;
import com.example.courses.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/admin/block")
public class BlockStudentServlet extends HttpServlet {
    private final UserService userService = new UserService();

    private static final Logger logger = LogManager.getLogger(BlockStudentServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.trace("Block user: get");
        String studentId = request.getParameter("student_id");

        if(studentId == null){
            logger.warn("Student id is null");
            throw new NotFoundException();
        }

        logger.info("Block student with id: " + studentId);
        try {
            long id = Long.parseLong(studentId);
            userService.blockUser(id);
        } catch (SQLException e) {
            logger.error("SQLException while blocking student", e);
            throw new ServerErrorException();
        } catch (NumberFormatException e) {
            logger.error("Invalid student id: " + studentId, e);
            throw new NotFoundException();
        }

        response.sendRedirect(request.getContextPath() + "/admin/students");
    }
}
