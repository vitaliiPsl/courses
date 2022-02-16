package com.example.courses.servlet.admin;

import com.example.courses.service.CourseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/admin/course/delete")
public class DeleteCourseServlet extends HttpServlet {
    private static final CourseService courseService = new CourseService();

    private static final Logger logger = LogManager.getLogger(DeleteCourseServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.trace("Delete course: get");

        String courseId = request.getParameter("course_id");

        if(courseId != null){
            logger.info("Delete course by id: " + courseId);
            try {
                long id = Long.parseLong(courseId);
                courseService.deleteCourse(id);
            } catch (SQLException e) {
                logger.error("SQLException while deleting course", e);
                response.sendRedirect(request.getContextPath() + "/error_handler?type=500");
                return;
            } catch (NumberFormatException e) {
                logger.error("Invalid course id: " + courseId, e);
                response.sendRedirect(request.getContextPath() + "/error_handler?type=404");
                return;
            }

            response.sendRedirect(request.getContextPath() + "/courses");
        } else {
            logger.warn("Course id is null");
            response.sendRedirect(request.getContextPath() + "/error_handler?type=404");
        }
    }
}
