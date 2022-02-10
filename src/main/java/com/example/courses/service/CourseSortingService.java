package com.example.courses.service;

import com.example.courses.DTO.CourseDTO;
import com.example.courses.DTO.SortingDTO;

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

    private SortingDTO sortingDTO;

    public SortingDTO sort(List<CourseDTO> courseDTOList, HttpServletRequest request){
        sortingDTO = (SortingDTO) request.getSession().getAttribute("sortingDTO");
        if(sortingDTO == null){
            sortingDTO = new SortingDTO();
        }

        sortingDTO.setSortingOptions(getSortingOptions());
        sortingDTO.setSortingOrderOptions(getSortingOrderOptions());

        String requestSorting = getRequestSorting(request);
        String requestSortingOrder = getRequestSortingOrder(request);
        applySoring(courseDTOList, requestSorting, requestSortingOrder);
        sortingDTO.setAppliedSorting(requestSorting);
        sortingDTO.setAppliedSortingOrder(requestSortingOrder);

        System.out.println(sortingDTO);

        return sortingDTO;
    }

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
        String sorting = null;
        String requestSorting = request.getParameter(REQUEST_PARAMETER_SORTING);

        if(requestSorting != null){
            sorting = requestSorting;
        } else if(sortingDTO.getAppliedSorting() != null){
            sorting = sortingDTO.getAppliedSorting();
        } else {
            sorting = SORT_BY_TITLE;
        }

        return sorting;
    }

    public String getRequestSortingOrder(HttpServletRequest request){
        String sortingOrder;
        String requestSortingOrder = request.getParameter(REQUEST_PARAMETER_ORDER);

        if(requestSortingOrder != null){
            sortingOrder = requestSortingOrder;
        } else if(sortingDTO.getAppliedSortingOrder() != null){
            sortingOrder = sortingDTO.getAppliedSortingOrder();
        } else {
            sortingOrder = SORT_ORDER_ASCENDING;
        }

        return sortingOrder;
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
