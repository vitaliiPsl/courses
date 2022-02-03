package com.example.courses.persistence.entity;

public enum Role {
    ADMIN("admin"), TEACHER("teacher"), STUDENT("student");

    final String role;

    Role(String role){
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "Role{" +
                "role='" + role + '\'' +
                '}';
    }
}
