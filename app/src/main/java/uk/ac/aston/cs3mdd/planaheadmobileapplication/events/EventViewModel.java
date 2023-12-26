package uk.ac.aston.cs3mdd.planaheadmobileapplication.events;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class EventViewModel extends AndroidViewModel {
    private final EventRepository eventRepository;
    private final LiveData<List<Event>> allEvents;

    public EventViewModel(Application application) {
        super(application);
        eventRepository = new EventRepository(application);
        allEvents = eventRepository.getAllEvents();
    }

    // Method to provide access to the LiveData list of all events
    public LiveData<List<Event>> getAllEvents() {
        return allEvents;
    }

    // Method to insert an event into the database through the repository
    public void insert(Event event) {
        eventRepository.insertEvent(event);
    }

    // Method to retrieve a specific event by its ID using LiveData through the repository
    public LiveData<Event> getEventById(int eventId) {
        return eventRepository.getEventById(eventId);
    }

    // Method to update an existing event in the database through the repository
    public void update(Event event) {
        eventRepository.update(event);
    }

    // Method to delete an event from the database through the repository
    public void delete(int eventId) {
        eventRepository.delete(eventId);
    }

    // Method to search for events its title and return the results as LiveData through the repository
    public LiveData<List<Event>> searchEventsByTitle(String title) {
        return eventRepository.searchEventsByTitle(title);
    }

    //Method to get all dates fot events
    public LiveData<List<Event>> getAllEventsByDate() {
        return eventRepository.getAllEventsByDate();
    }

    public LiveData<List<Event>> getAllEventsByDateDescending() {
        return eventRepository.getAllEventsByDateDescending();
    }

}
