package com.example.assignment.utils;

import android.annotation.SuppressLint;
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

    /**
     * Creates an intent to pick an image from the gallery.
     *
     * @return Intent to pick an image.
     */
    @NonNull
    public static Intent createImagePickerIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        return intent;
    }

    /**
     * Retrieves the URI of the selected image.
     *
     * @param data Intent containing the result data.
     * @return URI of the selected image, or null if none found.
     */
    @Nullable
    public static Uri getImageUri(@Nullable Intent data) {
        return data != null ? data.getData() : null;
    }

    /**
     * Opens a dialog for the user to choose how to select an image (e.g., via URL input).
     *
     * @param activity Activity context.
     * @param listener Listener for handling image selection events.
     */
    public static void openImagePicker(@NonNull Activity activity, @Nullable ImageSelectionListener listener) {
        String[] options = {"Enter Image URL"};

        new AlertDialog.Builder(activity)
                .setTitle("Choose an option")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        showUrlInputDialog(activity, listener);
                    }
                })
                .show();
    }

    /**
     * Displays an input dialog to enter an image URL.
     *
     * @param activity Activity context.
     * @param listener Listener for handling URL input events.
     */
    private static void showUrlInputDialog(@NonNull Activity activity, @Nullable ImageSelectionListener listener) {
        EditText input = new EditText(activity);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);

        new AlertDialog.Builder(activity)
                .setTitle("Enter Image URL")
                .setView(input)
                .setPositiveButton("OK", (dialog, which) -> {
                    if (listener != null) {
                        listener.onImageUrlEntered(input.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                .show();
    }

    /**
     * Handles the result of an image selection action.
     *
     * @param activity Activity context.
     * @param data Intent containing the result data.
     * @param listener Listener for handling image selection events.
     */
    public static void handleImageResult(@NonNull Activity activity, @Nullable Intent data, @Nullable ImageSelectionListener listener) {
        Uri imageUri = getImageUri(data);
        if (imageUri != null && listener != null) {
            listener.onImageSelected(imageUri);
        } else {
            Toast.makeText(activity, "Failed to load image", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Listener interface for handling image selection and URL input events.
     */
    public interface ImageSelectionListener {
        void onImageSelected(@NonNull Uri imageUri);
        void onImageUrlEntered(@NonNull String imageUrl);
    }
}
