package com.example.courses.servlet.admin;

import com.example.courses.exception.NotFoundException;
import com.example.courses.exception.ServerErrorException;
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

        if(courseId == null){
            logger.warn("Course id is null");
            throw new NotFoundException();
        }

        logger.info("Delete course by id: " + courseId);
        try {
            long id = Long.parseLong(courseId);
            courseService.deleteCourse(id);
        } catch (SQLException e) {
            logger.error("SQLException while deleting course", e);
            throw new ServerErrorException();
        } catch (NumberFormatException e) {
            logger.error("Invalid course id: " + courseId, e);
            throw new NotFoundException();
        }

        response.sendRedirect(request.getContextPath() + "/courses");
    }
}
