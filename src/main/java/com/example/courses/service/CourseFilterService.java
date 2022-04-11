package com.example.courses.service;

import com.example.courses.dto.CourseDTO;
import com.example.courses.persistence.entity.Course;
import com.example.courses.persistence.entity.Subject;
import com.example.courses.persistence.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This service allows to filter list of {@link CourseDTO} objects based on selected filters
 */
public class CourseFilterService {
    public static final String SUBJECT_FILTER = "subject";
    public static final String TEACHER_FILTER = "teacher";

    private static final Logger logger = LogManager.getLogger(CourseFilterService.class.getName());

    /**
     * Builds map of available filters with filter name as key and list of filter options as values
     * It takes list of available courses and
     * @param courseDTOList list of CourseDTO objects that represents available courses
     * @return map of available filters
     */
    public Map<String, List<?>> getAvailableFilters(List<CourseDTO> courseDTOList) {
        logger.trace("Get available filters");
        Map<String, List<?>> filters = new HashMap<>();

        List<Subject> subjects = getSubjects(courseDTOList);
        filters.put(SUBJECT_FILTER, subjects);

        List<User> teachers = getTeachers(courseDTOList);
        filters.put(TEACHER_FILTER, teachers);

        return filters;
    }

    /**
     * Applies filters to courseDTO list
     * @param courseDTOList - list courseDTO objects to filter
     * @param filters - filters selected by user
     */
    public void applyFilters(List<CourseDTO> courseDTOList, Map<String, List<Long>> filters) {
        logger.trace("ApplyFilters to courseDTOList: " + courseDTOList);
        logger.info("Applying filters: " + filters);

        logger.debug("Before applying filters: " + courseDTOList);
        if(filters.containsKey(SUBJECT_FILTER) && filters.get(SUBJECT_FILTER).size() != 0){
            filterBySubject(courseDTOList, filters.get(SUBJECT_FILTER));
        }

        if(filters.containsKey(TEACHER_FILTER) && filters.get(TEACHER_FILTER).size() != 0){
            filterByTeacher(courseDTOList, filters.get(TEACHER_FILTER));
        }

        logger.debug("After applying filters: " + courseDTOList);
    }

    private static void filterBySubject(List<CourseDTO> courseDTOList, List<Long> subjectList) {
        logger.trace("Filter by subject");
        courseDTOList.removeIf(courseDTO -> !subjectList.contains(courseDTO.getSubject().getId()));
    }

    private static void filterByTeacher(List<CourseDTO> courseDTOList, List<Long> teacherList) {
        logger.trace("Filter by teacher");
        courseDTOList.removeIf(courseDTO -> !teacherList.contains(courseDTO.getTeacher().getId()));
    }

    private static List<Subject> getSubjects(List<CourseDTO> courseDTOList) {
        logger.trace("Get subject");
        return courseDTOList.stream()
                .map(CourseDTO::getSubject)
                .distinct().sorted(Comparator.comparing(Subject::getSubject))
                .collect(Collectors.toList());
    }

    private static List<User> getTeachers(List<CourseDTO> courseDTOList) {
        logger.trace("Get teachers");
        return courseDTOList.stream()
                .map(CourseDTO::getTeacher)
                .distinct().sorted(Comparator.comparing(User::getFullName))
                .collect(Collectors.toList());
    }
}
