package com.example.assignment.ui;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
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

public class UpdateUserActivity extends AppCompatActivity implements ILoadFragment, ImagePickerHelper.ImageSelectionListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        initializeViews();
        setupViewModel();
        populateUserSpinner();
        setListeners();

        if (savedInstanceState == null) {
            loadFragment(new MenuFragment());
        }
    }

    private void initializeViews() {
        userSpinner = findViewById(R.id.user_spinner);
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

    private void populateUserSpinner() {
        LiveData<List<UserEntity>> usersLiveData = userViewModel.getAllUsers();
        usersLiveData.observe(this, this::updateSpinnerAdapter);
    }

    private void updateSpinnerAdapter(@NonNull List<UserEntity> users) {
        List<String> userNames = users.stream()
                .map(UserEntity::getFirstName)
                .collect(Collectors.toList());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, userNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userSpinner.setAdapter(adapter);
    }

    private void setListeners() {
        userSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onUserSelected(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle no item selected
            }
        });

        avatarImageView.setOnClickListener(v -> ImagePickerHelper.openImagePicker(UpdateUserActivity.this, UpdateUserActivity.this));

        Button submitButton = findViewById(R.id.btn_submit);
        submitButton.setOnClickListener(v -> validateAndSubmitUser());
    }

    private void onUserSelected(int position) {
        List<UserEntity> users = userViewModel.getAllUsers().getValue();

        if (users == null || users.isEmpty() || position < 0 || position >= users.size()) {
            return;
        }

        selectedUser = users.get(position);
        updateUserUI(selectedUser);
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
            Glide.with(this)
                    .load(avatarUrl)
                    .into(avatarImageView);
            imageLoadingProgress.setVisibility(View.GONE);
        }
    }

    private void setTextToLayout(@NonNull TextInputLayout layout, String text) {
        if (layout.getEditText() != null) {
            layout.getEditText().setText(text != null ? text : "");
        }
    }

    @Override
    public void onImageSelected(@NonNull Uri uri) {
        imageUri = uri;
        uploadTextView.setVisibility(View.GONE); // Hide the hint text
        ImageLoader.loadImage(this, imageUri, avatarImageView);
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
            finish(); // Close the activity
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

        return isValid;
    }

    private void updateUserEntity(String firstName, String lastName, String email, String avatarUrl) {
        selectedUser.setFirstName(firstName);
        selectedUser.setLastName(lastName);
        selectedUser.setEmail(email);
        selectedUser.setAvatar(avatarUrl);
    }

    @Override
    public void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.action_menu_view, fragment)
                .commit();
    }
}
