package com.example.courses.persistence.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class StudentCourse {
    long studentId;
    long courseId;

    LocalDateTime registrationDate;
    int score;

    public StudentCourse() {
        registrationDate = LocalDateTime.now();
    }

    public StudentCourse(long studentId, long courseId, LocalDateTime registrationDate, int score) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.registrationDate = registrationDate;
        this.score = score;
    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentCourse that = (StudentCourse) o;
        return studentId == that.studentId && courseId == that.courseId && score == that.score;
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, courseId, score);
    }

    @Override
    public String toString() {
        return "StudentCourse{" +
                "studentId=" + studentId +
                ", courseId=" + courseId +
                ", registrationDate=" + registrationDate +
                ", score=" + score +
                '}';
    }
}
