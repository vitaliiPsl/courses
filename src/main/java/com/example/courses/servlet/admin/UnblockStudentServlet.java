package com.example.courses.servlet.admin;

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

@WebServlet("/admin/unblock")
public class UnblockStudentServlet extends HttpServlet {
    private UserService userService = new UserService();

    private static final Logger logger = LogManager.getLogger(UnblockStudentServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.trace("Unblock student");
        String studentId = request.getParameter("student_id");

        if(studentId != null){
            logger.info("Unblock student by id: " + studentId);
            try {
                long id = Long.parseLong(studentId);
                userService.unblockUserById(id);
            } catch (SQLException e) {
                logger.error("SQLException while unblocking student", e);
                response.sendRedirect(request.getContextPath() + "/error_handler?type=500");
                return;
            }  catch (NumberFormatException e) {
                logger.error("Invalid student id: " + studentId, e);
                response.sendRedirect(request.getContextPath() + "/error_handler?type=404");
                return;
            }

            response.sendRedirect(request.getContextPath() + "/admin/students");
        } else {
            logger.warn("Student id is null");
            response.sendRedirect(request.getContextPath() + "/error_handler?type=404");
        }
    }
}
