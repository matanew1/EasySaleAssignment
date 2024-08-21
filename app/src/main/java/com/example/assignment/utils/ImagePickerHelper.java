package com.example.assignment.utils;

import android.content.Intent;
import android.net.Uri;

public class ImagePickerHelper {
    public static final int PICK_IMAGE_REQUEST = 1;

    public static Intent createImagePickerIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        return intent;
    }

    public static Uri getImageUri(Intent data) {
        if (data != null) {
            return data.getData();
        }
        return null;
    }
}
