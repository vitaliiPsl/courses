package com.example.courses.servlet.teacher;

import com.example.courses.exception.NotFoundException;
import com.example.courses.exception.ServerErrorException;
import com.example.courses.persistence.entity.Course;
import com.example.courses.persistence.entity.CourseStatus;
import com.example.courses.persistence.entity.StudentCourse;
import com.example.courses.persistence.entity.User;
import com.example.courses.service.CourseService;
import com.example.courses.service.StudentCourseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Saves students scores for particular course
 */
@WebServlet("/teacher/course/score")
public class ScoresServlet extends HttpServlet {
    private static final CourseService courseService = new CourseService();
    private static final StudentCourseService studentCourseService = new StudentCourseService();

    private static final Logger logger = LogManager.getLogger(ScoresServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.trace("Save students scores: post");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        String courseId = request.getParameter("course_id");
        if(courseId == null) {
            logger.warn("Course id is null");
            throw new NotFoundException();
        }

        logger.debug("Course id: " + courseId);
        try {
            long id = Long.parseLong(courseId);
            Course course = courseService.getCourseById(id);

            // check if course exists and in progress
            if(course == null || !course.getCourseStatus().equals(CourseStatus.IN_PROGRESS) || course.getTeacherId() != user.getId()) {
                response.sendRedirect(request.getContextPath() + "/course?course_id=" + courseId);
                return;
            }

            updateScores(request, course);
        } catch (SQLException e) {
            logger.error("SQLException while updating scores", e);
            throw new ServerErrorException();
        } catch (NumberFormatException e) {
            logger.error("Invalid course id: " + courseId, e);
            throw new NotFoundException();
        }

        response.sendRedirect(request.getContextPath() + "/course?course_id=" + courseId);
    }

    private void updateScores(HttpServletRequest request, Course course) throws SQLException {
        List<StudentCourse> studentCourseList = studentCourseService.getStudentsByCourseId(course.getId());

        for (StudentCourse studentCourse : studentCourseList) {
            String studentScore = request.getParameter("score_" + studentCourse.getStudentId());
            if (studentScore == null && studentScore.isBlank()) continue;

            int score = Integer.parseInt(studentScore);
            if(score <= course.getMaxScore()) {
                studentCourse.setScore(score);
            }
        }

        studentCourseService.updateStudentCourses(studentCourseList);
    }
}
