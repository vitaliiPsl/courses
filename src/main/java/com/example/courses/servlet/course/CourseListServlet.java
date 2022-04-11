package com.example.courses.servlet.course;

import com.example.courses.dto.CourseDTO;
import com.example.courses.exception.NotFoundException;
import com.example.courses.exception.ServerErrorException;
import com.example.courses.persistence.entity.Course;
import com.example.courses.persistence.entity.Role;
import com.example.courses.persistence.entity.User;
import com.example.courses.service.CourseDTOService;
import com.example.courses.service.CourseFilterService;
import com.example.courses.service.CourseService;
import com.example.courses.service.CourseSortingService;
import com.example.courses.servlet.Constants;
import com.example.courses.utils.PaginationUtils;
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
import java.util.Map;

/**
 * This servlet displays list of available courses
 */
@WebServlet("/courses")
public class CourseListServlet extends HttpServlet {
    private static final CourseService courseService = new CourseService();
    private static final CourseDTOService courseDTOService = new CourseDTOService();
    private static final CourseFilterService courseFilterService = new CourseFilterService();
    private static final CourseSortingService courseSortingService = new CourseSortingService();

    private static final Logger logger = LogManager.getLogger(CourseListServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.trace("CourseList: get");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String lang = (String) session.getAttribute("lang");

        List<CourseDTO> courseDTOList;
        try {
            List<Course> courseList = getCourses(request, user);
            courseDTOList = courseDTOService.getCourseDTOList(courseList, lang);
        } catch (SQLException e) {
            logger.error("SQLException: " + e.getMessage(), e);
            throw new ServerErrorException();
        }

        filter(request, session, lang, courseDTOList);
        sort(request, session, courseDTOList);

        courseDTOList = PaginationUtils.applyPagination(courseDTOList, request);

        logger.debug("CourseDTOList: " + courseDTOList);
        request.setAttribute("courses", courseDTOList);

        request.getRequestDispatcher(Constants.TEMPLATES_CONSTANTS.COURSE_LIST_JSP).forward(request, response);
    }

    private List<Course> getCourses(HttpServletRequest request, User user) throws SQLException {
        List<Course> courseList;
        String query = request.getParameter("query");

        if (query != null && !query.trim().isEmpty()) {
            courseList = getCoursesBySearchQuery(user, query);
            request.setAttribute("query", query);
        } else {
            courseList = getCoursesBasedOnUserRole(user);
        }
        return courseList;
    }

    private List<Course> getCoursesBasedOnUserRole(User user) throws SQLException {
        List<Course> courseList;
        if (user == null || user.getRole().equals(Role.STUDENT)) {
            courseList = courseService.getAvailable();
        } else {
            courseList = courseService.getAll();
        }
        return courseList;
    }

    private List<Course> getCoursesBySearchQuery(User user, String query) throws SQLException {
        List<Course> courseList;
        if (user == null || user.getRole().equals(Role.STUDENT)) {
            courseList = courseService.getAvailableBySearchQuery(query);
        } else {
            courseList = courseService.getBySearchQuery(query);
        }
        return courseList;
    }

    private void filter(HttpServletRequest request, HttpSession session, String lang, List<CourseDTO> courseDTOList) {
        // get available filters
        Map<String, List<?>> availableFilters = courseFilterService.getAvailableFilters(courseDTOList);

        // get filters from session and apply them
        Map<String, List<Long>> requestFilters = (Map<String, List<Long>>) session.getAttribute("filters");
        courseFilterService.applyFilters(courseDTOList, requestFilters);

        request.setAttribute("filters", availableFilters);
    }

    private void sort(HttpServletRequest request, HttpSession session, List<CourseDTO> courseDTOList) {
        String lang = (String) session.getAttribute("lang");

        // get sorting and order options
        Map<Integer, String> sortingOptions = courseSortingService.getSortingOptions(lang);
        Map<Integer, String> sortingOrderOptions = courseSortingService.getSortingOrderOptions(lang);

        // get sorting and order from session and apply to courseDTO list
        int sessionSorting = (int) session.getAttribute("sorting");
        int sessionSortingOrder = (int) session.getAttribute("sorting_order");
        courseSortingService.applySoring(courseDTOList, sessionSorting, sessionSortingOrder);

        request.setAttribute("sorting_options", sortingOptions);
        request.setAttribute("sorting_order_options", sortingOrderOptions);
    }
}
