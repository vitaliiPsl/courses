package com.example.courses.utils;

import com.example.courses.persistence.entity.Course;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

public class CourseUtils {
    private CourseUtils() {
    }

    public static Course buildCourse(HttpServletRequest request) {
        String title = request.getParameter("course_title");
        String description = request.getParameter("course_description");

        long subjectId = Long.parseLong(request.getParameter("subject_id"));
        long teacherId = Long.parseLong(request.getParameter("teacher_id"));
        long languageId = Long.parseLong(request.getParameter("language_id"));

        int maxScore = Integer.parseInt(request.getParameter("max_score"));
        LocalDateTime startDate = LocalDateTime.parse(request.getParameter("start_date"));
        LocalDateTime endDate = LocalDateTime.parse(request.getParameter("end_date"));

        Course.Builder builder = new Course.Builder();
        builder.setTitle(title)
                .setSubjectId(subjectId)
                .setTeacherId(teacherId)
                .setLanguageId(languageId)
                .setDescription(description)
                .setMaxScore(maxScore)
                .setStartDate(startDate)
                .setEndDate(endDate);

        return builder.build();
    }
}
