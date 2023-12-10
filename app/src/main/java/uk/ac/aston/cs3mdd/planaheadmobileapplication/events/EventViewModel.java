package uk.ac.aston.cs3mdd.planaheadmobileapplication.events;


import android.app.Application;
import android.database.Cursor;

import androidx.lifecycle.AndroidViewModel;

public class EventViewModel extends AndroidViewModel {
    private final EventRepository eventRepository;

    public EventViewModel(Application application) {
        super(application);
        this.eventRepository = new EventRepository(application);
    }

    // Methods for accessing data from the repository
    public void addEvent(Event event) {
        eventRepository.addEvent(event);
    }

    public void updateEvent(Event event) {
        eventRepository.updateEvent(event);
    }

    public Cursor getAllEvents() {
        return eventRepository.getAllEvents();
    }

    public void deleteEvent(String row_id) {
        eventRepository.deleteEvent(row_id);
    }

    public Cursor searchEvents(String title) {
        return eventRepository.searchEvents(title);
    }


}



