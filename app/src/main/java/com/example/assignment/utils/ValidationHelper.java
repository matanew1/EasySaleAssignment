package com.example.assignment.utils;

import android.util.Patterns;

import androidx.annotation.NonNull;

/**
 * Helper class for input validation.
 */
public class ValidationHelper {

    /**
     * Validate the first name field.
     * @param firstName The first name to validate.
     * @return True if the first name is valid, false otherwise.
     */
    public static boolean validateFirstName(@NonNull String firstName) {
        return firstName.trim().isEmpty();
    }

    /**
     * Validate the last name field.
     * @param lastName The last name to validate.
     * @return True if the last name is valid, false otherwise.
     */
    public static boolean validateLastName(@NonNull String lastName) {
        return lastName.trim().isEmpty();
    }

    /**
     * Validate the email field.
     * @param email The email to validate.
     * @return True if the email is valid, false otherwise.
     */
    public static boolean validateEmail(@NonNull String email) {
        return email.trim().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
