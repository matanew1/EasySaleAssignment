package com.example.assignment.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

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

    public static void openImagePicker(@NonNull Activity activity, @Nullable ImageSelectionListener listener) {
        String[] options = {"Choose from Gallery", "Enter Image URL"};

        new AlertDialog.Builder(activity)
                .setTitle("Choose an option")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        Intent intent = ImagePickerHelper.createImagePickerIntent();
                        activity.startActivityForResult(intent, ImagePickerHelper.PICK_IMAGE_REQUEST);
                    } else if (which == 1) {
                        showUrlInputDialog(activity, listener);
                    }
                })
                .show();
    }

    private static void showUrlInputDialog(@NonNull Activity activity, @Nullable ImageSelectionListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Enter Image URL");

        final EditText input = new EditText(activity);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String url = input.getText().toString();
            if (listener != null) {
                listener.onImageUrlEntered(url);
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    public static void handleImageResult(@NonNull Activity activity, @Nullable Intent data, @Nullable ImageSelectionListener listener) {
        if (data != null) {
            Uri imageUri = ImagePickerHelper.getImageUri(data);
            if (imageUri != null && listener != null) {
                listener.onImageSelected(imageUri);
            } else {
                Toast.makeText(activity, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public interface ImageSelectionListener {
        void onImageSelected(@NonNull Uri imageUri);
        void onImageUrlEntered(@NonNull String imageUrl);
    }
}
