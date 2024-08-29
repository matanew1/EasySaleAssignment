package com.example.assignment.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DeletedUserDao {
    @Insert
    void insert(DeletedUserEntity deletedUser);

    @Query("SELECT * FROM deleted_users")
    List<DeletedUserEntity> getAllDeletedUsers();

    @Query("DELETE FROM deleted_users WHERE id = :userId")
    void deleteById(int userId);

    @Query("DELETE FROM deleted_users")
    void deleteAll();
}

