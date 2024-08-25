package com.example.assignment.api;

import androidx.annotation.NonNull;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Client class for making network requests using Retrofit.
 */
public class ApiClient {

    private static final String BASE_URL = "https://reqres.in/";
    private static Retrofit retrofit;

    /**
     * Get the Retrofit instance for making network requests.
     * @return The Retrofit instance.
     */
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    /**
     * Get the API service for making network requests.
     * @return The API service.
     */
    @NonNull
    public static ApiService getApiService() {
        return getRetrofitInstance().create(ApiService.class);
    }
}
