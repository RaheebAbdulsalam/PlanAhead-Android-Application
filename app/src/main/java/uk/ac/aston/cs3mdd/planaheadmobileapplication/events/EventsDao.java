package uk.ac.aston.cs3mdd.planaheadmobileapplication.events;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface EventsDao {

    // An insert method for adding an Event to the events_table
    // OnConflictStrategy.IGNORE to handle conflicts by ignoring new data if a conflict occurs
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertEvent(Event event);

    // An update method for modifying an existing Event in the events_table
    @Update
    void updateEvent(Event event);

    // A method for deleting an Event from the events_table based on its ID
    @Query("DELETE FROM events_table WHERE id = :eventId")
    void deleteEventById(int eventId);

    // A method to retrieve all events from the events_table and return them as LiveData
    @Query("SELECT * FROM events_table")
    LiveData<List<Event>> getAllEvents();

    // A method to retrieve a specific Event from the events_table based on its ID and return it as LiveData
    @Query("SELECT * FROM events_table WHERE id = :eventId")
    LiveData<Event> getEventById(int eventId);

    // A method to search for events in the events_table based on a partial title match and return the results as LiveData
    @Query("SELECT * FROM events_table WHERE title LIKE :title")
    LiveData<List<Event>> searchEventsByTitle(String title);

    @Query("SELECT * FROM events_table ORDER BY date ASC")
    LiveData<List<Event>> getAllEventsByDate();

    @Query("SELECT * FROM events_table ORDER BY date DESC")
    LiveData<List<Event>> getAllEventsByDateDescending();
}
