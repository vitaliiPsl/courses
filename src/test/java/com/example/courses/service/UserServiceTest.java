package com.example.courses.service;

import com.example.courses.persistence.DAOFactory;
import com.example.courses.persistence.UserDAO;
import com.example.courses.persistence.entity.Role;
import com.example.courses.persistence.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    UserService userService;

    @Mock
    DAOFactory daoFactory;

    @Mock
    UserDAO userDAO;

    @Mock
    Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        userService = new UserService(daoFactory, userDAO);
        lenient().when(daoFactory.getConnection()).thenReturn(connection);
    }

    @Test
    void testRegisterUser() throws SQLException {
        User user = getUser();
        String password = "password";
        user.setPassword(password);

        when(userDAO.saveUser(connection, user)).thenReturn(1L);

        // User is valid, so saveUser should be called
        long generatedId = userService.registerUser(user);

        assertNotEquals(password, user.getPassword());
        verify(userDAO).saveUser(connection, user);
        assertEquals(1L, generatedId);
    }

    @Test
    void testRegisterUserInvalidUser(){
        User user = getUser();
        user.setEmail("1mail.com");

        // Method "registerUser" throws "IllegalArgumentException" if user is invalid
        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(user));
    }

    @Test
    void testRegisterUserAlreadyExists() throws SQLException {
        User user = getUser();

        User existing = new User.Builder().setEmail("john.doe@mail.com").build();

        when(userDAO.findUser(connection, user.getEmail())).thenReturn(existing);

        // Method "registerUser" throws "IllegalArgumentException" if user with given email already exists
        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(user));
    }

    @Test
    void testSaveUser() throws SQLException {
        User user = getUser();

        when(userDAO.saveUser(connection, user)).thenReturn(1L);

        long generatedId = userService.saveUser(user);

        verify(userDAO).saveUser(connection, user);
        assertEquals(1L, generatedId);

        when(userDAO.saveUser(connection, user)).thenThrow(new SQLException());
        assertThrows(SQLException.class, () -> userService.saveUser(user));
    }

    @Test
    void testDeleteUser() throws SQLException {
        User user = getUser();

        userService.deleteUser(user);
        verify(userDAO).deleteUserById(connection, user.getId());

        doThrow(new SQLException()).when(userDAO).deleteUserById(connection, user.getId());
        assertThrows(SQLException.class, () -> userService.deleteUser(user));
    }

    @Test
    void testUpdateUser() throws SQLException {
        User user = getUser();

        userService.updateUser(user);
        verify(userDAO).updateUser(connection, user);

        doThrow(new SQLException()).when(userDAO).updateUser(connection, user);
        assertThrows(SQLException.class, () -> userService.updateUser(user));
    }

    @Test
    void testGetUserById() throws SQLException {
        long id = 1;

        User user = getUser();
        user.setId(id);

        when(userDAO.findUser(connection, id)).thenReturn(user);

        user = userService.getUserById(id);
        verify(userDAO).findUser(connection, id);
        assertEquals(id, user.getId());

        doThrow(new SQLException()).when(userDAO).findUser(connection, id);
        assertThrows(SQLException.class, () -> userService.getUserById(id));
    }

    @Test
    void testGetUserByEmail() throws SQLException {
        String email = "user@mail.com";

        User user = getUser();
        user.setEmail(email);

        when(userDAO.findUser(connection, email)).thenReturn(user);

        user = userService.getUserByEmail(email);
        verify(userDAO).findUser(connection, email);
        assertEquals(email, user.getEmail());

        doThrow(new SQLException()).when(userDAO).findUser(connection, email);
        assertThrows(SQLException.class, () -> userService.getUserByEmail(email));
    }

    @Test
    void testGetUsersByIds() throws SQLException {
        List<Long> ids = LongStream.range(1, 6).boxed().collect(Collectors.toList());

        List<User> users = userService.getUsers(ids);
        verify(userDAO, times(ids.size())).findUser(any(), anyLong());
        assertEquals(users.size(), ids.size());

        doThrow(new SQLException()).when(userDAO).findUser(any(), anyLong());
        assertThrows(SQLException.class, () -> userService.getUsers(ids));
    }

    @Test
    void testGetUsersByRole() throws SQLException {
        Role role = Role.STUDENT;

        userService.getUsersByRole(role);
        verify(userDAO).findAllByRole(connection, role);

        doThrow(new SQLException()).when(userDAO).findAllByRole(connection, role);
        assertThrows(SQLException.class, () -> userService.getUsersByRole(role));
    }

    @Test
    void testBlockUser() throws SQLException {
        User user = getUser();

        when(userDAO.findUser(connection, user.getId())).thenReturn(user);
        userService.blockUser(user.getId());
        verify(userDAO).updateUser(connection, user);
        assertTrue(user.isBlocked());

        doThrow(new SQLException()).when(userDAO).updateUser(connection, user);
        assertThrows(SQLException.class, () -> userService.blockUser(user.getId()));
    }


    @Test
    void testUnblockUser() throws SQLException {
        User user = getUser();

        when(userDAO.findUser(connection, user.getId())).thenReturn(user);
        userService.unblockUser(user.getId());
        verify(userDAO).updateUser(connection, user);
        assertFalse(user.isBlocked());

        doThrow(new SQLException()).when(userDAO).updateUser(connection, user);
        assertThrows(SQLException.class, () -> userService.blockUser(user.getId()));
    }

    private User getUser() {
        return new User.Builder()
                .setFirstName("John")
                .setLastName("Doe")
                .setEmail("john.doe@mail.com")
                .setPassword("strongPassword")
                .build();
    }
}