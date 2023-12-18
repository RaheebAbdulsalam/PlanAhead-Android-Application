package uk.ac.aston.cs3mdd.planaheadmobileapplication.events;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Event.class}, version = 1, exportSchema = false)
public abstract class EventDatabase extends RoomDatabase {
    // Declare an abstract method to get the DAO (Data Access Object)
    public abstract EventsDao eventDao();

    // Singleton pattern to ensure only one instance of the database is created
    private static EventDatabase INSTANCE;

    // Number of threads for database write operations
    private static final int NUMBER_OF_THREADS = 4;

    // ExecutorService to manage background threads for database write operations
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    // Static method to get an instance of the database
    static EventDatabase getDatabase(final Context context) {
        // Check if an instance already exists
        if (INSTANCE == null) {
            // Synchronize to prevent multiple threads from creating separate instances simultaneously
            synchronized (EventDatabase.class) {
                // Double-check for null inside the synchronized block
                if (INSTANCE == null) {
                    // Create a new database instance using Room's databaseBuilder method
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    EventDatabase.class, "event_database")
                            .build();
                }
            }
        }
        // Return the existing or newly created instance
        return INSTANCE;
    }
}