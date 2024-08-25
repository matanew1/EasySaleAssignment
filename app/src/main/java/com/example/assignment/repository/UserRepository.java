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

/**
 * Repository class for managing user-related data operations.
 */
public class UserRepository {
    private UserDao userDao; // Database access object for user data
    private ApiService apiService; // API service for making network requests

    /**
     * Constructor for the UserRepository class.
     * @param application The application context.
     */
    public UserRepository(Application application) {
        // Get the instance of the Room database and initialize UserDao
        UserDatabase db = UserDatabase.getDatabase(application);
        userDao = db.userDao();
        // Initialize ApiService for network requests
        apiService = ApiClient.getApiService();
    }

    /**
     * Get all users from the local database.
     * @return LiveData object containing a list of all users.
     */
    public LiveData<List<UserEntity>> getAllUsers() {
        return userDao.getAllUsers(); // Returns the LiveData object containing all users from the database
    }

    /**
     * Insert a new user into the local database.
     * @param user The user entity to be inserted.
     */
    public void insert(UserEntity user) {
        // Runs the insert operation in a background thread
        new Thread(() -> userDao.insert(user)).start();
    }

    /**
     * Update an existing user in the local database.
     * @param user The user entity to be updated.
     */
    public void update(UserEntity user) {
        // Runs the update operation in a background thread
        new Thread(() -> userDao.update(user)).start();
    }

    /**
     * Delete a user from the local database.
     * @param user The user entity to be deleted.
     */
    public void delete(UserEntity user) {
        // Runs the delete operation in a background thread
        new Thread(() -> userDao.delete(user)).start();
    }

    /**
     * Fetch users from the API and insert them into the local database.
     * @param page The current page number for pagination.
     * @param totalPages The total number of pages.
     */
    public void fetchUsersFromApi(int page, int totalPages) {
        apiService.getUsers(page, totalPages).enqueue(new Callback<UserResponse>() {

            @Override
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<UserEntity> users = response.body().getData();
                    if (users != null && !users.isEmpty()) {
                        for (UserEntity user : users) {
                            insert(user);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {}
        });
    }

    /**
     * Delete all users from the local database.
     */
    public void deleteAllUsers() {
        new Thread(() -> userDao.deleteAllUsers()).start();
    }
}
