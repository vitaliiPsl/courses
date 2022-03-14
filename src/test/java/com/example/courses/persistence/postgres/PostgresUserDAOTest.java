package com.example.courses.persistence.postgres;

import com.example.courses.persistence.UserDAO;
import com.example.courses.persistence.entity.Role;
import com.example.courses.persistence.entity.User;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PostgresUserDAOTest {

    static String dbUrl = "jdbc:h2:mem:myDb;DB_CLOSE_DELAY=-1";

    static String CREATE_TABLE_USER =
            "CREATE TABLE person\n" +
                    "(\n" +
                    "    id         BIGSERIAL,\n" +
                    "    first_name VARCHAR(50)  NOT NULL,\n" +
                    "    last_name  VARCHAR(50)  NOT NULL,\n" +
                    "    email      VARCHAR(50)  NOT NULL,\n" +
                    "    password   VARCHAR(100) NOT NULL,\n" +
                    "    is_blocked BOOL DEFAULT FALSE,\n" +
                    "    role_id    INT,\n" +
                    "    image_name VARCHAR(256),\n" +
                    "    CONSTRAINT pk_person PRIMARY KEY (id),\n" +
                    "    CONSTRAINT fk_person_role FOREIGN KEY (role_id)\n" +
                    "        REFERENCES role (id)\n" +
                    "        ON UPDATE CASCADE\n" +
                    "        ON DELETE NO ACTION,\n" +
                    "    CONSTRAINT uq_person_email UNIQUE (email)\n" +
                    ")";

    static String CREATE_TABLE_ROLE =
            "CREATE TABLE role\n" +
                    "(\n" +
                    "    id   SERIAL,\n" +
                    "    name VARCHAR(50),\n" +
                    "    CONSTRAINT pk_role PRIMARY KEY (id),\n" +
                    "    CONSTRAINT uq_role_name UNIQUE (name)\n" +
                    ")";

    static String DROP_TABLE_ROLE = "DROP TABLE role CASCADE";

    static String DROP_TABLE_USER = "DROP TABLE person CASCADE";

    static String INSERT_ROLES =
            "INSERT INTO role(id, name)\n" +
                    "VALUES (1, 'admin'),\n" +
                    "       (2, 'teacher'),\n" +
                    "       (3, 'student');\n";

    static UserDAO userDAO;

    static Connection connection;

    @BeforeAll
    static void globalSetUp() throws SQLException {
        userDAO = new PostgresUserDAO();
        connection = DriverManager.getConnection(dbUrl);
    }

    @BeforeEach
    void setUp() throws SQLException {
        connection.createStatement().executeUpdate(CREATE_TABLE_ROLE);
        connection.createStatement().executeUpdate(INSERT_ROLES);
        connection.createStatement().executeUpdate(CREATE_TABLE_USER);
    }


    @AfterEach
    void tearDown() throws SQLException {
        connection.createStatement().executeUpdate(DROP_TABLE_USER);
        connection.createStatement().executeUpdate(DROP_TABLE_ROLE);
    }

    @Test
    void testConnection(){
        assertNotNull(connection);
    }

    @Test
    void testFindUserById() throws SQLException {
        long defaultUserId = 999;
        String defaultUserEmail = "default@mail.com";

        String insertStatement =
                "INSERT INTO person(id, first_name, last_name, email, password, role_id) " +
                "VALUES(" + defaultUserId + ", 'John', 'Doe', '" + defaultUserEmail + "', 'password', 1)";
        connection.createStatement().executeUpdate(insertStatement);

        User user = userDAO.findUser(connection, defaultUserId);

        assertNotNull(user);
        assertEquals(defaultUserId, user.getId());
        assertEquals(defaultUserEmail, user.getEmail());
    }

    @Test
    void testFindUserByEmail() throws SQLException {
        long defaultUserId = 999;
        String defaultUserEmail = "default@mail.com";

        String insertStatement =
                "INSERT INTO person(id, first_name, last_name, email, password, role_id) " +
                "VALUES(" + defaultUserId + ", 'John', 'Doe', '" + defaultUserEmail + "', 'password', 1)";
        connection.createStatement().executeUpdate(insertStatement);

        User user = userDAO.findUser(connection, defaultUserEmail);

        assertNotNull(user);
        assertEquals(defaultUserId, user.getId());
        assertEquals(defaultUserEmail, user.getEmail());
    }

    @Test
    void testSaveUser() throws SQLException {
        User user = getUser("j.doe@mail.com", Role.STUDENT);

        long id = userDAO.saveUser(connection, user);
        User retrieved = userDAO.findUser(connection, id);
        assertEquals(user, retrieved);

        User user2 = getUser("user1@mail.com", Role.STUDENT);
        long id2 = userDAO.saveUser(connection, user2);
        assertNotEquals(id, id2);
    }


    @Test
    void testDeleteUser() throws SQLException {
        User user = getUser("j.doe@mail.com", Role.STUDENT);

        long id = userDAO.saveUser(connection, user);
        User retrieved = userDAO.findUser(connection, id);
        assertEquals(user, retrieved);

        userDAO.deleteUserById(connection, id);

        retrieved = userDAO.findUser(connection, id);
        assertNull(retrieved);
    }

    @Test
    void testUpdateUser() throws SQLException {
        User user = getUser("user@mail.com", Role.TEACHER);
        long id = userDAO.saveUser(connection, user);

        String name = "Stephen";
        user.setFirstName(name);
        userDAO.updateUser(connection, user);

        User retrieved = userDAO.findUser(connection, id);
        assertEquals(name, user.getFirstName());
        assertEquals(user.getEmail(), retrieved.getEmail());
    }

    @Test
    void testFindAll() throws SQLException {
        List<User> userList = new ArrayList<>();
        int numberOfUsers = 3;
        insertUsers(numberOfUsers);

        List<User> result = userDAO.findAll(connection);
        assertEquals(numberOfUsers, result.size());
    }

    @Test
    void testFindByRole() throws SQLException {
        insertUsers(4);

        User teacher = getUser("teacher@mail.com", Role.TEACHER);
        User teacher2 = getUser("teacher2@mail.com", Role.TEACHER);

        userDAO.saveUser(connection, teacher);
        userDAO.saveUser(connection, teacher2);

        List<User> teachers = userDAO.findAllByRole(connection, Role.TEACHER);

        assertEquals(2, teachers.size());
        assertTrue(teachers.contains(teacher));
        assertTrue(teachers.contains(teacher2));
    }

    private void insertUsers(int numberOfUserToInsert) throws SQLException {
        for(int i = 0; i < numberOfUserToInsert; i++){
            User user = getUser("user" + i + "@mail.com", Role.STUDENT);
            userDAO.saveUser(connection, user);
        }
    }

    private User getUser(String email, Role role) {
        return new User.Builder()
                .setFirstName("John")
                .setLastName("Doe")
                .setEmail(email)
                .setPassword("password")
                .setRole(role)
                .build();
    }
}