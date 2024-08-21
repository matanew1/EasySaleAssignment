package com.example.assignment.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.assignment.R;
import com.example.assignment.db.UserEntity;
import com.example.assignment.utils.ImageLoader;
import com.example.assignment.utils.ImagePickerHelper;
import com.example.assignment.utils.ValidationHelper;
import com.example.assignment.viewmodel.UserViewModel;
import com.google.android.material.textfield.TextInputLayout;

public class AddUserActivity extends AppCompatActivity {

    private ImageView avatarImageView;
    private Uri imageUri;
    private UserViewModel userViewModel;
    private TextView uploadTextView;
    private TextInputLayout firstNameLayout;
    private TextInputLayout lastNameLayout;
    private TextInputLayout emailLayout;
    private ProgressBar imageLoadingProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        // Initialize UI components
        EditText firstNameEditText = findViewById(R.id.first_name_et);
        EditText lastNameEditText = findViewById(R.id.last_name_et);
        EditText emailEditText = findViewById(R.id.email_et);
        Button submitButton = findViewById(R.id.btn_submit);
        avatarImageView = findViewById(R.id.avatar_image);
        uploadTextView = findViewById(R.id.upload_text);
        imageLoadingProgress = findViewById(R.id.image_loading_progress);

        firstNameLayout = findViewById(R.id.first_name_layout);
        lastNameLayout = findViewById(R.id.last_name_layout);
        emailLayout = findViewById(R.id.email_layout);

        // Initialize UserViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Set up click listener for the ImageView to open the image picker
        avatarImageView.setOnClickListener(v -> openImagePicker());

        submitButton.setOnClickListener(v -> {
            String firstName = firstNameEditText.getText().toString();
            String lastName = lastNameEditText.getText().toString();
            String email = emailEditText.getText().toString();

            // Reset errors
            firstNameLayout.setError(null);
            lastNameLayout.setError(null);
            emailLayout.setError(null);

            boolean isValid = true;

            if (!ValidationHelper.validateFirstName(this, firstName)) {
                firstNameLayout.setError("First name is required");
                isValid = false;
            }
            if (!ValidationHelper.validateLastName(this, lastName)) {
                lastNameLayout.setError("Last name is required");
                isValid = false;
            }
            if (!ValidationHelper.validateEmail(this, email)) {
                emailLayout.setError("Valid email is required");
                isValid = false;
            }

            if (isValid) {
                UserEntity user = new UserEntity();
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setEmail(email);

                if (imageUri != null) {
                    user.setAvatar(imageUri.toString());
                }

                userViewModel.addNewUser(user);
                Toast.makeText(this, "User added successfully", Toast.LENGTH_SHORT).show();
                finish(); // Close the activity
            }
        });
    }

    private void openImagePicker() {
        Intent intent = ImagePickerHelper.createImagePickerIntent();
        startActivityForResult(intent, ImagePickerHelper.PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ImagePickerHelper.PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            imageLoadingProgress.setVisibility(ProgressBar.VISIBLE);
            imageUri = ImagePickerHelper.getImageUri(data);
            if (imageUri != null) {
                uploadTextView.setVisibility(TextView.GONE); // Hide the hint text
                ImageLoader.loadImage(this, imageUri, avatarImageView);
            } else {
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
            imageLoadingProgress.setVisibility(ProgressBar.GONE);
        }
    }
}
