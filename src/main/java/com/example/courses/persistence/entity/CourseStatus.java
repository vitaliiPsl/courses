package com.example.courses.persistence.entity;

public enum CourseStatus {
    NOT_STARTED("not started"),
    IN_PROGRESS("in progress"),
    COMPLETED("completed");

    String status;

    CourseStatus(String status){
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "CourseStatus{" +
                "status='" + status + '\'' +
                '}';
    }
}
