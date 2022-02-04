package com.example.courses.DTO;

import com.example.courses.persistence.entity.Course;
import com.example.courses.persistence.entity.Language;
import com.example.courses.persistence.entity.User;

public class CourseDTO {
    Course course;
    Language language;
    User teacher;

    public CourseDTO(){}

    public CourseDTO(Course course, Language language, User teacher) {
        this.course = course;
        this.language = language;
        this.teacher = teacher;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    @Override
    public String toString() {
        return "CourseDTO{" +
                "course=" + course +
                ", language=" + language +
                ", teacher=" + teacher +
                '}';
    }
}
