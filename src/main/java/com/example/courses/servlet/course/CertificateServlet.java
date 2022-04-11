package com.example.courses.servlet.course;

import com.example.courses.exception.NotFoundException;
import com.example.courses.exception.ServerErrorException;
import com.example.courses.persistence.entity.Course;
import com.example.courses.persistence.entity.CourseStatus;
import com.example.courses.persistence.entity.StudentCourse;
import com.example.courses.persistence.entity.User;
import com.example.courses.service.CourseService;
import com.example.courses.service.StudentCourseService;
import com.example.courses.service.CertificateService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

/**
 * This servlet handles certificate generation request
 * It requires course id, user to be logged in and current interface language
 */
@WebServlet("/certificate")
public class CertificateServlet extends HttpServlet {
    private static final CourseService courseService = new CourseService();
    private static final StudentCourseService studentCourseService = new StudentCourseService();

    private static final Logger logger = LogManager.getLogger(CertificateServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // required parameters
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String lang = (String) session.getAttribute("lang");
        String courseId = request.getParameter("course_id");

        byte[] certificate = null;

        try{
            long id = Long.parseLong(courseId);
            Course course = courseService.getCourseById(id);
            StudentCourse studentCourse = studentCourseService.getStudentCourse(user.getId(), course.getId());

            // provided course must exist, be completed and student must be registered for that course
            if(course == null || studentCourse == null || !course.getCourseStatus().equals(CourseStatus.COMPLETED)){
                logger.warn("Certificate. Invalid data: " + course + ", " + studentCourse);
                throw new NotFoundException();
            }

            certificate = new CertificateService(lang).makeCertificate(course, user, studentCourse.getScore());
        } catch (SQLException e) {
            logger.error("SQLException", e);
            throw new ServerErrorException();
        } catch (Exception e) {
            logger.error("Exception while making certificate", e);
            throw new ServerErrorException();
        }

        // write certificate bytes in response
        OutputStream out = response.getOutputStream();
        out.write(certificate);
        out.close();
    }
}
