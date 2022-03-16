package com.example.courses.servlet.course;

import com.example.courses.DTO.CourseDTO;
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

@WebServlet("/courses")
public class CourseListServlet extends HttpServlet {
    private static final int RECORDS_PER_PAGE = 5;

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
            List<Course> courseList;
            String query = request.getParameter("query");

            if (query != null && !query.trim().isEmpty()) {
                courseList = courseService.getBySearchQuery(query);
                request.setAttribute("query", query);
            } else {
                if (user == null || user.getRole().equals(Role.STUDENT)) {
                    courseList = courseService.getAvailable();
                } else {
                    courseList = courseService.getAll();
                }
            }

            courseDTOList = courseDTOService.getCourseDTOList(courseList, lang);

            filter(request, session, lang, courseDTOList);
            sort(request, session, courseDTOList);

            courseDTOList = applyPagination(courseDTOList, request);
        } catch (SQLException e) {
            logger.error("SQLException: " + e.getMessage(), e);
            throw new ServerErrorException();
        }

        logger.debug("CourseDTOList: " + courseDTOList);
        request.setAttribute("courses", courseDTOList);

        request.getRequestDispatcher(Constants.TEMPLATES_CONSTANTS.COURSE_LIST_JSP).forward(request, response);
    }

    private void filter(HttpServletRequest request, HttpSession session, String lang, List<CourseDTO> courseDTOList) throws SQLException {
        // get available filters
        Map<String, List<?>> availableFilters = courseFilterService.getAvailableFilters(lang);

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

    private List<CourseDTO> applyPagination(List<CourseDTO> courseDTOList, HttpServletRequest request) throws IOException {
        // default page
        int currentPage = 1;

        // get current page if it is not null
        String pageStr = request.getParameter("page");
        if (pageStr != null) {
            currentPage = Integer.parseInt(pageStr);
        }

        // calculate total number of pages
        int numberOfPages = courseDTOList.size() / RECORDS_PER_PAGE;
        if (courseDTOList.size() % RECORDS_PER_PAGE > 0) {
            numberOfPages++;
        }

        // redirect to 'not found' if current page is not in range of total number of pages
        if (numberOfPages != 0 && (currentPage < 1 || currentPage > numberOfPages)) {
            logger.error("Chosen page (" + currentPage + ") does not exists");
            throw new NotFoundException();
        }

        // save current page and number of pages in request
        request.setAttribute("page", currentPage);
        request.setAttribute("number_of_pages", numberOfPages);

        // return sublist
        int start = currentPage * RECORDS_PER_PAGE - RECORDS_PER_PAGE;
        int end = Math.min(start + RECORDS_PER_PAGE, courseDTOList.size());

        return courseDTOList.subList(start, end);
    }
}
