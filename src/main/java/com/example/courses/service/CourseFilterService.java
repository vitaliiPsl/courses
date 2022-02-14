package com.example.courses.service;

import com.example.courses.DTO.CourseDTO;
import com.example.courses.persistence.entity.Course;
import com.example.courses.persistence.entity.Language;
import com.example.courses.persistence.entity.Subject;
import com.example.courses.persistence.entity.User;

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

    public CourseFilterService(){
        courseService = new CourseService();
        courseDTOService = new CourseDTOService();
    }

    public Map<String, List<String>> getAvailableFilters(String languageCode) throws SQLException {
        Map<String, List<String>> filters = new HashMap<>();

        List<Course> courseList = courseService.getAvailable();
        List<CourseDTO> courseDTOList = courseDTOService.getCourseDTOList(courseList, languageCode);

        List<String> subjects = getSubjects(courseDTOList);
        filters.put(SUBJECT_FILTER, subjects);

        List<String> teachers = getTeachers(courseDTOList);
        filters.put(TEACHER_FILTER, teachers);

        return filters;
    }

    public Map<String, List<String>> getRequestFilters(HttpServletRequest request) {
        Map<String, List<String>> filters = new HashMap<>();

        String[] subjects = request.getParameterValues(SUBJECT_FILTER);
        if(subjects != null){
            List<String> subjectList = Arrays.asList(subjects);
            filters.put(SUBJECT_FILTER, subjectList);
        }

        String[] teachers = request.getParameterValues(TEACHER_FILTER);
        if(teachers != null){
            List<String> teacherList = Arrays.asList(teachers);
            filters.put(TEACHER_FILTER, teacherList);
        }

        return filters;
    }

    public void applyFilters(List<CourseDTO> courseDTOList, Map<String, List<String>> filters) {
        if(filters.containsKey(SUBJECT_FILTER) && filters.get(SUBJECT_FILTER).size() != 0){
            filterBySubject(courseDTOList, filters.get(SUBJECT_FILTER));
        }
        if(filters.containsKey(TEACHER_FILTER) && filters.get(TEACHER_FILTER).size() != 0){
            filterByTeacher(courseDTOList, filters.get(TEACHER_FILTER));
        }
    }

    private static void filterBySubject(List<CourseDTO> courseDTOList, List<String> subjectList) {
        courseDTOList.removeIf(courseDTO -> !subjectList.contains(courseDTO.getSubject().getSubject()));
    }

    private static void filterByTeacher(List<CourseDTO> courseDTOList, List<String> teacherList) {
        courseDTOList.removeIf(courseDTO -> !teacherList.contains(courseDTO.getTeacher().getFullName()));
    }

    private static List<String> getSubjects(List<CourseDTO> courseDTOList) {
        return courseDTOList.stream()
                .map(CourseDTO::getSubject)
                .map(Subject::getSubject)
                .distinct().sorted()
                .collect(Collectors.toList());
    }

    private static List<String> getTeachers(List<CourseDTO> courseDTOList) {
        return courseDTOList.stream()
                .map(CourseDTO::getTeacher)
                .map(User::getFullName)
                .distinct().sorted()
                .collect(Collectors.toList());
    }
}
