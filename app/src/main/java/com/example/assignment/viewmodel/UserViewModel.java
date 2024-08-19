package com.example.assignment.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.assignment.db.UserEntity;
import com.example.assignment.repository.UserRepository;

import java.util.List;

// UserViewModel class that manages UI-related data in a lifecycle-conscious way
public class UserViewModel extends AndroidViewModel {
    // Declaration of the UserRepository instance
    private UserRepository repository;
    // LiveData holding a list of UserEntity objects representing all users
    private LiveData<List<UserEntity>> allUsers;

    // Constructor for the UserViewModel
    public UserViewModel(@NonNull Application application) {
        super(application); // Call the constructor of the superclass (AndroidViewModel)
        repository = new UserRepository(application); // Initialize the UserRepository
        allUsers = repository.getAllUsers(); // Retrieve all users from the repository and assign to LiveData
    }

    // Method to return the LiveData containing the list of all users
    public LiveData<List<UserEntity>> getAllUsers() {
        return allUsers; // Return the LiveData object
    }

    // Method to insert a new user into the database
    public void insert(UserEntity user) {
        repository.insert(user); // Call the insert method of the repository to add a new user
    }

    // Method to update an existing user's details in the database
    public void update(UserEntity user) {
        repository.update(user); // Call the update method of the repository to modify user details
    }

    // Method to delete a user from the database
    public void delete(UserEntity user) {
        repository.delete(user); // Call the delete method of the repository to remove the user
    }

    // Method to fetch users from the API
    public void fetchUsersFromApi(int page) {
        repository.fetchUsersFromApi(page); // Call the fetchUsersFromApi method of the repository with a page number
    }
}
