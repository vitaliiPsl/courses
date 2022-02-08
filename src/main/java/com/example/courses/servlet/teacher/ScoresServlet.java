package com.example.courses.servlet.teacher;

import com.example.courses.persistence.entity.StudentCourse;
import com.example.courses.service.StudentCourseService;

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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String courseId = request.getParameter("course_id");

        if(courseId != null) {
            try {
                long id = Long.parseLong(courseId);
                List<StudentCourse> studentCourseList = studentCourseService.getStudentsByCourseId(id);

                for (StudentCourse studentCourse : studentCourseList) {
                    String studentScore = request.getParameter("score_" + studentCourse.getStudentId());

                    if (studentScore != null && !studentScore.trim().isEmpty()) {
                        studentCourse.setScore(Integer.parseInt(studentScore));
                    }
                }

                studentCourseService.updateStudentCourse(studentCourseList);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            response.sendRedirect(request.getContextPath() + "/course?course_id=" + courseId);
        } else {
            response.sendRedirect(request.getContextPath() + "/courses");
        }
    }
}
