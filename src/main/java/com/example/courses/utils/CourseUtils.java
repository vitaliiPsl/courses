package com.example.courses.utils;

import com.example.courses.persistence.entity.Course;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

public class CourseUtils {
    private CourseUtils() {
    }

    public static Course buildCourse(HttpServletRequest request) {
        String name = request.getParameter("course_title");
        String description = request.getParameter("course_description");
        long subjectId = Long.parseLong(request.getParameter("course_subject"));
        long teacherId = Long.parseLong(request.getParameter("teacher_id"));
        long languageId = Long.parseLong(request.getParameter("language_id"));
        int maxScore = Integer.parseInt(request.getParameter("max_score"));
        LocalDateTime startDate = LocalDateTime.parse(request.getParameter("start_date"));
        LocalDateTime endDate = LocalDateTime.parse(request.getParameter("end_date"));

        Course course = new Course();
        course.setTitle(name);
        course.setSubjectId(subjectId);
        course.setDescription(description);
        course.setMaxScore(maxScore);
        course.setStartDate(startDate);
        course.setEndDate(endDate);
        course.setTeacherId(teacherId);
        course.setLanguageId(languageId);

        return course;
    }
}
