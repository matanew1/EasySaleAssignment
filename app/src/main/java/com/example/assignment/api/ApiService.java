package com.example.assignment.api;

import com.example.assignment.db.UserEntity;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interface defining the API endpoints for fetching users.
 */
public interface ApiService {

    /**
     * Fetch users from the API.
     * @param page The page number for pagination.
     * @return A Retrofit Call object containing the API response.
     */
    @GET("api/users")
    Call<UserResponse> getUsers(@Query("page") int page, @Query("per_page") int perPage);

    @POST("api/users")
    Call<UserEntity> addUser(@Body UserEntity user);

    @PUT("api/users/{id}")
    Call<UserEntity> updateUser(@Path("id") int id, @Body UserEntity user);

    @DELETE("api/users/{id}")
    Call<Void> deleteUser(@Path("id") int id);
}
