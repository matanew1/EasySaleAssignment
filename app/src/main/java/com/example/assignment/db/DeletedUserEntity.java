package com.example.assignment.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "deleted_users")
public class DeletedUserEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String userName;
    public String email;
    public String deletedAt;  // Timestamp of deletion

    // Getters and setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getDeletedAt() {
        return deletedAt;
    }
    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    //toString

    @NonNull
    @Override
    public String toString() {
        return "DeletedUserEntity{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", deletedAt='" + deletedAt + '\'' +
                '}';
    }
}
