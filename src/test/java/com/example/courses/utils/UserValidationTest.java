package com.example.courses.utils;

import com.example.courses.persistence.entity.Role;
import com.example.courses.persistence.entity.User;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserValidationTest {

    @ParameterizedTest
    @NullSource
    void testUserNull(User user){
        assertFalse(UserValidation.isUserValid(user));
    }

    @ParameterizedTest
    @MethodSource("makeInvalidNames")
    void testInvalidName(String name){
        assertFalse(UserValidation.isNameValid(name));
    }

    @ParameterizedTest
    @MethodSource("makeValidNames")
    void testValidName(String name){
        assertTrue(UserValidation.isNameValid(name));
    }

    @ParameterizedTest
    @MethodSource("makeInvalidEmail")
    void testInvalidEmail(String email){
        assertFalse(UserValidation.isEmailValid(email));
    }

    @ParameterizedTest
    @MethodSource("makeValidEmail")
    void testValidEmail(String email){
        assertTrue(UserValidation.isEmailValid(email));
    }

    @ParameterizedTest
    @MethodSource("makeInvalidPassword")
    void testInvalidPassword(String password){
        assertFalse(UserValidation.isPasswordValid(password));
    }

    @ParameterizedTest
    @MethodSource("makeValidPassword")
    void testValidPassword(String password){
        assertTrue(UserValidation.isPasswordValid(password));
    }

    @ParameterizedTest
    @MethodSource("makeInvalidUser")
    void testInvalidUser(User user){
        assertFalse(UserValidation.isUserValid(user));
    }

    @ParameterizedTest
    @MethodSource("makeValidUser")
    void testValidUser(User user){
        assertTrue(UserValidation.isUserValid(user));
    }

    static List<User> makeInvalidUser(){
        return Arrays.asList(
                null,
                new User(14, "", "Doe", "doe@mail.com", "strongPassword", false, "default.jpeg", Role.STUDENT),
                new User(14, "John", "", "doe@mail.com", "strongPassword", false, "default.jpeg", Role.STUDENT),
                new User(14, "John", "Doe", "123doe@mail.com", "strongPassword", false, "default.jpeg", Role.STUDENT),
                new User(14, "John", "Doe", "123doe@mail.com", "12345", false, "default.jpeg", Role.STUDENT),
                new User(14, "John", "Doe", "doe@mailcom", "12345", false, "default.jpeg", Role.STUDENT),
                new User(14, "John", "Doe", "doe@mailcom", null, false, "default.jpeg", Role.STUDENT)
        );
    }

    static List<User> makeValidUser() {
        return Arrays.asList(
                new User(14, "Patten", "Beggin", "pbegginb@tinyurl.com", "pX36BF0U4n", false, "default.jpeg", Role.STUDENT),
                new User(5, "Uriel", "Brickham", "ubrickham9@surveymonkey.com", "QNFnrwRHCbJS", false, "default.jpeg", Role.STUDENT)
        );
    }

    static List<String> makeInvalidNames(){
        return Arrays.asList(null, "", "   ");
    }

    static List<String> makeValidNames(){
        return Arrays.asList("John", " Emily ", "D'Artagnan");
    }

    static List<String> makeInvalidEmail(){
        return Arrays.asList(null, "@.", " name.com ", "j-doe@mail.com", "doe@mail", "doe@mail.", "123name@mail.com");
    }

    static List<String> makeValidEmail(){
        return Arrays.asList("j.doe@mail.com", "peRSon1mail@mail.net", "wierd_name@email.uk");
    }

    static List<String> makeInvalidPassword(){
        return Arrays.asList(null, "123", "  ", "", "hello");
    }

    static List<String> makeValidPassword(){
        return Arrays.asList("password", "PassWord", "213pass", "js;Rmt~4b5~Bd>:j");
    }
}