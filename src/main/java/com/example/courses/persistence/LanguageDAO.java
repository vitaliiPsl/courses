package com.example.courses.persistence;

import com.example.courses.persistence.entity.Language;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface LanguageDAO {
    long saveLanguage(Connection connection, Language language) throws SQLException;
    void deleteLanguageById(Connection connection, long id) throws SQLException;

    Language findLanguageById(Connection connection, long id) throws SQLException;
    Language findLanguageByName(Connection connection, String name) throws SQLException;
    Language findLanguageByCode(Connection connection, String code) throws SQLException;
    Language findDefaultLanguage(Connection connection) throws SQLException;

    List<Language> findAll(Connection connection) throws SQLException;
}
