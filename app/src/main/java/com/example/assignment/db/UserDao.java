package com.example.assignment.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

// Data Access Object (DAO) interface for performing database operations on the UserEntity table
@Dao
public interface UserDao {

    // Insert a new UserEntity into the user_table. If there's a conflict (e.g., same ID), replace the existing entry
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserEntity user);

    // Update an existing UserEntity in the user_table
    @Update
    void update(UserEntity user);

    // Delete a UserEntity from the user_table
    @Delete
    void delete(UserEntity user);

    // Query to retrieve all users from the user_table, ordered by their ID in ascending order
    @Query("SELECT * FROM user_table ORDER BY id ASC")
    LiveData<List<UserEntity>> getAllUsers(); // Returns a LiveData object containing a list of all UserEntity objects

    @Query("DELETE FROM user_table")
    void deleteAllUsers();
}
