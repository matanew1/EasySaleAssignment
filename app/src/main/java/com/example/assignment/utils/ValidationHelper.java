package com.example.assignment.utils;

import android.content.Context;
import android.util.Patterns;

public class ValidationHelper {

    // Validate the first name field
    public static boolean validateFirstName(Context context, String firstName) {
        return !firstName.trim().isEmpty();
    }

    // Validate the last name field
    public static boolean validateLastName(Context context, String lastName) {
        return !lastName.trim().isEmpty();
    }

    // Validate the email field
    public static boolean validateEmail(Context context, String email) {
        return !email.trim().isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
