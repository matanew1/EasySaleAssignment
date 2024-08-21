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
    public interface ApiCallback {
        void onResult(boolean hasData);
    }

    // Method to fetch users from the API
    public void fetchUsersFromApi(int page, ApiCallback callback) {
        apiService.getUsers(page).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<UserEntity> users = response.body().getData();
                    if (users != null && !users.isEmpty()) {
                        for (UserEntity user : users) {
                            insert(user);
                        }
                        callback.onResult(true);
                    } else {
                        callback.onResult(false);
                    }
                } else {
                    callback.onResult(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                callback.onResult(false);
            }
        });
    }

    public void deleteAllUsers() {
        new Thread(() -> userDao.deleteAllUsers()).start();
    }
}
