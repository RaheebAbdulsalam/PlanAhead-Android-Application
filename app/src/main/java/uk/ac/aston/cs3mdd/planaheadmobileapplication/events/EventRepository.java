package uk.ac.aston.cs3mdd.planaheadmobileapplication.events;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class EventRepository {
    private final EventsDao eventsDao;
    private final LiveData<List<Event>> allEvents;

    EventRepository(Application application) {
        // Get an instance of the Room EventDatabase using the application context
        EventDatabase db = EventDatabase.getDatabase(application);
        // Obtain the EventsDao from the Room EventDatabase
        eventsDao = db.eventDao();
        // Retrieve LiveData of all events from the DAO
        allEvents = eventsDao.getAllEvents();
    }

    // Method to provide access to the LiveData list of all events
    public LiveData<List<Event>> getAllEvents() {
        return allEvents;
    }

    // Method to insert an event into the database using a background thread
    public void insertEvent(Event event) {
        // Use a background thread from the ExecutorService to perform the insertion
       EventDatabase.databaseWriteExecutor.execute(() -> {
            eventsDao.insertEvent(event);
        });
    }

    // Method to retrieve a specific event by its ID using LiveData
    public LiveData<Event> getEventById(int eventId) {
        return eventsDao.getEventById(eventId);
    }

    // Method to update an existing event in the database using a background thread
    public void update(Event event) {
        // A background thread from the ExecutorService is used to perform the update
       EventDatabase.databaseWriteExecutor.execute(() -> {
            eventsDao.updateEvent(event);
        });
    }

    // Method to delete an event from the database using a background thread
    public void delete(int eventId) {
        EventDatabase.databaseWriteExecutor.execute(() -> {
            eventsDao.deleteEventById(eventId);
        });
    }

    // Method to search for events by title and return the results as LiveData
    public LiveData<List<Event>> searchEventsByTitle(String title) {
        return eventsDao.searchEventsByTitle("%" + title + "%");
    }


    public LiveData<List<Event>> getAllEventsByDate(){
        return eventsDao.getAllEventsByDate();
    }

    LiveData<List<Event>> getAllEventsByDateDescending() {
        return eventsDao.getAllEventsByDateDescending();
    }
}
