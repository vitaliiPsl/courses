package com.example.courses.servlet.teacher;

import com.example.courses.persistence.entity.StudentCourse;
import com.example.courses.service.StudentCourseService;
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

@WebServlet("/teacher/course/score")
public class ScoresServlet extends HttpServlet {
    private static final StudentCourseService studentCourseService = new StudentCourseService();

    private static final Logger logger = LogManager.getLogger(ScoresServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.trace("Save students scores: post");
        String courseId = request.getParameter("course_id");

        if(courseId != null) {
            logger.debug("Course id: " + courseId);

            try {
                long id = Long.parseLong(courseId);
                List<StudentCourse> studentCourseList = studentCourseService.getStudentsByCourseId(id);

                for (StudentCourse studentCourse : studentCourseList) {
                    String studentScore = request.getParameter("score_" + studentCourse.getStudentId());
                    if (studentScore != null && !studentScore.isBlank()) {
                        studentCourse.setScore(Integer.parseInt(studentScore));
                    }
                }
                studentCourseService.updateStudentCourses(studentCourseList);
            } catch (SQLException e) {
                logger.error("SQLException while updating scores", e);
                response.sendRedirect(request.getContextPath() + "/error_handler?type=500");
                return;
            } catch (NumberFormatException e) {
                logger.error("Invalid course id: " + courseId, e);
                response.sendRedirect(request.getContextPath() + "/error_handler?type=404");
                return;
            }
            response.sendRedirect(request.getContextPath() + "/course?course_id=" + courseId);
        } else {
            response.sendRedirect(request.getContextPath() + "/courses");
        }
    }
}
