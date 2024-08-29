package com.example.assignment.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment.R;
import com.example.assignment.db.UserEntity;
import com.example.assignment.utils.ILoadFragment;
import com.example.assignment.viewmodel.UserViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GetAllUsersActivity extends AppCompatActivity implements ILoadFragment {
    private UserViewModel userViewModel;
    private UserAdapter adapter;
    private List<UserEntity> allUsers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_all_users);

        setupRefreshButton();
        initializeViewModel();
        setupRecyclerView();
        setupSearch();

        if (savedInstanceState == null) {
            loadFragment(new MenuFragment());
        }
    }

    private void setupRefreshButton() {
        FloatingActionButton refreshButton = findViewById(R.id.refresh_button);
        refreshButton.setOnClickListener(v -> userViewModel.fetchUsersFromApiIgnoreDeleted());
    }

    private void initializeViewModel() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        userViewModel.getAllUsers().observe(this, users -> {
            if (users != null) {
                allUsers = users;
                adapter.setUsers(allUsers);
            }
        });
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new UserAdapter();
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                if (userViewModel.hasMoreData() && lastVisibleItem == totalItemCount - 1 && dy > 0) {
                    userViewModel.fetchUsersFromApi();
                }
            }
        });
        setupAdapterListener();
    }

    private void setupAdapterListener() {
        adapter.setItemClickListener(position -> {
            UserEntity user = adapter.getUserAtPosition(position);
            Intent intent = new Intent(this, EditOrDeleteUserActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        });
    }

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

    private void filterUsers(String query) {
        List<UserEntity> filteredUsers = allUsers.stream()
                .filter(user -> user.getFirstName().toLowerCase().contains(query.toLowerCase()) ||
                        user.getLastName().toLowerCase().contains(query.toLowerCase()) ||
                        user.getEmail().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
        adapter.setUsers(filteredUsers);
    }

    @Override
    public void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.action_menu_view, fragment)
                .commit();
    }
}
