package com.example.courses.servlet;

import com.example.courses.DTO.CourseDTO;
import com.example.courses.DTO.SortingDTO;
import com.example.courses.persistence.entity.Course;
import com.example.courses.persistence.entity.Role;
import com.example.courses.persistence.entity.User;
import com.example.courses.service.CourseDTOService;
import com.example.courses.service.CourseFilterService;
import com.example.courses.service.CourseService;
import com.example.courses.service.CourseSortingService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String lang = (String) session.getAttribute("lang");

        List<CourseDTO> courseDTOList = null;
        Map<String, List<String>> availableFilters = null;
        Map<String, List<String>> requestFilters = null;
        List<String> sortingOptions = null;
        List<String> sortingOrderOptions = null;

        try {
            List<Course> courseList;
            String query = request.getParameter("query");

            if (query != null && !query.trim().isEmpty()) {
                courseList = courseService.getCoursesBySearchQuery(query);
                request.setAttribute("query", query);
            } else {
                if (user == null || user.getRole().equals(Role.STUDENT)) {
                    courseList = courseService.getAvailable();
                } else {
                    courseList = courseService.getAll();
                }
            }

            courseDTOList = courseDTOService.getCourseDTOList(courseList, lang);

            availableFilters = courseFilterService.getAvailableFilters(lang);
            requestFilters = (Map<String, List<String>>) session.getAttribute("filters");
            courseFilterService.applyFilters(courseDTOList, requestFilters);

            sortingOptions = courseSortingService.getSortingOptions();
            sortingOrderOptions = courseSortingService.getSortingOrderOptions();
            String sessionSorting = (String) session.getAttribute("sorting");
            String sessionSortingOrder = (String) session.getAttribute("sorting_order");
            System.out.println(sessionSorting);
            System.out.println(sessionSortingOrder);
            courseSortingService.applySoring(courseDTOList, sessionSorting, sessionSortingOrder);

            courseDTOList = pagination(courseDTOList, request);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        request.setAttribute("courses", courseDTOList);
        request.setAttribute("filters", availableFilters);
        request.setAttribute("applied_filters", requestFilters);
        request.setAttribute("sorting_options", sortingOptions);
        request.setAttribute("sorting_order_options", sortingOrderOptions);

        request.getRequestDispatcher(Constants.TEMPLATES_CONSTANTS.COURSE_LIST_JSP).forward(request, response);
    }

    private List<CourseDTO> pagination(List<CourseDTO> courseDTOList, HttpServletRequest request) {
        String pageStr = request.getParameter("page");
        int page = 1;
        if (pageStr != null) {
            page = Integer.parseInt(pageStr);
        }

        int numberOfPages = courseDTOList.size() / RECORDS_PER_PAGE;
        if (courseDTOList.size() % RECORDS_PER_PAGE > 0) {
            numberOfPages++;
        }

        request.setAttribute("page", page);
        request.setAttribute("number_of_pages", numberOfPages);

        int start = page * RECORDS_PER_PAGE - RECORDS_PER_PAGE;
        int end = Math.min(start + RECORDS_PER_PAGE, courseDTOList.size());

        return courseDTOList.subList(start, end);
    }
}
