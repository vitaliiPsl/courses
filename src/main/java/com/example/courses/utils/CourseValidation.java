package com.example.courses.utils;

import com.example.courses.persistence.entity.Course;

public class CourseValidation {
    static final int MAX_TITLE_LENGTH = 100;
    static final int MAX_SUBJECT_LENGTH = 100;
    static final int MAX_DESCRIPTION_LENGTH = 512;

    private CourseValidation(){}

    public static boolean isCourseValid(Course course){
        return course != null
                && isTitleValid(course.getTitle())
                && isDescriptionValid(course.getDescription());
    }

    public static boolean isTitleValid(String title) {
        return title != null
                && !title.trim().isEmpty()
                && title.length() <= MAX_TITLE_LENGTH;
    }

    public static boolean isSubjectValid(String subject) {
        return subject != null
                && !subject.trim().isEmpty()
                && subject.length() <= MAX_SUBJECT_LENGTH;
    }

    public static boolean isDescriptionValid(String description) {
        if(description != null){
            return description.length() < MAX_DESCRIPTION_LENGTH;
        }

        return true;
    }

}
