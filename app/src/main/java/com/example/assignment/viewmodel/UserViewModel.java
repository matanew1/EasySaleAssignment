package com.example.assignment.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.assignment.db.UserEntity;
import com.example.assignment.repository.UserRepository;

import java.util.List;

/**
 * ViewModel class for managing user data in the application.
 */
public class UserViewModel extends AndroidViewModel {
    private final UserRepository repository;
    private final MutableLiveData<List<UserEntity>> allUsers;
    private int currentPage = 1;
    private final int pageSize = 10;
    private boolean hasMoreData = true;

    /**
     * Constructor for the UserViewModel.
     * @param application The application context.
     */
    public UserViewModel(@NonNull Application application) {
        super(application); // Call the constructor of the superclass (AndroidViewModel)
        repository = new UserRepository(application); // Initialize the UserRepository
        allUsers = new MutableLiveData<>();
        loadUsers(); // Load users initially
    }

    /**
     * Get all users from the repository.
     * @return LiveData containing a list of all users.
     */
    public LiveData<List<UserEntity>> getAllUsers() {
        return allUsers; // Return the LiveData object
    }

    /**
     * Load users from the database and fetch them from the API if necessary.
     */
    private void loadUsers() {
        // Observe changes in user data from the repository
        repository.getAllUsers().observeForever(users -> {
            if (users != null) {
                if (users.isEmpty()) {
                    // Fetch users from the API if the database is empty
                    fetchUsersFromApi();
                } else {
                    // Update LiveData with users from the database
                    allUsers.postValue(users);
                }
            }
        });
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
        if (hasMoreData) {
            hasMoreData = repository.fetchUsersFromApi(currentPage, pageSize, users -> {
                if (users != null && !users.isEmpty()) {
                    allUsers.postValue(users);
                    incrementPage();
                } else {
                    hasMoreData = false; // Stop further loading when no more data is available
                    currentPage = 1;
                }
            });
        }
    }

    private synchronized void incrementPage() {
        currentPage++;
    }

    /**
     * Checks if a user with the given email exists.
     * @param email The email to check.
     * @return A LiveData object containing the UserEntity if found, null otherwise.
     */
    public LiveData<UserEntity> getUserByEmail(String email) {
        return repository.getUserByEmail(email);
    }

    public boolean hasMoreData() {
        return hasMoreData;
    }
}
