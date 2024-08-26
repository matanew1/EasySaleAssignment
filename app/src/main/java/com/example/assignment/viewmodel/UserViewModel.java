package com.example.assignment.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.assignment.db.UserEntity;
import com.example.assignment.repository.UserRepository;

import java.util.List;

/**
 * ViewModel class for managing user data in the application.
 */
public class UserViewModel extends AndroidViewModel {
    private UserRepository repository;
    private LiveData<List<UserEntity>> allUsers;

    /**
     * Constructor for the UserViewModel.
     * @param application The application context.
     */
    public UserViewModel(@NonNull Application application) {
        super(application); // Call the constructor of the superclass (AndroidViewModel)
        repository = new UserRepository(application); // Initialize the UserRepository
        allUsers = repository.getAllUsers(); // Retrieve all users from the repository and assign to LiveData
    }

    /**
     * Get all users from the repository.
     * @return LiveData containing a list of all users.
     */
    public LiveData<List<UserEntity>> getAllUsers() {
        return allUsers; // Return the LiveData object
    }

    /**
     * Delete all users from the database.
     */
    public void deleteAllUsers() {
        repository.deleteAllUsers();
    }

    /**
     * Add a new user to the database.
     * @param user The user entity to be added.
     */
    public void addNewUser(UserEntity user) {
        repository.insert(user); // Call the insert method of the repository to add a new user
    }

    /**
     * Update an existing user in the database.
     * @param user The user entity to be updated.
     */
    public void updateUser(@NonNull UserEntity user) {
        repository.update(user); // Call the update method of the repository to modify user details
    }

    /**
     * Delete a user from the database.
     * @param user The user entity to be deleted.
     */
    public void deleteUser(UserEntity user) {
        repository.delete(user); // Call the delete method of the repository to remove the user
    }

    /**
     * Fetch users from the API and insert them into the database.
     */
    public void fetchUsersFromApi() {
        int currentPage = 1;
        repository.fetchUsersFromApi(currentPage);
    }

}
