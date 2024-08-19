package com.example.assignment.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

// Interface defining the API endpoints for network operations using Retrofit
public interface ApiService {

    // Define a GET request to fetch users from the API endpoint "api/users"
    @GET("api/users")
    Call<UserResponse> getUsers(@Query("page") int page);
    // This method takes an integer parameter 'page' to specify the page number in the API request
    // The @Query annotation appends the 'page' parameter to the request URL as a query parameter
    // Returns a Call object that will contain the response in a UserResponse object when the request is executed
}
