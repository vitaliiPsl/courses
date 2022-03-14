package com.example.courses.persistence.postgres;

import com.example.courses.persistence.SubjectDAO;
import com.example.courses.persistence.entity.Subject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PostgresSubjectDAOTest {

    static String dbUrl = "jdbc:h2:mem:myDb;DB_CLOSE_DELAY=-1";

    String CREATE_TABLE_SUBJECT =
        "CREATE TABLE subject\n" +
                "(\n" +
                "    id   BIGSERIAL,\n" +
                "    name VARCHAR(50),\n" +
                "    CONSTRAINT pk_subject PRIMARY KEY (id)\n" +
                ")";

    String CREATE_TABLE_LANGUAGE =
        "CREATE TABLE language\n" +
                "(\n" +
                "    id   BIGSERIAL,\n" +
                "    name VARCHAR(50) NOT NULL,\n" +
                "    code VARCHAR(10) NOT NULL,\n" +
                "    CONSTRAINT pk_language PRIMARY KEY (id)\n" +
                ")";

    String CREATE_TABLE_SUBJECT_TRANSLATION =
        "CREATE TABLE subject_translation\n" +
                "(\n" +
                "    subject_id       BIGINT,\n" +
                "    language_id      BIGINT,\n" +
                "    name_translation VARCHAR(50),\n" +
                "    CONSTRAINT pk_subject_translation PRIMARY KEY (subject_id, language_id),\n" +
                "    CONSTRAINT fk_subject_translation_subject FOREIGN KEY (subject_id)\n" +
                "        REFERENCES subject (id)\n" +
                "        ON DELETE CASCADE\n" +
                "        ON UPDATE CASCADE,\n" +
                "    CONSTRAINT fk_subject_translation_language FOREIGN KEY (language_id)\n" +
                "        REFERENCES language (id)\n" +
                "        ON DELETE CASCADE\n" +
                "        ON UPDATE CASCADE\n" +
                ")";

    String DROP_TABLE_SUBJECT =
            "DROP TABLE subject CASCADE ";

    String DROP_TABLE_LANGUAGE =
            "DROP TABLE language CASCADE ";

    String DROP_TABLE_SUBJECT_TRANSLATION =
            "DROP TABLE subject_translation";

    final long ENGLISH_ID = 1;
    final long UKRAINIAN_ID = 2;

    String INSERT_LANGUAGES =
            "INSERT INTO language(id, name, code)\n" +
                    "VALUES (" + ENGLISH_ID + ", 'english', 'en'),\n" +
                    "       (" + UKRAINIAN_ID + ", 'ukrainian', 'uk')";

    final long DEFAULT_SUBJECT_ID = 999;
    final String DEFAULT_SUBJECT_NAME = "Business analysis";

    String INSERT_DEFAULT_SUBJECT =
            "INSERT INTO subject(id, name) " +
            "VALUES("+ DEFAULT_SUBJECT_ID + ", '" + DEFAULT_SUBJECT_NAME +"')";

    final String DEFAULT_SUBJECT_UKRAINIAN_NAME = "Бізнес-аналіз";

    String INSERT_DEFAULT_SUBJECT_UKRAINIAN_TRANSLATION =
            "INSERT INTO subject_translation(subject_id, language_id, name_translation) " +
            "VALUES(" + DEFAULT_SUBJECT_ID + ", " + UKRAINIAN_ID + ", '" + DEFAULT_SUBJECT_UKRAINIAN_NAME + "');";

    static SubjectDAO subjectDAO;
    static Connection connection;

    @BeforeAll
    static void globalSetUp() throws SQLException {
        subjectDAO = new PostgresSubjectDAO();
        connection = DriverManager.getConnection(dbUrl);
    }

    @BeforeEach
    void setUp() throws SQLException {
        connection.createStatement().executeUpdate(CREATE_TABLE_SUBJECT);
        connection.createStatement().executeUpdate(CREATE_TABLE_LANGUAGE);
        connection.createStatement().executeUpdate(CREATE_TABLE_SUBJECT_TRANSLATION);
        connection.createStatement().executeUpdate(INSERT_LANGUAGES);
        connection.createStatement().executeUpdate(INSERT_DEFAULT_SUBJECT);
        connection.createStatement().executeUpdate(INSERT_DEFAULT_SUBJECT_UKRAINIAN_TRANSLATION);
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.createStatement().executeUpdate(DROP_TABLE_SUBJECT);
        connection.createStatement().executeUpdate(DROP_TABLE_LANGUAGE);
        connection.createStatement().executeUpdate(DROP_TABLE_SUBJECT_TRANSLATION);
    }

    @Test
    void testConnection(){
        assertNotNull(connection);
    }

    @Test
    void testFindSubject() throws SQLException {
        Subject subject = subjectDAO.findSubject(connection, DEFAULT_SUBJECT_ID, UKRAINIAN_ID);
        assertNotNull(subject);
        assertEquals(DEFAULT_SUBJECT_UKRAINIAN_NAME, subject.getSubject());

        // There is no english translation, so method should return subject with default name
        subject = subjectDAO.findSubject(connection, DEFAULT_SUBJECT_ID, ENGLISH_ID);
        assertNotNull(subject);
        assertEquals(DEFAULT_SUBJECT_NAME, subject.getSubject());
    }

    @Test
    void testSaveSubject() throws SQLException {
        Subject subject = new Subject();
        subject.setSubject("Unit testing");

        long id = subjectDAO.saveSubject(connection, subject);

        Subject result = subjectDAO.findSubject(connection, id, 0);
        assertEquals(subject, result);
    }

    @Test
    void testSaveSubjectTranslation() throws SQLException {
        // save subject with default name
        Subject subject = new Subject();
        subject.setSubject("Unit testing");

        long id = subjectDAO.saveSubject(connection, subject);

        // save subject Ukrainian translation
        String subjectUkrainianTranslation = "Модульне тестування";
        subject.setSubject(subjectUkrainianTranslation);
        subject.setLanguageId(UKRAINIAN_ID);

        subjectDAO.saveSubjectTranslation(connection, subject);

        Subject result = subjectDAO.findSubject(connection, id, UKRAINIAN_ID);
        assertEquals(subject, result);
    }

    @Test
    void testDeleteSubjectById() throws SQLException {
        // save subject with default name
        Subject subject = new Subject();
        subject.setSubject("Unit testing");

        long id = subjectDAO.saveSubject(connection, subject);

        subjectDAO.deleteSubjectById(connection, id);
        Subject result = subjectDAO.findSubject(connection, id, 0);

        assertNull(result);
    }

    @Test
    void testUpdateSubjectDescription() throws SQLException {
        // save subject with default name
        Subject subject = new Subject();
        subject.setSubject("Unit testing");

        long id = subjectDAO.saveSubject(connection, subject);

        // save subject Ukrainian translation
        String subjectEnglishTranslation = "Unit testing";
        subject.setSubject(subjectEnglishTranslation);
        subject.setLanguageId(UKRAINIAN_ID);
        subjectDAO.saveSubjectTranslation(connection, subject);

        // update translation
        String subjectUkrainianTranslation = "Модульне тестування";
        subject.setSubject(subjectUkrainianTranslation);
        subjectDAO.updateSubjectTranslation(connection, subject);

        Subject result = subjectDAO.findSubject(connection, id, UKRAINIAN_ID);
        assertEquals(subjectUkrainianTranslation, result.getSubject());
    }

    @Test
    void testFindAll() throws SQLException {
        for(int i = 0; i < 3; i++) {
            subjectDAO.saveSubject(connection, new Subject());
        }

        // method should return four subjects
        List<Subject> subjectList = subjectDAO.findAll(connection, 0);
        assertEquals(4, subjectList.size());
    }
}