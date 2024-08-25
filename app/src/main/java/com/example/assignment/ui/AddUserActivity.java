package com.example.assignment.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
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

public class AddUserActivity extends AppCompatActivity implements ILoadFragment, ImagePickerHelper.ImageSelectionListener, ImagePickerHelper.GalleryLauncherProvider {

    private ImageView avatarImageView;
    private Uri imageUri;
    private UserViewModel userViewModel;
    private TextView uploadTextView;
    private TextInputLayout firstNameLayout;
    private TextInputLayout lastNameLayout;
    private TextInputLayout emailLayout;
    private ProgressBar imageLoadingProgress;
    private ActivityResultLauncher<PickVisualMediaRequest> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        launcher = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), o -> {
            if (o == null) {
                Toast.makeText(AddUserActivity.this, "No image Selected", Toast.LENGTH_SHORT).show();
            } else {
                ImageLoader.loadImage(this, o, avatarImageView);
                imageUri = o;
                uploadTextView.setVisibility(TextView.GONE);
            }
        });

        initializeViews();
        setupViewModel();
        setupListeners();

        if (savedInstanceState == null) {
            loadFragment(new MenuFragment());
        }
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

    private void setupListeners() {
        avatarImageView.setOnClickListener(v -> ImagePickerHelper.openImagePicker(this, this));

        Button submitButton = findViewById(R.id.btn_submit);
        submitButton.setOnClickListener(v -> validateAndSubmitUser());

        Button cancelButton = findViewById(R.id.btn_cancel);
        cancelButton.setOnClickListener(v -> finish());
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

    }


    private void validateAndSubmitUser() {
        String firstName = ((EditText) findViewById(R.id.first_name_et)).getText().toString();
        String lastName = ((EditText) findViewById(R.id.last_name_et)).getText().toString();
        String email = ((EditText) findViewById(R.id.email_et)).getText().toString();

        clearErrors();

        boolean isValid = validateInput(firstName, lastName, email);

        if (isValid) {
            UserEntity user = createUserEntity(firstName, lastName, email);
            userViewModel.addNewUser(user);
            Toast.makeText(this, "User added successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, GetAllUsersActivity.class);
            startActivity(intent);
        }
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

    @NonNull
    private UserEntity createUserEntity(String firstName, String lastName, String email) {
        UserEntity user = new UserEntity();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);

        if (imageUri != null) {
            user.setAvatar(imageUri.toString());
        }

        return user;
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
