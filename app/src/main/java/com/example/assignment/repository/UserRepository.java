package com.example.assignment.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.assignment.api.ApiClient;
import com.example.assignment.api.ApiService;
import com.example.assignment.api.UserResponse;
import com.example.assignment.db.UserDao;
import com.example.assignment.db.UserDatabase;
import com.example.assignment.db.UserEntity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

// Repository class that handles data operations for UserEntity
public class UserRepository {
    // Data Access Object (DAO) for user-related database operations
    private UserDao userDao;
    // API service for making network requests
    private ApiService apiService;

    // Constructor for UserRepository, takes an Application as a parameter
    public UserRepository(Application application) {
        // Get the instance of the Room database and initialize UserDao
        UserDatabase db = UserDatabase.getDatabase(application);
        userDao = db.userDao();
        // Initialize ApiService for network requests
        apiService = ApiClient.getApiService();
    }

    // Method to get all users from the local database, returns LiveData containing a list of UserEntity objects
    public LiveData<List<UserEntity>> getAllUsers() {
        return userDao.getAllUsers(); // Returns the LiveData object containing all users from the database
    }

    // Method to insert a new user into the local database
    public void insert(UserEntity user) {
        // Runs the insert operation in a background thread
        new Thread(() -> userDao.insert(user)).start();
    }

    // Method to update an existing user in the local database
    public void update(UserEntity user) {
        // Runs the update operation in a background thread
        new Thread(() -> userDao.update(user)).start();
    }

    // Method to delete a user from the local database
    public void delete(UserEntity user) {
        // Runs the delete operation in a background thread
        new Thread(() -> userDao.delete(user)).start();
    }

    // Method to fetch users from the API
    public void fetchUsersFromApi(int page) {
        // Makes an asynchronous network request to fetch users
        apiService.getUsers(page).enqueue(new Callback<UserResponse>() {
            // Called when the API response is received
            @Override
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                // If the response is successful and contains a body
                if (response.isSuccessful() && response.body() != null) {
                    // Loop through each user in the response data
                    for (UserEntity user : response.body().getData()) {
                        // Convert the API user model to a database entity
                        UserEntity userEntity = new UserEntity();
                        userEntity.setId(user.getId());
                        userEntity.setFirstName(user.getFirstName());
                        userEntity.setLastName(user.getLastName());
                        userEntity.setEmail(user.getEmail());
                        userEntity.setAvatar(user.getAvatar());
                        // Insert the user entity into the local database
                        insert(userEntity);
                    }
                }
            }

            // Called if the API request fails
            @Override
            public void onFailure(@NonNull Call<UserResponse> call, Throwable t) {
                // Handle API error (e.g., log the error or show a message to the user)
            }
        });
    }
}
