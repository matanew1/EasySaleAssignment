package com.example.assignment.ui;

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
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GetAllUsersActivity extends AppCompatActivity implements ILoadFragment {
    private UserViewModel userViewModel;
    private UserAdapter adapter;
    private List<UserEntity> allUsers = new ArrayList<>();
    private boolean isLoading = false;
    private int currentPage = 1;
    private final int PAGE_SIZE = 20; // Number of items per page

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

    private void initializeViewModel() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getAllUsers().observe(this, users -> {
            if (users != null) {
                allUsers = new ArrayList<>(users);
                adapter.setUsers(allUsers);
                if (users.isEmpty()) {
                    userViewModel.fetchUsersFromApi();
                }
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
                super.onScrolled(recyclerView, dx, dy); // Call the super method first
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null && !isLoading) { // Check if layoutManager is not null and not loading
                    int visibleItemCount = layoutManager.getChildCount(); // Get the number of visible items
                    int totalItemCount = layoutManager.getItemCount(); // Get the total number of items
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition(); // Get the position of the first visible item

                    // Check if we have reached the end of the list
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {
                        // Load more data
                        loadMoreUsers();
                    }
                }
            }
        });

        setupAdapterListener();
    }

    private void setupAdapterListener() {
        adapter.setDeleteListener(position -> {
            UserEntity user = adapter.getUserAtPosition(position);
            userViewModel.deleteUser(user);
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

    private void loadMoreUsers() {
        isLoading = true;
        userViewModel.fetchUsersFromApi();
        currentPage++;
    }

    @Override
    public void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.action_menu_view, fragment)
                .commit();
    }
}
