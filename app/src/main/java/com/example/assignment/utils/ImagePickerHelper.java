package com.example.assignment.utils;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

public class ImagePickerHelper {

    public static final int PICK_IMAGE_REQUEST = 1;

    public static Intent createImagePickerIntent() {
        return new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    }

    public static Uri getImageUri(Intent data) {
        return data != null && data.getData() != null ? data.getData() : null;
    }
}
