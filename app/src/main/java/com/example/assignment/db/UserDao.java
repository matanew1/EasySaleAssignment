package com.example.assignment.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Data Access Object (DAO) for the UserEntity.
 */
@Dao
public interface UserDao {

    /**
     * Insert a new UserEntity into the user_table.
     * @param user The UserEntity to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserEntity user);

    /**
     * Update an existing UserEntity in the user_table.
     * @param user The UserEntity to be updated.
     */
    @Update
    void update(UserEntity user);

    /**
     * Delete a UserEntity from the user_table.
     * @param user The UserEntity to be deleted.
     */
    @Delete
    void delete(UserEntity user);

    /**
     * Get all UserEntity objects from the user_table.
     * @return A LiveData object containing a list of all UserEntity objects.
     */
    @Query("SELECT * FROM user_table ORDER BY id ASC")
    LiveData<List<UserEntity>> getAllUsers(); // Returns a LiveData object containing a list of all UserEntity objects

    /**
     * Delete all UserEntity objects from the user_table.
     */
    @Query("DELETE FROM user_table")
    void deleteAllUsers();
}
