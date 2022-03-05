package com.example.courses.persistence;

import com.example.courses.persistence.entity.Subject;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface SubjectDAO {
    long saveSubject(Connection connection, Subject subject) throws SQLException;
    void saveSubjectDescription(Connection connection, Subject subject) throws SQLException;

    void deleteSubjectById(Connection connection, long id) throws SQLException;
    void updateSubjectDescription(Connection connection, Subject subject) throws SQLException;

    Subject findSubject(Connection connection, long id, long languageId) throws SQLException;

    List<Subject> findAll(Connection connection, long languageId) throws SQLException;
}
