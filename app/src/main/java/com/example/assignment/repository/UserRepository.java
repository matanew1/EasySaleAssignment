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
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository class for managing user-related data operations.
 */
public class UserRepository {

    private final UserDao userDao; // Database access object for user data
    private final ApiService apiService; // API service for making network requests
    private final ExecutorService executorService; // Executor service for background tasks

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
        // Initialize ExecutorService for background operations
        executorService = Executors.newFixedThreadPool(3);
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
        apiService.addUser(user).enqueue(new Callback<UserEntity>() {
            @Override
            public void onResponse(Call<UserEntity> call, Response<UserEntity> response) {
                if (response.isSuccessful() && response.body() != null) {
                    executorService.execute(() -> userDao.insert(response.body()));
                }
            }

            @Override
            public void onFailure(Call<UserEntity> call, Throwable t) {
                logError("Error inserting user: " + t.getMessage());
            }
        });
    }

    /**
     * Update an existing user in the local database.
     * @param user The user entity to be updated.
     */
    public void update(UserEntity user) {
        apiService.updateUser(user.getId(), user).enqueue(new Callback<UserEntity>() {
            @Override
            public void onResponse(Call<UserEntity> call, Response<UserEntity> response) {
                if (response.isSuccessful() && response.body() != null) {
                    executorService.execute(() -> userDao.update(response.body()));
                }
            }

            @Override
            public void onFailure(Call<UserEntity> call, Throwable t) {
                logError("Error updating user: " + t.getMessage());
            }
        });
    }

    /**
     * Delete a user from the local database.
     * @param user The user entity to be deleted.
     */
    public void delete(@NonNull UserEntity user) {
        apiService.deleteUser(user.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    executorService.execute(() -> userDao.delete(user));
                } else {
                    logError("Failed to delete user from API: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                logError("Error deleting user from API: " + t.getMessage());
            }
        });
    }

    /**
     * Fetch users from the API and insert them into the local database.
     * @param page The current page number for pagination.
     * @param fetchUsersCallback The callback to notify when users are fetched.
     */
    public void fetchUsersFromApi(int page, FetchUsersCallback fetchUsersCallback) {
        apiService.getUsers(page).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<UserEntity> users = response.body().getData();
                    if (users != null && !users.isEmpty()) {
                        // Insert users into the database
                        executorService.execute(() -> {
                            for (UserEntity user : users) {
                                userDao.insert(user);
                            }
                            // Notify the callback with fetched users
                            fetchUsersCallback.onUsersFetched(users);
                        });
                    }
                } else {
                    logError("Failed to fetch users: " + response.message());
                    fetchUsersCallback.onUsersFetched(null); // Notify callback with null
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                logError("Error fetching users: " + t.getMessage());
                fetchUsersCallback.onUsersFetched(null); // Notify callback with null
            }
        });
    }

    // Callback interface for fetching users
    public interface FetchUsersCallback {
        void onUsersFetched(List<UserEntity> users);
    }

    // Log errors (could be extended to use a logging framework)
    private void logError(String message) {
        System.err.println(message); // Simple logging to standard error
    }
}
