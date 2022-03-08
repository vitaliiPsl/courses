package com.example.courses.service;

import com.example.courses.DTO.CourseDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class CourseSortingService {
    public static final String SORT_BY_TITLE = "title";
    public static final String SORT_BY_DURATION = "duration";
    public static final String SORT_BY_NUMBER_OF_STUDENTS = "number of students";

    public static final String SORT_ORDER_ASCENDING = "ascending";
    public static final String SORT_ORDER_DESCENDING = "descending";

    private static final Logger logger = LogManager.getLogger(CourseSortingService.class.getName());

    /**
     * Sorting options
     * @return list of sorting options
     */
    public List<String> getSortingOptions() {
        logger.trace("Get sorting options");
        return Arrays.asList(
                SORT_BY_TITLE,
                SORT_BY_DURATION,
                SORT_BY_NUMBER_OF_STUDENTS
        );
    }

    /**
     * Sorting order options
     * @return list of sorting order options
     */
    public List<String> getSortingOrderOptions() {
        logger.trace("Get sorting order options");
        return Arrays.asList(
                SORT_ORDER_ASCENDING,
                SORT_ORDER_DESCENDING
        );
    }

    /**
     * Applies selected sorting to courseDTO list
     * @param courseDTOList - list of courseDTO object to sort
     * @param sorting - selected sorting
     * @param order selected sorting order
     */
    public void applySoring(List<CourseDTO> courseDTOList, String sorting, String order){
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

    private void sort(String order, List<CourseDTO> courseDTOList, Comparator<CourseDTO> comparator) {
        logger.trace("Sort. Course: " + courseDTOList + ". Order: " + order);

        if(order.equals(SORT_ORDER_ASCENDING)) {
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
