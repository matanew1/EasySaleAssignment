package com.example.assignment.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.assignment.R;
import com.example.assignment.db.UserEntity;
import com.example.assignment.utils.ILoadFragment;
import com.example.assignment.viewmodel.UserViewModel;

import java.util.ArrayList;


//TODO: PAGINATION !!! + fix create new
public class GetAllUsersActivity extends AppCompatActivity implements ILoadFragment {
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_all_users);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        UserAdapter adapter = new UserAdapter();
        recyclerView.setAdapter(adapter);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getAllUsers().observe(this, users -> {
            if (users != null && !users.isEmpty()) {
                adapter.setUsers(users);
            } else {
                adapter.setUsers(new ArrayList<>());
                userViewModel.fetchUsersFromApi();
            }
        });

        if (savedInstanceState == null) {
            loadFragment(new MenuFragment());
        }

        adapter.setDeleteListener(position -> {
            UserEntity user = UserAdapter.users.get(position);
            userViewModel.deleteUser(user);
        });
    }

    @Override
    public void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.action_menu_view, fragment)
                .commit();
    }
}
