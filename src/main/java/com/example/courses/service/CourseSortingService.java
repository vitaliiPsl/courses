package com.example.courses.service;

import com.example.courses.dto.CourseDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.*;

public class CourseSortingService {
    public static final int SORT_BY_TITLE = 1;
    public static final int SORT_BY_DURATION = 2;
    public static final int SORT_BY_NUMBER_OF_STUDENTS = 3;

    public static final int SORT_ORDER_ASCENDING = 1;
    public static final int SORT_ORDER_DESCENDING = 2;

    private static final Logger logger = LogManager.getLogger(CourseSortingService.class.getName());

    /**
     * Sorting options
     * @return list of sorting options
     */
    public Map<Integer, String> getSortingOptions(String lang) {
        logger.trace("Get sorting options");

        Locale locale = new Locale(lang);
        ResourceBundle bundle = ResourceBundle.getBundle("i18n.sorting.sorting", locale);

        return Map.of(
                SORT_BY_TITLE, bundle.getString("label.sorting_option.title"),
                SORT_BY_DURATION, bundle.getString("label.sorting_option.duration"),
                SORT_BY_NUMBER_OF_STUDENTS, bundle.getString("label.sorting_option.number_of_students")
        );
    }

    /**
     * Sorting order options
     * @return list of sorting order options
     */
    public Map<Integer, String> getSortingOrderOptions(String lang) {
        logger.trace("Get sorting order options");

        Locale locale = new Locale(lang);
        ResourceBundle bundle = ResourceBundle.getBundle("i18n.sorting.sorting", locale);

        return Map.of(
                SORT_ORDER_ASCENDING, bundle.getString("label.sorting_order.ascending"),
                SORT_ORDER_DESCENDING, bundle.getString("label.sorting_order.descending")
        );
    }

    /**
     * Applies selected sorting to courseDTO list
     * @param courseDTOList - list of courseDTO object to sort
     * @param sorting - selected sorting
     * @param order selected sorting order
     */
    public void applySoring(List<CourseDTO> courseDTOList, int sorting, int order){
        logger.trace("Apply sorting to following courseDTO list: " + courseDTOList);
        logger.info("Applying sorting. Sorting: " + sorting + ", order: " + order);

        logger.debug("Before sorting: " + courseDTOList);
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

        logger.debug("After sorting: " + courseDTOList);
    }

    private void sort(int order, List<CourseDTO> courseDTOList, Comparator<CourseDTO> comparator) {
        logger.trace("Sort. Course: " + courseDTOList + ". Order: " + order);

        if(order == SORT_ORDER_ASCENDING) {
            logger.trace("Sort in ascending order");
            courseDTOList.sort(comparator);
        } else {
            logger.trace("Sort in descending order");
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
