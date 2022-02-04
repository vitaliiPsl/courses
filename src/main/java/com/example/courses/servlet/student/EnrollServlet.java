package com.example.courses.servlet.student;

import com.example.courses.persistence.entity.User;
import com.example.courses.service.StudentCourseService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;


@WebServlet(urlPatterns = {"/enroll"})
public class EnrollServlet extends HttpServlet {
    private static final StudentCourseService studentCourseService = new StudentCourseService();


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String courseId = request.getParameter("course_id");

        if(courseId != null) {
            long id = Long.parseLong(courseId);
            User user = (User) request.getSession().getAttribute("user");

            try {
                studentCourseService.registerStudentForCourse(user.getId(), id);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            response.sendRedirect(request.getContextPath() + "/course?course_id=" + id);
        } else {
            response.sendRedirect(request.getContextPath() + "/courses");
        }
    }
}
