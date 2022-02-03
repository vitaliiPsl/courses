package com.example.courses.persistence.entity;

import java.util.Objects;

public class Language {
    private long id;
    private String name;
    private String languageCode;

    public Language(){}

    public Language(long id, String name, String languageCode) {
        this.id = id;
        this.name = name;
        this.languageCode = languageCode;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Language language = (Language) o;
        return id == language.id && Objects.equals(name, language.name) && Objects.equals(languageCode, language.languageCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, languageCode);
    }

    @Override
    public String toString() {
        return "Language{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", languageCode='" + languageCode + '\'' +
                '}';
    }
}
