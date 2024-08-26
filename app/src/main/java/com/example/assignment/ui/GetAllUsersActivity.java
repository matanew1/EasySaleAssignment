package com.example.assignment.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment.R;
import com.example.assignment.db.UserEntity;
import com.example.assignment.utils.ILoadFragment;
import com.example.assignment.viewmodel.UserViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Activity to display all users.
 */
public class GetAllUsersActivity extends AppCompatActivity implements ILoadFragment {
    private UserViewModel userViewModel;
    private UserAdapter adapter;
    private List<UserEntity> allUsers = new ArrayList<>();

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
        setContentView(R.layout.activity_get_all_users);

        initializeViewModel();
        setupRecyclerView();
        setupSearch();

        if (savedInstanceState == null) {
            loadFragment(new MenuFragment());
        }
    }

    /**
     * Initializes the ViewModel for handling user data.
     */
    private void initializeViewModel() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

         // Observe changes in user data
        userViewModel.getAllUsers().observe(this, users -> {
            if (users != null) {
                allUsers = users; // Update local list
                adapter.setUsers(allUsers); // Update adapter
            }
        });
    }

    /**
     * Sets up the RecyclerView for displaying user data.
     */
    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new UserAdapter();
        recyclerView.setAdapter(adapter);
        setupAdapterListener();
    }

    /**
     * Sets up the adapter listener for handling user clicks.
     */
    private void setupAdapterListener() {
        adapter.setItemClickListener(position -> {
            UserEntity user = adapter.getUserAtPosition(position);
            Intent intent = new Intent(this, UpdateUserActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        });
    }

    /**
     * Sets up the search functionality for filtering user data.
     */
    private void setupSearch() {
        TextInputEditText searchBar = findViewById(R.id.search_bar);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterUsers(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    /**
     * Filters the user data based on the given query.
     * @param query The search query.
     */
    private void filterUsers(String query) {
        List<UserEntity> filteredUsers = allUsers.stream()
                .filter(user -> user.getFirstName().toLowerCase().contains(query.toLowerCase()) ||
                        user.getLastName().toLowerCase().contains(query.toLowerCase()) ||
                        user.getEmail().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
        adapter.setUsers(filteredUsers);
    }

    /**
     * Loads a fragment into the specified container.
     * @param fragment The fragment to load.
     */
    @Override
    public void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.action_menu_view, fragment)
                .commit();
    }
}
