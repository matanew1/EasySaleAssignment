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

/**
 * This class represents the activity for adding a new user.
 */
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

    /**
     * Called when the activity is first created.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
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

    /**
     * Initializes the views in the activity.
     */
    private void initializeViews() {
        avatarImageView = findViewById(R.id.avatar_image);
        uploadTextView = findViewById(R.id.upload_text);
        imageLoadingProgress = findViewById(R.id.image_loading_progress);
        firstNameLayout = findViewById(R.id.first_name_layout);
        lastNameLayout = findViewById(R.id.last_name_layout);
        emailLayout = findViewById(R.id.email_layout);
    }

    /**
     * Sets up the ViewModel for the activity.
     */
    private void setupViewModel() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    /**
     * Sets up the listeners for the activity.
     */
    private void setupListeners() {
        avatarImageView.setOnClickListener(v -> ImagePickerHelper.openImagePicker(this, this));

        Button submitButton = findViewById(R.id.btn_submit);
        submitButton.setOnClickListener(v -> validateAndSubmitUser());

        Button cancelButton = findViewById(R.id.btn_cancel);
        cancelButton.setOnClickListener(v -> finish());
    }

    /**
     * Called when an image URL is entered.
     * @param imageUrl The URL of the image.
     */
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

    /**
     * Called when an error occurs during image selection.
     * @param errorMessage The error message.
     */
    @Override
    public void onError(@NonNull String errorMessage) {

    }

    /**
     * Validates and submits the user data.
     */
    private void validateAndSubmitUser() {
        String firstName = ((EditText) findViewById(R.id.first_name_et)).getText().toString();
        String lastName = ((EditText) findViewById(R.id.last_name_et)).getText().toString();
        String email = ((EditText) findViewById(R.id.email_et)).getText().toString();

        clearErrors();

        boolean isValid = validateInput(firstName, lastName, email);

        if (isValid) {
            userViewModel.getUserByEmail(email).observe(this, user -> {
                if (user != null) {
                    Toast.makeText(this, "User with this email already exists", Toast.LENGTH_SHORT).show();
                } else {
                    UserEntity newUser = createUserEntity(firstName, lastName, email);
                    userViewModel.addNewUser(newUser);
                    Toast.makeText(this, "User added successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, GetAllUsersActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    /**
     * Clears the error messages in the input fields.
     */
    private void clearErrors() {
        firstNameLayout.setError(null);
        lastNameLayout.setError(null);
        emailLayout.setError(null);
    }

    /**
     * Validates the input fields.
     * @param firstName The first name.
     * @param lastName The last name.
     * @param email The email.
     * @return True if the input is valid, false otherwise.
     */
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

    /**
     * Creates a new UserEntity object with the given first name, last name, and email.
     * @param firstName The first name.
     * @param lastName The last name.
     * @param email The email.
     * @return A new UserEntity object.
     */
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

    /**
     * Loads a fragment into the activity.
     * @param fragment The fragment to load.
     */
    @Override
    public void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.action_menu_view, fragment)
                .commit();
    }

    /**
     * Returns the launcher for image selection.
     * @return The launcher for image selection.
     */
    @NonNull
    @Override
    public ActivityResultLauncher<PickVisualMediaRequest> getGalleryLauncher() {
        return launcher;
    }
}
