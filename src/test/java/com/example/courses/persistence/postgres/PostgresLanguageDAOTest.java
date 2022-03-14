package com.example.courses.persistence.postgres;

import com.example.courses.persistence.LanguageDAO;
import com.example.courses.persistence.entity.Language;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class PostgresLanguageDAOTest {

    static String dbUrl = "jdbc:h2:mem:myDb;DB_CLOSE_DELAY=-1";

    String CREATE_TABLE_LANGUAGE =
            "CREATE TABLE language\n" +
                    "(\n" +
                    "    id   BIGSERIAL,\n" +
                    "    name VARCHAR(50) NOT NULL,\n" +
                    "    code VARCHAR(10) NOT NULL,\n" +
                    "    CONSTRAINT pk_language PRIMARY KEY (id)\n" +
                    ")";

    String CREATE_TABLE_LANGUAGE_TRANSLATION =
            "CREATE TABLE language_translation\n" +
                    "(\n" +
                    "    language_id             BIGINT      NOT NULL,\n" +
                    "    translation_language_id BIGINT      NOT NULL,\n" +
                    "    name_translation        VARCHAR(50) NOT NULL,\n" +
                    "    code_translation        VARCHAR(10) NOT NULL,\n" +
                    "    CONSTRAINT pk_language_translation PRIMARY KEY (language_id, translation_language_id),\n" +
                    "    CONSTRAINT fk_language_translation_language FOREIGN KEY (language_id)\n" +
                    "        REFERENCES language (id)\n" +
                    "        ON DELETE CASCADE\n" +
                    "        ON UPDATE CASCADE,\n" +
                    "    CONSTRAINT fk_language_translation_language_translation FOREIGN KEY (translation_language_id)\n" +
                    "        REFERENCES language (id)\n" +
                    "        ON DELETE CASCADE\n" +
                    "        ON UPDATE CASCADE\n" +
                    ")";

    String DROP_TABLE_LANGUAGE = "DROP TABLE language CASCADE";

    String DROP_TABLE_LANGUAGE_TRANSLATION = "DROP TABLE language_translation";

    final long ENGLISH_ID = 98;
    final long UKRAINIAN_ID = 99;

    final String ENGLISH_NAME = "english";
    final String ENGLISH_CODE = "en";

    String INSERT_LANGUAGES =
            "INSERT INTO language(id, name, code)\n" +
                    "VALUES (" + ENGLISH_ID + ", '" + ENGLISH_NAME + "', '" + ENGLISH_CODE + "'),\n" +
                    "       (" + UKRAINIAN_ID + ", 'ukrainian', 'uk')";

    String ENGLISH_LANGUAGE_UKRAINIAN_TRANSLATION =
            "англійська";

    String INSERT_ENGLISH_LANGUAGE_UKRAINIAN_TRANSLATION =
            "INSERT INTO language_translation(language_id, translation_language_id, name_translation, code_translation)\n" +
                    "VALUES (" + ENGLISH_ID + ", "+ UKRAINIAN_ID + ", '" + ENGLISH_LANGUAGE_UKRAINIAN_TRANSLATION + "', 'англ')";

    static LanguageDAO languageDAO;
    static Connection connection;

    @BeforeAll
    static void globalSetUp() throws SQLException {
        languageDAO = new PostgresLanguageDAO();
        connection = DriverManager.getConnection(dbUrl);
    }

    @BeforeEach
    void setUp() throws SQLException {
        connection.createStatement().executeUpdate(CREATE_TABLE_LANGUAGE);
        connection.createStatement().executeUpdate(CREATE_TABLE_LANGUAGE_TRANSLATION);
        connection.createStatement().executeUpdate(INSERT_LANGUAGES);
        connection.createStatement().executeUpdate(INSERT_ENGLISH_LANGUAGE_UKRAINIAN_TRANSLATION);
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.createStatement().executeUpdate(DROP_TABLE_LANGUAGE);
        connection.createStatement().executeUpdate(DROP_TABLE_LANGUAGE_TRANSLATION);
    }

    @Test
    void testConnection(){
        assertNotNull(connection);
    }

    @Test
    void testFindLanguageById() throws SQLException {
        Language lang = languageDAO.findLanguageById(connection, ENGLISH_ID, UKRAINIAN_ID);
        assertNotNull(lang);
        assertEquals(ENGLISH_LANGUAGE_UKRAINIAN_TRANSLATION, lang.getName());

        // there is no english translation, so it should return default name
        lang = languageDAO.findLanguageById(connection, ENGLISH_ID, ENGLISH_ID);
        assertEquals(ENGLISH_NAME, lang.getName());
    }

    @Test
    void testFindLanguageByCode() throws SQLException {
        Language lang = languageDAO.findLanguageByCode(connection, ENGLISH_CODE);

        assertNotNull(lang);
        assertEquals(ENGLISH_NAME, lang.getName());
    }

    @Test
    void testSaveLanguage() throws SQLException {
        Language language = new Language();
        language.setName("Français");
        language.setLanguageCode("fr");

        long id = languageDAO.saveLanguage(connection, language);

        Language result = languageDAO.findLanguageById(connection, id, 0);
        assertNotNull(result);
        assertEquals(language.getName(), result.getName());
    }

    @Test
    void testSaveLanguageTranslation() throws SQLException {
        Language language = new Language();
        language.setName("Français");
        language.setLanguageCode("fr");

        long id = languageDAO.saveLanguage(connection, language);

        String englishTranslation = "French";
        language.setName(englishTranslation);
        language.setLanguageCode("fr");
        language.setTranslationLanguageId(ENGLISH_ID);
        languageDAO.saveLanguageTranslation(connection, language);

        Language result = languageDAO.findLanguageById(connection, id, ENGLISH_ID);
        assertNotNull(result);
        assertEquals(englishTranslation, result.getName());
    }

    @Test
    void deleteLanguageById() throws SQLException {
        Language language = new Language();
        language.setName("Français");
        language.setLanguageCode("fr");

        long id = languageDAO.saveLanguage(connection, language);

        languageDAO.deleteLanguageById(connection, id);

        Language result = languageDAO.findLanguageById(connection, id, 0);
        assertNull(result);
    }

    @Test
    void findAll() throws SQLException {
        Language language = new Language();
        language.setName("Français");
        language.setLanguageCode("fr");

        languageDAO.saveLanguage(connection, language);

        // it should return 3 languages, 1 just inserted and 2 default
        List<Language> languageList = languageDAO.findAll(connection, 0);
        assertEquals(3, languageList.size());
    }
}