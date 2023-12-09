package uk.ac.aston.cs3mdd.planaheadmobileapplication.events;

import android.content.Context;
import android.database.Cursor;

public class EventRepository {
    private final EventDatabase eventDatabase;

    public EventRepository(Context context) {
        this.eventDatabase = new EventDatabase(context);
    }
    public void addEvent(Event event) {
        eventDatabase.addEvent(event);
    }
    public void updateEvent(Event event) {
        eventDatabase.updateEvent(event);
    }
    public Cursor getAllEvents() {
        return eventDatabase.readAllData();
    }

    public void deleteEvent(String row_id) {
        eventDatabase.deleteEvent(row_id);
    }

    public Cursor searchEvents(String title) {
        return eventDatabase.searchData(title);
    }


}