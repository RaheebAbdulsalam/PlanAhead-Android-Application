package uk.ac.aston.cs3mdd.planaheadmobileapplication;

import static org.junit.Assert.assertEquals;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.List;

import uk.ac.aston.cs3mdd.planaheadmobileapplication.events.Event;
import uk.ac.aston.cs3mdd.planaheadmobileapplication.events.EventsDao;
import uk.ac.aston.cs3mdd.planaheadmobileapplication.events.EventDatabase;

@RunWith(AndroidJUnit4.class)
public class EventDatabaseTest {
//    private EventDatabase eventDatabase;
//    private EventsDao eventDao;
//
//    @Before
//    public void createDb() {
//            eventDatabase = Room.inMemoryDatabaseBuilder(
//                            ApplicationProvider.getApplicationContext(),
//                            EventDatabase.class)
//                    .allowMainThreadQueries()
//                    .build();
//            eventDao = eventDatabase.eventDao();
//    }
//
//    @After
//    public void closeDb(){
//        eventDatabase.close();
//    }
//
//    @Test
//    public void insertEvent() throws Exception {
//        Event event = new Event("Test Event", "2023-12-31", "12:00 PM", "Test Address", "12345", "Test City", "Test Notes");
//        eventDao.insertEvent(event);
//        List<Event> allEvents = LiveDataTest.getValue(eventDao.getAllEvents());
//        assert (allEvents.get(0).getTitle().equals("Test Event"));
//    }
//
//    @Test
//    public void updateEvent() throws Exception {
//        Event event = new Event("Test Event", "2023-12-31", "12:00 PM", "Test Address", "12345", "Test City", "Test Notes");
//        eventDao.insertEvent(event);
//        List<Event> allEvents = LiveDataTest.getValue(eventDao.getAllEvents());
//        assert (allEvents.size() == 1);
//        Event updatedEvent = allEvents.get(0);
//        updatedEvent.setTitle("Updated Event");
//        eventDao.updateEvent(updatedEvent);
//        List<Event> updatedEvents = LiveDataTest.getValue(eventDao.getAllEvents());
//        assert (updatedEvents.get(0).getTitle().equals("Updated Event"));
//    }
//
//    @Test
//    public void deleteEvent() throws Exception {
//        Event event = new Event("Test Event", "2023-12-31", "12:00 PM", "Test Address", "12345", "Test City", "Test Notes");
//        eventDao.insertEvent(event);
//        List<Event> allEvents = LiveDataTest.getValue(eventDao.getAllEvents());
//        eventDao.deleteEventById(allEvents.get(0).getId());
//        List<Event> deletedEvents = LiveDataTest.getValue(eventDao.getAllEvents());
//        assert (deletedEvents.isEmpty());
//    }
//
//    @Test
//    public void getAllEvents() throws Exception {
//        // Insert test events
//        Event event1 = new Event("Event 1", "2023-12-31", "12:00 PM", "Address 1", "12345", "City 1", "Notes 1");
//        Event event2 = new Event("Event 2", "2023-12-31", "01:00 PM", "Address 2", "54321", "City 2", "Notes 2");
//        eventDao.insertEvent(event1);
//        eventDao.insertEvent(event2);
//        List<Event> allEvents = LiveDataTest.getValue(eventDao.getAllEvents());
//        assertEquals("Event 1", allEvents.get(0).getTitle());
//        assertEquals("Event 2", allEvents.get(1).getTitle());
//    }

}

