package com.example.courses.servlet;

import com.example.courses.DTO.CourseDTO;
import com.example.courses.persistence.entity.Course;
import com.example.courses.persistence.entity.Role;
import com.example.courses.persistence.entity.StudentCourse;
import com.example.courses.persistence.entity.User;
import com.example.courses.service.CourseDTOService;
import com.example.courses.service.CourseService;
import com.example.courses.service.StudentCourseService;
import com.example.courses.service.UserService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/user")
public class UserProfileServlet extends HttpServlet {
    private final UserService userService = new UserService();
    private final StudentCourseService studentCourseService = new StudentCourseService();
    private final CourseService courseService = new CourseService();
    private final CourseDTOService courseDTOService = new CourseDTOService();

    private static final Logger logger = LogManager.getLogger(UserProfileServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String lang = (String) session.getAttribute("lang");

        User user = null;
        String userId = request.getParameter("user_id");
        List<CourseDTO> courseDTOList = null;

        try{
            long id = Long.parseLong(userId);
            user = userService.getUserById(id);
            List<Course> courseList = new ArrayList<>();

            if(user.getRole().equals(Role.STUDENT)){
                List<StudentCourse> studentCourseList = studentCourseService.getCoursesByStudentId(id);
                List<Long> coursesIds = studentCourseList.stream().map(StudentCourse::getCourseId).collect(Collectors.toList());
                courseList = courseService.getCourses(coursesIds);
            } else if(user.getRole().equals(Role.TEACHER)){
                courseList = courseService.getCoursesByTeacherId(id);
            }

            courseDTOList = courseDTOService.getCourseDTOList(courseList, lang);
        } catch (SQLException e) {
            logger.error("SQLException: " + e.getMessage(), e);
            response.sendRedirect(request.getContextPath() + "/error_handler?type=500");
            return;
        }

        request.setAttribute("user", user);
        request.setAttribute("courses", courseDTOList);

        request.getRequestDispatcher(Constants.TEMPLATES_CONSTANTS.USER_PROFILE).forward(request, response);
    }
}
