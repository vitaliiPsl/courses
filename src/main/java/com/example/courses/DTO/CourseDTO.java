package com.example.courses.DTO;

import com.example.courses.persistence.entity.Course;
import com.example.courses.persistence.entity.Language;
import com.example.courses.persistence.entity.User;

import java.util.List;

public class CourseDTO {
    Course course;
    Language language;
    User teacher;
    List<User> students;

    public CourseDTO(){}

    public CourseDTO(Course course, Language language, User teacher, List<User> students) {
        this.course = course;
        this.language = language;
        this.teacher = teacher;
        this.students = students;
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

    public List<User> getStudents() {
        return students;
    }

    public void setStudents(List<User> students) {
        this.students = students;
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
