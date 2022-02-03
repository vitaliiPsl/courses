package com.example.courses.utils;

import com.example.courses.persistence.entity.User;

public class UserValidation {
    static final int MIN_PASSWORD_LENGTH = 6;
    static final String EMAIL_REGEX = "[a-zA-Z]+[\\w.]+@[\\w.]+\\.[a-zA-Z]{2,6}";

    private UserValidation(){}

    public static boolean isUserValid(User user){
        return user != null
                && isNameValid(user.getFirstName())
                && isNameValid(user.getLastName())
                && isEmailValid(user.getEmail())
                && isPasswordValid(user.getPassword());
    }

    public static boolean isNameValid(String name){
        return name != null && !name.trim().isEmpty();
    }

    public static boolean isEmailValid(String email){
        return email != null && email.matches(EMAIL_REGEX);
    }

    public static boolean isPasswordValid(String password){
        return password != null
                && !password.trim().isEmpty()
                && password.length() >= MIN_PASSWORD_LENGTH;
    }
}
