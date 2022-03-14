package com.example.courses.service;

import com.example.courses.persistence.DAOFactory;
import com.example.courses.persistence.SubjectDAO;
import com.example.courses.persistence.UserDAO;
import com.example.courses.persistence.entity.Subject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubjectServiceTest {

    SubjectService subjectService;

    @Mock
    DAOFactory daoFactory;

    @Mock
    SubjectDAO subjectDAO;

    @Mock
    Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        subjectService = new SubjectService(daoFactory, subjectDAO);
        lenient().when(daoFactory.getConnection()).thenReturn(connection);
    }

    @Test
    void testSaveSubject() throws SQLException {
        Subject subject = new Subject();

        subjectService.saveSubject(subject);
        verify(subjectDAO).saveSubject(connection, subject);

        doThrow(new SQLException()).when(subjectDAO).saveSubject(connection, subject);
        assertThrows(SQLException.class, () -> subjectService.saveSubject(subject));
    }

    @Test
    void testSaveSubjectTranslation() throws SQLException {
        Subject subject = new Subject();

        subjectService.saveSubjectTranslation(subject);
        verify(subjectDAO).saveSubjectTranslation(connection, subject);

        doThrow(new SQLException()).when(subjectDAO).saveSubjectTranslation(connection, subject);
        assertThrows(SQLException.class, () -> subjectService.saveSubjectTranslation(subject));
    }

    @Test
    void testGetSubject() throws SQLException {
        long subjectId = 1;
        long languageId = 1;

        subjectService.getSubject(subjectId, languageId);
        verify(subjectDAO).findSubject(connection, subjectId, languageId);

        doThrow(new SQLException()).when(subjectDAO).findSubject(connection, subjectId, languageId);
        assertThrows(SQLException.class, () -> subjectService.getSubject(subjectId, languageId));
    }

    @Test
    void testGetAll() throws SQLException {
        long languageId = 1;

        subjectService.getAll(languageId);
        verify(subjectDAO).findAll(connection, languageId);

        doThrow(new SQLException()).when(subjectDAO).findAll(connection, languageId);
        assertThrows(SQLException.class, () -> subjectService.getAll(languageId));
    }

    @Test
    void testDeleteSubject() throws SQLException {
        long subjectId = 1;

        subjectService.deleteSubjectById(subjectId);
        verify(subjectDAO).deleteSubjectById(connection, subjectId);

        doThrow(new SQLException()).when(subjectDAO).deleteSubjectById(connection, subjectId);
        assertThrows(SQLException.class, () -> subjectService.deleteSubjectById(subjectId));
    }
}