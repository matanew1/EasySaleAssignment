package com.example.assignment.viewmodel;

import android.app.Application;
import android.util.Log;

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

    public void deleteAllUsers() {
        repository.deleteAllUsers();
    }

    // Method to insert a new user into the database
    public void addNewUser(UserEntity user) {
        repository.insert(user); // Call the insert method of the repository to add a new user
    }

    // Method to update an existing user's details in the database
    public void updateUser(@NonNull UserEntity user) {
        repository.update(user); // Call the update method of the repository to modify user details
    }

    // Method to delete a user from the database
    public void deleteUser(UserEntity user) {
        repository.delete(user); // Call the delete method of the repository to remove the user
    }

    public void fetchUsersFromApi() {
        int currentPage = 1;
        int totalPages = 100;
        repository.fetchUsersFromApi(currentPage, totalPages);
    }

}
