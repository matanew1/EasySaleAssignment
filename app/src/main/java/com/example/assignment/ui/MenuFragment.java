package com.example.assignment.ui;

import android.app.Activity;
import android.app.ActivityOptions;
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

/**
 * Fragment representing the menu screen.
 */
public class MenuFragment extends Fragment {

    /**
     * Called to have the fragment instantiate its user interface view.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    /**
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize UI components
        Button addUserButton = view.findViewById(R.id.btn_add_user);
        Button getAllUsersButton = view.findViewById(R.id.btn_get_all_users);

        // Set click listeners
        addUserButton.setOnClickListener(v -> switchScreen(AddUserActivity.class));
        getAllUsersButton.setOnClickListener(v -> switchScreen(GetAllUsersActivity.class));
    }

    /**
     * Switches the current screen to the specified activity.
     * @param target The activity to switch to.
     */
    private void switchScreen(Class<? extends Activity> target) {
        Intent intent = new Intent(getActivity(), target);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Start the activity in a new task
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Clear activities on top of the specified activity
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY); // Do not save the activity in the history stack

        // Create custom transition animations
        Bundle options = ActivityOptions.makeCustomAnimation(
                getContext(),
                R.anim.fade_in,
                R.anim.fade_out
        ).toBundle();
        startActivity(intent, options);
    }

}
