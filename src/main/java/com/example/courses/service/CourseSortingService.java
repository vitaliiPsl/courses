package com.example.courses.service;

import com.example.courses.DTO.CourseDTO;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class CourseSortingService {
    static final String SORT_BY_TITLE = "title";
    static final String SORT_BY_DURATION = "duration";
    static final String SORT_BY_NUMBER_OF_STUDENTS = "number of students";

    static final String SORT_ORDER_ASCENDING = "ascending";
    static final String SORT_ORDER_DESCENDING = "descending";

    public static final String REQUEST_PARAMETER_SORTING = "sorting";
    public static final String REQUEST_PARAMETER_ORDER = "order";

    public List<String> getSortingOptions() {
        return Arrays.asList(
                SORT_BY_TITLE,
                SORT_BY_DURATION,
                SORT_BY_NUMBER_OF_STUDENTS
        );
    }
    public List<String> getSortingOrderOptions() {
        return Arrays.asList(
                SORT_ORDER_ASCENDING,
                SORT_ORDER_DESCENDING
        );
    }

    public String getRequestSorting(HttpServletRequest request){
        return request.getParameter(REQUEST_PARAMETER_SORTING);
    }

    public String getRequestSortingOrder(HttpServletRequest request){
        String order = request.getParameter(REQUEST_PARAMETER_ORDER);

        return order != null ? order : SORT_ORDER_ASCENDING;
    }

    public void applySoring(List<CourseDTO> courseDTOList, String sorting, String order){
        if(sorting == null){
            return;
        }

        switch (sorting) {
            case SORT_BY_TITLE:
                sort(order, courseDTOList, SortingComparators.getTitleComparator());
                break;
            case SORT_BY_DURATION:
                sort(order, courseDTOList, SortingComparators.getDurationComparator());
                break;
            case SORT_BY_NUMBER_OF_STUDENTS:
                sort(order, courseDTOList, SortingComparators.getNumberOfStudentsComparator());
                break;
        }
    }

    private void sort(String order, List<CourseDTO> courseDTOList, Comparator<CourseDTO> comparator) {
        if(order.equals(SORT_ORDER_ASCENDING)) {
            courseDTOList.sort(comparator);
        } else {
            courseDTOList.sort(comparator.reversed());
        }
    }

    private static class SortingComparators {
        public static Comparator<CourseDTO> getTitleComparator() {
            return Comparator.comparing(courseDTO -> courseDTO.getCourse().getTitle());
        }

        public static Comparator<CourseDTO> getDurationComparator() {
            return Comparator.comparing(courseDTO -> Duration.between(courseDTO.getCourse().getStartDate(), courseDTO.getCourse().getEndDate()));
        }

        public static Comparator<CourseDTO> getNumberOfStudentsComparator(){
            return Comparator.comparing(courseDTO -> courseDTO.getStudents().size());
        }
    }
}
