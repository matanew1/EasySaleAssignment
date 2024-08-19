package com.example.assignment.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

// Define the database configuration and serve as the main access point to the persisted data
@Database(entities = {UserEntity.class}, version = 1)
public abstract class UserDatabase extends RoomDatabase {

    // Volatile variable to ensure visibility of changes to the INSTANCE variable across threads
    private static volatile UserDatabase INSTANCE;

    // Method to get the singleton instance of the database
    public static UserDatabase getDatabase(final Context context) {
        // Check if INSTANCE is null; if yes, synchronize to ensure only one instance is created
        if (INSTANCE == null) {
            synchronized (UserDatabase.class) {
                // Double-check if INSTANCE is still null before creating the database
                if (INSTANCE == null) {
                    // Create the database using Room's database builder
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    UserDatabase.class, "user_database")
                            .build(); // Build the database instance
                }
            }
        }
        return INSTANCE; // Return the singleton instance of the database
    }

    // Abstract method to provide access to the DAO (Data Access Object) for the UserEntity
    public abstract UserDao userDao();
}
