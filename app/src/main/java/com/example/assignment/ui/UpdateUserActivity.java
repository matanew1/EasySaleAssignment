package com.example.assignment.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.assignment.R;
import com.example.assignment.db.UserEntity;
import com.example.assignment.utils.ILoadFragment;
import com.example.assignment.utils.ImageLoader;
import com.example.assignment.utils.ImagePickerHelper;
import com.example.assignment.utils.ValidationHelper;
import com.example.assignment.viewmodel.UserViewModel;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Activity for updating a user's information.
 */
public class UpdateUserActivity extends AppCompatActivity implements ILoadFragment, ImagePickerHelper.ImageSelectionListener, ImagePickerHelper.GalleryLauncherProvider {

    private Spinner userSpinner;
    private ImageView avatarImageView;
    private Uri imageUri;
    private UserViewModel userViewModel;
    private TextView uploadTextView;
    private TextInputLayout firstNameLayout;
    private TextInputLayout lastNameLayout;
    private TextInputLayout emailLayout;
    private ProgressBar imageLoadingProgress;
    private UserEntity selectedUser;
    private ActivityResultLauncher<PickVisualMediaRequest> launcher;


    /**
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        // Initialize launcher for gallery selection
        launcher = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), o -> {
            if (o == null) {
                Toast.makeText(UpdateUserActivity.this, "No image Selected", Toast.LENGTH_SHORT).show();
            } else {
                ImageLoader.loadImage(this, o, avatarImageView);
                imageUri = o;
                uploadTextView.setVisibility(TextView.GONE);
            }
        });

        initializeViews();
        setupViewModel();
        populateUserSpinner();
        setListeners();

        // If the activity is being created for the first time, load the MenuFragment
        if (savedInstanceState == null) {
            loadFragment(new MenuFragment());
        }
    }

    /**
     * Initializes the views in the activity.
     */
    private void initializeViews() {
        userSpinner = findViewById(R.id.user_spinner);
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
     * Populates the user spinner with the list of users from the database.
     */
    private void populateUserSpinner() {
        LiveData<List<UserEntity>> usersLiveData = userViewModel.getAllUsers();
        usersLiveData.observe(this, this::updateSpinnerAdapter);
    }

    /**
     * Updates the user spinner adapter with the list of users.
     * @param users The list of users to display in the spinner.
     */
    private void updateSpinnerAdapter(@NonNull List<UserEntity> users) {
        List<String> userNames = users.stream()
                .map(UserEntity::getFirstName)
                .collect(Collectors.toList());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, userNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userSpinner.setAdapter(adapter);
    }

    /**
     * Sets up the listeners for the activity.
     */
    private void setListeners() {
        // Handle spinner item selection
        userSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onUserSelected(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        avatarImageView.setOnClickListener(v -> ImagePickerHelper.openImagePicker(this, this));

        Button submitButton = findViewById(R.id.btn_submit);
        submitButton.setOnClickListener(v -> validateAndSubmitUser());

        Button cancelButton = findViewById(R.id.btn_cancel);
        cancelButton.setOnClickListener(v -> finish());
    }

    /**
     * Handles the selection of a user from the spinner.
     * @param position The position of the selected user in the spinner.
     */
    private void onUserSelected(int position) {
        List<UserEntity> users = userViewModel.getAllUsers().getValue();

        if (users == null || users.isEmpty() || position < 0 || position >= users.size()) {
            return;
        }

        selectedUser = users.get(position);
        updateUserUI(selectedUser);
    }

    /**
     * Updates the UI with the selected user's information.
     * @param user The selected user to update the UI with.
     */
    private void updateUserUI(@NonNull UserEntity user) {
        loadAvatar(user.getAvatar());
        setTextToLayout(firstNameLayout, user.getFirstName());
        setTextToLayout(lastNameLayout, user.getLastName());
        setTextToLayout(emailLayout, user.getEmail());
    }

    /**
     * Loads the avatar image for the selected user.
     * @param avatarUrl The URL of the avatar image to load.
     */
    private void loadAvatar(String avatarUrl) {
        if (avatarUrl != null && !avatarUrl.isEmpty()) {
            imageLoadingProgress.setVisibility(View.VISIBLE);
            ImageLoader.loadImage(this, Uri.parse(avatarUrl), avatarImageView);
            imageLoadingProgress.setVisibility(View.GONE);
        }
    }

    /**
     * Sets the text to the given layout.
     * @param layout The TextInputLayout to set the text to.
     * @param text The text to set.
     */
    private void setTextToLayout(@NonNull TextInputLayout layout, String text) {
        if (layout.getEditText() != null) {
            layout.getEditText().setText(text != null ? text : "");
        }
    }

    /**
     * Handles the image selection from the gallery.
     * @param imageUrl The URL of the selected image.
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
     * Handles errors during image selection.
     * @param errorMessage The error message to display.
     */
    @Override
    public void onError(@NonNull String errorMessage) {

    }

    /**
     * Validates and submits the user's information for update.
     */
    private void validateAndSubmitUser() {
        String firstName = getTextFromLayout(firstNameLayout);
        String lastName = getTextFromLayout(lastNameLayout);
        String email = getTextFromLayout(emailLayout);

        clearErrors();

        boolean isValid = validateInput(firstName, lastName, email);

        if (isValid) {
            updateUserEntity(firstName, lastName, email, imageUri.toString());
            userViewModel.updateUser(selectedUser);
            Toast.makeText(this, "User updated successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, GetAllUsersActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Gets the text from the given TextInputLayout.
     * @param layout The TextInputLayout to get the text from.
     * @return The text from the TextInputLayout.
     */
    @NonNull
    private String getTextFromLayout(@NonNull TextInputLayout layout) {
        return layout.getEditText() != null ? layout.getEditText().getText().toString() : "";
    }

    /**
     * Clears the errors from the TextInputLayouts.
     */
    private void clearErrors() {
        firstNameLayout.setError(null);
        lastNameLayout.setError(null);
        emailLayout.setError(null);
    }

    /**
     * Validates the input fields.
     * @param firstName The first name to validate.
     * @param lastName The last name to validate.
     * @param email The email to validate.
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
     * Updates the selected user's entity with the given information.
     * @param firstName The first name to validate.
     * @param lastName The last name to validate.
     * @param email The email to validate.
     * @param avatarUrl The URL of the avatar image to set.
     */
    private void updateUserEntity(String firstName, String lastName, String email, String avatarUrl) {
        selectedUser.setFirstName(firstName);
        selectedUser.setLastName(lastName);
        selectedUser.setEmail(email);
        selectedUser.setAvatar(avatarUrl);
    }

    /**
     * Loads a fragment into the action menu view.
     * @param fragment The fragment to load.
     */
    @Override
    public void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.action_menu_view, fragment)
                .commit();
    }

    /**
     * Gets the launcher for the gallery selection.
     * @return The launcher for the gallery selection.
     */
    @NonNull
    @Override
    public ActivityResultLauncher<PickVisualMediaRequest> getGalleryLauncher() {
        return launcher;
    }
}
