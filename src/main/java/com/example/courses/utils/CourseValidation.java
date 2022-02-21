package com.example.courses.utils;

import com.example.courses.persistence.entity.Course;

import java.time.LocalDateTime;

public class CourseValidation {
    private CourseValidation() {
    }

    public static boolean isCourseValid(Course course) {
        return course != null
                && isTitleValid(course.getTitle())
                && isStartAndEndDateValid(course.getStartDate(), course.getEndDate());
    }

    public static boolean isTitleValid(String title) {
        return title != null && !title.isBlank();
    }

    public static boolean isStartAndEndDateValid(LocalDateTime startDate, LocalDateTime endDate) {
        return startDate != null && endDate != null && startDate.isBefore(endDate) && startDate.isAfter(LocalDateTime.now());
    }

}
