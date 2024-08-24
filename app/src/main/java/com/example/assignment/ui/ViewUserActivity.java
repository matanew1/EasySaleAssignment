package com.example.assignment.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.assignment.R;
import com.example.assignment.db.UserEntity;
import com.example.assignment.utils.ILoadFragment;

import java.util.Objects;

public class ViewUserActivity extends AppCompatActivity  implements ILoadFragment {

    private ImageView avatarImageView;
    private TextView firstNameTextView;
    private TextView lastNameTextView;
    private TextView emailTextView;
    private TextView titleTextView;
    private UserEntity user;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user);

        Intent intent = getIntent();
        user = (UserEntity) intent.getSerializableExtra("user");

        // initialize views
        initViews();

        // load user data
        loadUserData();

        // Load menu fragment
        if (savedInstanceState == null) {
            loadFragment(new MenuFragment());
        }

    }

    @SuppressLint("SetTextI18n")
    private void loadUserData() {
        // load avatar image
        Glide.with(this)
                .load(Objects.requireNonNull(user).getAvatar())
                .into(this.avatarImageView);

        // load first name
        this.firstNameTextView.setText(user.getFirstName());

        // load last name
        this.lastNameTextView.setText(user.getLastName());

        // load email
        this.emailTextView.setText(user.getEmail());

        // set title
        this.titleTextView.setText(user.getFirstName() + " " + user.getLastName());

    }

    private void initViews() {
        this.avatarImageView = findViewById(R.id.avatar_image);
        this.firstNameTextView = findViewById(R.id.first_name_field);
        this.lastNameTextView = findViewById(R.id.last_name_field);
        this.emailTextView = findViewById(R.id.email_field);
        this.titleTextView = findViewById(R.id.title_text);
    }

    @Override
    public void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.bottom_action_menu, fragment)
                .commit();
    }
}
