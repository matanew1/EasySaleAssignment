package com.example.assignment.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class AddUserActivity extends AppCompatActivity {

    private ImageView avatarImageView;
    private Uri imageUri;
    private UserViewModel userViewModel;

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

        // Initialize UserViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Set up click listener for the ImageView to open the image picker
        avatarImageView.setOnClickListener(v -> openImagePicker());

        submitButton.setOnClickListener(v -> {
            String firstName = firstNameEditText.getText().toString();
            String lastName = lastNameEditText.getText().toString();
            String email = emailEditText.getText().toString();

            if (ValidationHelper.validateInputs(this, firstName, lastName, email)) {
                UserEntity user = new UserEntity();
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setEmail(email);

                if (imageUri != null) {
                    user.setAvatar(imageUri.toString());
                }

                userViewModel.insert(user);
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
            imageUri = ImagePickerHelper.getImageUri(data);
            Bitmap bitmap = ImageLoader.loadBitmapFromUri(this, imageUri);
            if (bitmap != null) {
                avatarImageView.setImageBitmap(bitmap);
            } else {
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
