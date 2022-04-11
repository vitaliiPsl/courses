package com.example.courses.servlet.user;

import com.example.courses.dto.CourseDTO;
import com.example.courses.exception.NotFoundException;
import com.example.courses.exception.ServerErrorException;
import com.example.courses.persistence.entity.Course;
import com.example.courses.persistence.entity.Role;
import com.example.courses.persistence.entity.StudentCourse;
import com.example.courses.persistence.entity.User;
import com.example.courses.service.CourseDTOService;
import com.example.courses.service.CourseService;
import com.example.courses.service.StudentCourseService;
import com.example.courses.service.UserService;
import com.example.courses.servlet.Constants;
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

/**
 * This servlet display user's profile
 */
@WebServlet("/user")
public class UserProfileServlet extends HttpServlet {
    private final UserService userService = new UserService();
    private final StudentCourseService studentCourseService = new StudentCourseService();
    private final CourseService courseService = new CourseService();
    private final CourseDTOService courseDTOService = new CourseDTOService();

    private static final Logger logger = LogManager.getLogger(UserProfileServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.trace("User profile: get");

        HttpSession session = request.getSession();
        String lang = (String) session.getAttribute("lang");

        String userId = request.getParameter("user_id");
        if(userId == null) {
            logger.warn("User id is null");
            throw new NotFoundException();
        }

        try {
            long id = Long.parseLong(userId);

            User user = getUser(id);
            request.setAttribute("user", user);

            List<Course> courseList = getUserCourses(user);
            List<CourseDTO> courseDTOList = courseDTOService.getCourseDTOList(courseList, lang);
            request.setAttribute("courses", courseDTOList);
        } catch (SQLException e) {
            logger.error("SQLException: " + e.getMessage(), e);
            throw new ServerErrorException();
        } catch (NumberFormatException e) {
            logger.error("Invalid user id: " + userId, e);
            throw new NotFoundException();
        }

        request.getRequestDispatcher(Constants.TEMPLATES_CONSTANTS.USER_PROFILE).forward(request, response);
    }

    private User getUser(long id) throws SQLException {
        User user = userService.getUserById(id);
        if(user == null){
            logger.warn("User with id " + id + "doesn't exists");
            throw new NotFoundException();
        }
        return user;
    }

    private List<Course> getUserCourses(User user) throws SQLException {
        List<Course> courseList = new ArrayList<>();

        if (user.getRole().equals(Role.STUDENT)) {
            courseList = getStudentCourses(user);
        } else if (user.getRole().equals(Role.TEACHER)) {
            courseList = courseService.getByTeacherId(user.getId());
        }

        return courseList;
    }

    private List<Course> getStudentCourses(User user) throws SQLException {
        List<StudentCourse> studentCourseList = studentCourseService.getCoursesByStudentId(user.getId());
        List<Long> coursesIds = studentCourseList.stream().map(StudentCourse::getCourseId).collect(Collectors.toList());

        return courseService.getCourses(coursesIds);
    }
}
