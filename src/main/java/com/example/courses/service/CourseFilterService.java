package com.example.courses.service;

import com.example.courses.DTO.CourseDTO;
import com.example.courses.persistence.entity.Course;
import com.example.courses.persistence.entity.Language;
import com.example.courses.persistence.entity.Subject;
import com.example.courses.persistence.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public Map<String, List<String>> getAvailableFilters(String languageCode) throws SQLException {
        logger.trace("Get available filters");
        logger.debug("Get available filters. Localization language: " + languageCode);

        Map<String, List<String>> filters = new HashMap<>();

        List<Course> courseList;
        List<CourseDTO> courseDTOList;

        try {
            courseList = courseService.getAvailable();
            courseDTOList = courseDTOService.getCourseDTOList(courseList, languageCode);
        } catch (SQLException e){
            logger.error("SQLException while getting courseDTOList", e);
            throw e;
        }

        List<String> subjects = getSubjects(courseDTOList);
        filters.put(SUBJECT_FILTER, subjects);

        List<String> teachers = getTeachers(courseDTOList);
        filters.put(TEACHER_FILTER, teachers);

        return filters;
    }

    public void applyFilters(List<CourseDTO> courseDTOList, Map<String, List<String>> filters) {
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

    private static void filterBySubject(List<CourseDTO> courseDTOList, List<String> subjectList) {
        logger.trace("Filter by subject");
        courseDTOList.removeIf(courseDTO -> !subjectList.contains(courseDTO.getSubject().getSubject()));
    }

    private static void filterByTeacher(List<CourseDTO> courseDTOList, List<String> teacherList) {
        logger.trace("Filter by teacher");
        courseDTOList.removeIf(courseDTO -> !teacherList.contains(courseDTO.getTeacher().getFullName()));
    }

    private static List<String> getSubjects(List<CourseDTO> courseDTOList) {
        logger.trace("Get subject");
        return courseDTOList.stream()
                .map(CourseDTO::getSubject)
                .map(Subject::getSubject)
                .distinct().sorted()
                .collect(Collectors.toList());
    }

    private static List<String> getTeachers(List<CourseDTO> courseDTOList) {
        logger.trace("Get teachers");
        return courseDTOList.stream()
                .map(CourseDTO::getTeacher)
                .map(User::getFullName)
                .distinct().sorted()
                .collect(Collectors.toList());
    }
}
