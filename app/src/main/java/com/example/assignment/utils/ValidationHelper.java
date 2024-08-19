package com.example.assignment.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

public class ValidationHelper {

    public static boolean validateInputs(Context context,String firstName, String lastName, String email) {
        if (TextUtils.isEmpty(firstName)) {
            showToast(context,"First name is required");
            return false;
        }

        if (TextUtils.isEmpty(lastName)) {
            showToast(context,"Last name is required");
            return false;
        }

        if (TextUtils.isEmpty(email)) {
            showToast(context,"Email is required");
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast(context,"Invalid email address");
            return false;
        }

        return true;
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
