package com.example.courses.utils;

import org.mindrot.jbcrypt.BCrypt;

public class HashingUtils {

    public static String hashPassword(String passwordProvided) {
        String salt = BCrypt.gensalt();
        return BCrypt.hashpw(passwordProvided, salt);
    }

    public static boolean checkPassword(String providedPassword, String storedHash) {

        return BCrypt.checkpw(providedPassword, storedHash);
    }
}
