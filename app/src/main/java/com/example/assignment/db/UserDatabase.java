package com.example.assignment.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * Database class for the UserEntity.
 * @see UserEntity
 */
@Database(entities = {UserEntity.class, DeletedUserEntity.class}, version = 2)
public abstract class UserDatabase extends RoomDatabase {

    // Volatile variable to ensure visibility of changes to the INSTANCE variable across threads
    private static volatile UserDatabase INSTANCE;

    /**
     * Get the singleton instance of the database.
     * @param context The application context.
     * @return The singleton instance of the database.
     */
    public static UserDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (UserDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.
                            databaseBuilder(context.getApplicationContext(), UserDatabase.class, "user_database")
                            .build();
                }
            }
        }
        return INSTANCE; // Return the singleton instance of the database
    }

    /**
     * Get the DAO for the UserEntity.
     * @return The DAO for the UserEntity.
     */
    public abstract UserDao userDao();

    /**
     * Get the DAO for the DeletedUser.
     * @return The DAO for the DeletedUser.
     */
    public abstract DeletedUserDao deletedUserDao();
}
