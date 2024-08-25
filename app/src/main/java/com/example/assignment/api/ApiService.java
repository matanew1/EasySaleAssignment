package com.example.assignment.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Interface defining the API endpoints for fetching users.
 */
public interface ApiService {

    /**
     * Fetch users from the API.
     * @param page The page number for pagination.
     * @param perPage The number of users per page.
     * @return A Retrofit Call object containing the API response.
     */
    @GET("api/users")
    Call<UserResponse> getUsers(@Query("page") int page, @Query("per_page") int perPage);
}
