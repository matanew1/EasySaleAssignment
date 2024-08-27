package com.example.assignment.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.assignment.R;
import com.example.assignment.db.UserEntity;
import com.example.assignment.utils.ILoadFragment;
import com.example.assignment.utils.ImageLoader;
import com.example.assignment.utils.ImagePickerHelper;
import com.example.assignment.utils.ValidationHelper;
import com.example.assignment.viewmodel.UserViewModel;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Date;

public class UpdateUserActivity extends AppCompatActivity implements ILoadFragment, ImagePickerHelper.ImageSelectionListener, ImagePickerHelper.GalleryLauncherProvider {

    private ImageView avatarImageView;
    private Uri imageUri;
    private UserViewModel userViewModel;
    private TextView uploadTextView;
    private TextInputLayout firstNameLayout;
    private TextInputLayout lastNameLayout;
    private TextInputLayout emailLayout;
    private ProgressBar imageLoadingProgress;
    private ActivityResultLauncher<PickVisualMediaRequest> launcher;
    private UserEntity currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        // Initialize launcher for gallery selection
        launcher = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri == null) {
                Toast.makeText(UpdateUserActivity.this, "No image Selected", Toast.LENGTH_SHORT).show();
            } else {
                ImageLoader.loadImage(this, uri, avatarImageView);
                imageUri = uri;
                uploadTextView.setVisibility(TextView.GONE);
            }
        });

        initializeViews();
        setupViewModel();
        setListeners();

        if (savedInstanceState == null) {
            loadFragment(new MenuFragment());
        }

        Intent intent = getIntent();
        currentUser = (UserEntity) intent.getSerializableExtra("user");

        if (currentUser == null) {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        updateUserUI(currentUser);
    }

    private void initializeViews() {
        avatarImageView = findViewById(R.id.avatar_image);
        uploadTextView = findViewById(R.id.upload_text);
        imageLoadingProgress = findViewById(R.id.image_loading_progress);
        firstNameLayout = findViewById(R.id.first_name_layout);
        lastNameLayout = findViewById(R.id.last_name_layout);
        emailLayout = findViewById(R.id.email_layout);
    }

    private void setupViewModel() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    private void setListeners() {
        avatarImageView.setOnClickListener(v -> ImagePickerHelper.openImagePicker(this, this));

        Button submitButton = findViewById(R.id.btn_submit);
        submitButton.setOnClickListener(v -> validateAndSubmitUser());

        Button cancelButton = findViewById(R.id.btn_cancel);
        cancelButton.setOnClickListener(v -> finish());

        Button deleteButton = findViewById(R.id.btn_delete);
        deleteButton.setOnClickListener(v -> deleteUser());
    }

    private void deleteUser() {
        userViewModel.deleteUser(currentUser);
        Toast.makeText(this, "User deleted successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, GetAllUsersActivity.class);
        startActivity(intent);
    }

    private void updateUserUI(@NonNull UserEntity user) {
        loadAvatar(user.getAvatar());
        setTextToLayout(firstNameLayout, user.getFirstName());
        setTextToLayout(lastNameLayout, user.getLastName());
        setTextToLayout(emailLayout, user.getEmail());
    }

    private void loadAvatar(String avatarUrl) {
        if (avatarUrl != null && !avatarUrl.isEmpty()) {
            imageLoadingProgress.setVisibility(View.VISIBLE);
            ImageLoader.loadImage(this, Uri.parse(avatarUrl), avatarImageView);
            imageLoadingProgress.setVisibility(View.GONE);
        }
    }

    private void setTextToLayout(@NonNull TextInputLayout layout, String text) {
        if (layout.getEditText() != null) {
            layout.getEditText().setText(text != null ? text : "");
        }
    }

    @Override
    public void onImageUrlEntered(@NonNull String imageUrl) {
        if (!imageUrl.isEmpty() && Patterns.WEB_URL.matcher(imageUrl).matches()) {
            imageLoadingProgress.setVisibility(ProgressBar.VISIBLE);
            imageUri = Uri.parse(imageUrl);
            ImageLoader.loadImage(this, imageUri, avatarImageView);
            uploadTextView.setVisibility(TextView.GONE);
            imageLoadingProgress.setVisibility(ProgressBar.GONE);
        } else {
            Toast.makeText(this, "Invalid URL", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onError(@NonNull String errorMessage) {
        Log.e("UpdateUserActivity", "Error: " + errorMessage);
    }

    private void validateAndSubmitUser() {
        String firstName = getTextFromLayout(firstNameLayout);
        String lastName = getTextFromLayout(lastNameLayout);
        String email = getTextFromLayout(emailLayout);

        // Use existing avatar URL if no new image is selected
        String avatarUrl = imageUri != null ? imageUri.toString() : currentUser.getAvatar();

        clearErrors();

        boolean isValid = validateInput(firstName, lastName, email);

        if (isValid) {
            updateUserEntity(firstName, lastName, email, avatarUrl);
            Log.d("UpdateUserActivity", "Updating user: " + currentUser.toString());
            userViewModel.updateUser(currentUser);
            Toast.makeText(this, "User updated successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, GetAllUsersActivity.class);
            startActivity(intent);
        }
    }


    @NonNull
    private String getTextFromLayout(@NonNull TextInputLayout layout) {
        return layout.getEditText() != null ? layout.getEditText().getText().toString() : "";
    }

    private void clearErrors() {
        firstNameLayout.setError(null);
        lastNameLayout.setError(null);
        emailLayout.setError(null);
    }

    private boolean validateInput(String firstName, String lastName, String email) {
        boolean isValid = true;

        if (ValidationHelper.validateFirstName(firstName)) {
            firstNameLayout.setError("First name is required");
            isValid = false;
        }

        if (ValidationHelper.validateLastName(lastName)) {
            lastNameLayout.setError("Last name is required");
            isValid = false;
        }

        if (ValidationHelper.validateEmail(email)) {
            emailLayout.setError("Valid email is required");
            isValid = false;
        }

        return isValid;
    }

    private void updateUserEntity(String firstName, String lastName, String email, String avatarUrl) {
        currentUser.setFirstName(firstName);
        currentUser.setLastName(lastName);
        currentUser.setEmail(email);
        currentUser.setAvatar(avatarUrl);
    }

    @Override
    public void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.action_menu_view, fragment)
                .commit();
    }

    @NonNull
    @Override
    public ActivityResultLauncher<PickVisualMediaRequest> getGalleryLauncher() {
        return launcher;
    }
}
