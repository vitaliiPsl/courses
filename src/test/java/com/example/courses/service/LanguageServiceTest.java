package com.example.courses.service;

import com.example.courses.persistence.DAOFactory;
import com.example.courses.persistence.LanguageDAO;
import com.example.courses.persistence.SubjectDAO;
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
class LanguageServiceTest {

    LanguageService languageService;

    @Mock
    DAOFactory daoFactory;

    @Mock
    LanguageDAO languageDAO;

    @Mock
    Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        languageService = new LanguageService(daoFactory, languageDAO);
        lenient().when(daoFactory.getConnection()).thenReturn(connection);
    }

    @Test
    void testGetLanguageById() throws SQLException {
        long languageId = 1;
        long languageTranslationId = 1;

        languageService.getLanguageById(languageId, languageTranslationId);
        verify(languageDAO).findLanguageById(connection, languageId, languageTranslationId);

        doThrow(new SQLException()).when(languageDAO).findLanguageById(connection, languageId, languageTranslationId);
        assertThrows(SQLException.class, () -> languageService.getLanguageById(languageId, languageTranslationId));
    }

    @Test
    void testGetLanguageByCode() throws SQLException {
        String languageCode = "en";

        languageService.getLanguageByCode(languageCode);
        verify(languageDAO).findLanguageByCode(connection, languageCode);

        doThrow(new SQLException()).when(languageDAO).findLanguageByCode(connection, languageCode);
        assertThrows(SQLException.class, () -> languageService.getLanguageByCode(languageCode));
    }

    @Test
    void testGetAllLanguages() throws SQLException {
        long languageTranslationId = 1;

        languageService.getAllLanguages(languageTranslationId);
        verify(languageDAO).findAll(connection, languageTranslationId);

        doThrow(new SQLException()).when(languageDAO).findAll(connection, languageTranslationId);
        assertThrows(SQLException.class, () -> languageService.getAllLanguages(languageTranslationId));
    }
}