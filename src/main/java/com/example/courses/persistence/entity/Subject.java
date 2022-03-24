package com.example.courses.persistence.entity;

import java.util.Objects;

public class Subject {
    private long id;
    private String subject;
    private long languageId;

    public Subject() {
    }

    public Subject(long id, String subject, long languageId) {
        this.id = id;
        this.subject = subject;
        this.languageId = languageId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public long getLanguageId() {
        return languageId;
    }

    public void setLanguageId(long languageId) {
        this.languageId = languageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return id == subject.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", subject='" + subject + '\'' +
                ", languageId=" + languageId +
                '}';
    }
}
