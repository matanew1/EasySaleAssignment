package com.example.assignment.utils;

import android.app.Activity;
import android.text.InputType;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.assignment.ui.AddUserActivity;

public class ImagePickerHelper {

    /**
     * Opens a dialog for the user to choose how to select an image (e.g., via URL input).
     *
     * @param activity Activity context.
     * @param listener Listener for handling image selection events.
     */
    public static void openImagePicker(@NonNull Activity activity, @Nullable ImageSelectionListener listener) {
        String[] options = {"Enter Web Image URL", "Upload Image From Gallery"};

        new AlertDialog.Builder(activity)
                .setTitle("Choose an option")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        showUrlInputDialog(activity, listener);
                    } else if (which == 1) {
                        openGallery(activity, listener);
                    }
                })
                .show();
    }

    private static void openGallery(@NonNull Activity activity, @Nullable ImageSelectionListener listener) {
        AddUserActivity addUserActivity = (AddUserActivity) activity;
        ActivityResultLauncher<PickVisualMediaRequest> launcher = addUserActivity.getLauncher();
        launcher.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
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
     * Listener interface for handling image selection and URL input events.
     */
    public interface ImageSelectionListener {
        void onImageUrlEntered(@NonNull String imageUrl);
        void onImageUploaded(@NonNull String imageUri);
    }
}
