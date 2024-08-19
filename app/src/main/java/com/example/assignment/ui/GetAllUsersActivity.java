package com.example.assignment.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.assignment.R;
import com.example.assignment.viewmodel.UserViewModel;

// MainActivity class that serves as the entry point for the application
public class GetAllUsersActivity extends AppCompatActivity {
    // Declaration of the UserViewModel instance
    private UserViewModel userViewModel;

    // onCreate method, called when the activity is first created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the activity's layout using activity_main.xml
        setContentView(R.layout.activity_get_all_users);

        // Initialize the RecyclerView component from the layout
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        // Set the layout manager for the RecyclerView, which determines how the items are arranged
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Optimize RecyclerView's performance by indicating that size is fixed
        recyclerView.setHasFixedSize(true);

        // Create an instance of UserAdapter to handle data binding between the data and the RecyclerView
        UserAdapter adapter = new UserAdapter();
        // Set the adapter for the RecyclerView
        recyclerView.setAdapter(adapter);

        // Initialize the UserViewModel using ViewModelProvider, which provides a ViewModel that is scoped to this activity
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        // Observe changes in the list of users from the ViewModel and update the adapter whenever the data changes
        userViewModel.getAllUsers().observe(this, adapter::setUsers);

        // Fetch user data from the API, specifying the page number (in this case, 1)
        userViewModel.fetchUsersFromApi(1);

        // Load the MenuFragment
        if (savedInstanceState == null) {
            loadFragment(new MenuFragment());
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.action_menu_view, fragment);
        fragmentTransaction.commit();
    }
}
