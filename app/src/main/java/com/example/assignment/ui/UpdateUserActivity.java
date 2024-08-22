package com.example.assignment.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class UpdateUserActivity extends AppCompatActivity implements ILoadFragment {

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
        setSpinnerListeners();

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

    private void setSpinnerListeners() {
        userSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<UserEntity> users = userViewModel.getAllUsers().getValue();

                if (users == null || users.isEmpty() || position < 0 || position >= users.size()) {
                    // Handle edge cases where users list might be null or invalid position
                    return;
                }

                selectedUser = users.get(position);

                if (selectedUser == null) {
                    // Handle case where selected user is null (though unlikely if list is valid)
                    return;
                }

                // Update the UI with the selected user's details
                updateUserUI(selectedUser);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle no item selected
            }
        });
        avatarImageView.setOnClickListener(v -> openImagePicker());

        Button submitButton = findViewById(R.id.btn_submit);
        submitButton.setOnClickListener(v -> validateAndSubmitUser());
    }

    private void updateUserUI(@NonNull UserEntity user) {
        if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
            Uri avatarUri = Uri.parse(user.getAvatar());
            avatarImageView.setImageURI(avatarUri);
            Glide.with(UpdateUserActivity.this)
                    .load(avatarUri)
                    .into(avatarImageView);
        }

        // Set other fields
        setTextToLayout(firstNameLayout, user.getFirstName());
        setTextToLayout(lastNameLayout, user.getLastName());
        setTextToLayout(emailLayout, user.getEmail());
    }

    private void setTextToLayout(@NonNull TextInputLayout layout, String text) {
        if (layout.getEditText() != null) {
            layout.getEditText().setText(text != null ? text : "");
        }
    }

    private void openImagePicker() {
        Intent intent = ImagePickerHelper.createImagePickerIntent();
        startActivityForResult(intent, ImagePickerHelper.PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ImagePickerHelper.PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            handleImageResult(data);
        }
    }

    private void handleImageResult(@Nullable Intent data) {
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

    private void validateAndSubmitUser() {
        EditText firstNameEditText = findViewById(R.id.first_name_et);
        EditText lastNameEditText = findViewById(R.id.last_name_et);
        EditText emailEditText = findViewById(R.id.email_et);

        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String email = emailEditText.getText().toString();

        clearErrors();

        boolean isValid = validateInput(firstName, lastName, email);

        if (isValid) {
            selectedUser.setFirstName(firstName);
            selectedUser.setLastName(lastName);
            selectedUser.setEmail(email);
            if (imageUri != null) {
                selectedUser.setAvatar(imageUri.toString());
            }
            userViewModel.updateUser(selectedUser);
            Toast.makeText(this, "User added successfully", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity
        }
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
}
