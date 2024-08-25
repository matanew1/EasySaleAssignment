package com.example.assignment.api;

import com.example.assignment.db.UserEntity;

import java.util.List;

/**
 * Represents the response from the API containing a list of UserEntity objects.
 */
public class UserResponse {
    private List<UserEntity> data;

    /**
     * Getter for the list of UserEntity objects.
     * @return The list of UserEntity objects.
     */
    public List<UserEntity> getData() {
        return this.data;
    }

    /**
     * Setter for the list of UserEntity objects.
     * @param data The list of UserEntity objects to set.
     */
    @SuppressWarnings("unused")
    public void setData(List<UserEntity> data) {
        this.data = data;
    }

}