package com.example.courses.persistence.entity;

import java.util.Objects;

public class Language {
    private long id;
    private String name;
    private String languageCode;
    private long translationLanguageId;

    public Language(){}

    public Language(long id, String name, String languageCode, long translationLanguageId) {
        this.id = id;
        this.name = name;
        this.languageCode = languageCode;
        this.translationLanguageId = translationLanguageId;
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

    public long getTranslationLanguageId() {
        return translationLanguageId;
    }

    public void setTranslationLanguageId(long translationLanguageId) {
        this.translationLanguageId = translationLanguageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Language language = (Language) o;
        return id == language.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Language{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", languageCode='" + languageCode + '\'' +
                ", translationLanguageId=" + translationLanguageId +
                '}';
    }
}
