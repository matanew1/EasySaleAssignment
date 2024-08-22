package com.example.assignment.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.assignment.R;

public class MenuFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize UI components
        Button addUserButton = view.findViewById(R.id.btn_add_user);
        Button getAllUsersButton = view.findViewById(R.id.btn_get_all_users);
        Button updateUserButton = view.findViewById(R.id.btn_update_user);

        // Set click listeners
        addUserButton.setOnClickListener(v -> switchScreen(AddUserActivity.class));
        getAllUsersButton.setOnClickListener(v -> switchScreen(GetAllUsersActivity.class));
        updateUserButton.setOnClickListener(v -> switchScreen(UpdateUserActivity.class));
    }

    /**
     * Switches to the specified activity.
     *
     * @param target The activity class to switch to.
     */
    private void switchScreen(Class<? extends Activity> target) {
        Intent intent = new Intent(getActivity(), target);
        startActivity(intent);
    }
}
