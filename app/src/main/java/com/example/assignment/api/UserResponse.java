package com.example.assignment.api;

import com.example.assignment.db.UserEntity;

import java.util.List;

public class UserResponse {
    private List<UserEntity> data;

    public List<UserEntity> getData() {
        return data;
    }

    public void setData(List<UserEntity> data) {
        this.data = data;
    }
}