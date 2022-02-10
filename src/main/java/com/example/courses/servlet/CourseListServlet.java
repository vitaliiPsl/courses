package com.example.courses.servlet;

import com.example.courses.DTO.CourseDTO;
import com.example.courses.persistence.entity.Course;
import com.example.courses.persistence.entity.Role;
import com.example.courses.persistence.entity.User;
import com.example.courses.service.CourseDTOService;
import com.example.courses.service.CourseFilterService;
import com.example.courses.service.CourseService;
import com.example.courses.service.CourseSortingService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        User user = (User) request.getSession().getAttribute("user");
        List<CourseDTO> courseDTOList = null;
        Map<String, List<String>> availableFilters = null;
        Map<String, List<String>> requestFilters = null;
        String requestSorting = null;
        String requestSortingOrder = null;

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
            courseDTOList = courseDTOService.getCourseDTOList(courseList);

            availableFilters = courseFilterService.getAvailableFilters();
            requestFilters = courseFilterService.getRequestFilters(request);
            courseFilterService.applyFilters(courseDTOList, requestFilters);

            requestSorting = courseSortingService.getRequestSorting(request);
            requestSortingOrder = courseSortingService.getRequestSortingOrder(request);
            courseSortingService.applySoring(courseDTOList, requestSorting, requestSortingOrder);

            courseDTOList = pagination(courseDTOList, request);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        request.setAttribute("courses", courseDTOList);
        request.setAttribute("filters", availableFilters);
        request.setAttribute("applied_filters", requestFilters);
        request.setAttribute("sorting_options", courseSortingService.getSortingOptions());
        request.setAttribute("applied_sorting", requestSorting);
        request.setAttribute("sorting_order_options", courseSortingService.getSortingOrderOptions());
        request.setAttribute("applied_sorting_order", requestSortingOrder);

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
