package com.example.assignment.api;

import com.example.assignment.User;

import java.util.List;

public class UserResponse {
    private List<User> data;

    public List<User> getData() {
        return data;
    }

    public void setData(List<User> data) {
        this.data = data;
    }
}