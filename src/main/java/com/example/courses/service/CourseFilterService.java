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

public class CourseFilterService {
    public static final String SUBJECT_FILTER = "subject";
    public static final String TEACHER_FILTER = "teacher";

    private final CourseService courseService;
    private final CourseDTOService courseDTOService;

    private static final Logger logger = LogManager.getLogger(CourseFilterService.class.getName());

    public CourseFilterService(){
        courseService = new CourseService();
        courseDTOService = new CourseDTOService();
    }

    public CourseFilterService(CourseService courseService, CourseDTOService courseDTOService) {
        this.courseService = courseService;
        this.courseDTOService = courseDTOService;
    }

    /**
     * Makes map of available filters
     * @param languageCode - code of translation language
     * @return map of available filters
     * @throws SQLException
     */
    public Map<String, List<?>> getAvailableFilters(String languageCode) throws SQLException {
        logger.trace("Get available filters");
        logger.debug("Get available filters. Localization language: " + languageCode);

        Map<String, List<?>> filters = new HashMap<>();

        List<Course> courseList;
        List<CourseDTO> courseDTOList;

        try {
            courseList = courseService.getAvailable();
            courseDTOList = courseDTOService.getCourseDTOList(courseList, languageCode);
        } catch (SQLException e){
            logger.error("SQLException while getting courseDTOList", e);
            throw e;
        }

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
