package uk.ac.aston.cs3mdd.planaheadmobileapplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class EventDatabaseTest {
//    private AppDatabase eventDatabase;
//    private SQLiteDatabase db;
//
//    // Method to set up the database
//    @Before
//    public void setup() {
//        Context context = ApplicationProvider.getApplicationContext();
//        db = SQLiteDatabase.create(null);
//        eventDatabase = new AppDatabase(context) {
//            @Override
//            public SQLiteDatabase getWritableDatabase() {
//                return db;
//            }
//        };
//        // Call onCreate to create the necessary table the database
//        eventDatabase.onCreate(db);
//    }
//
//    // Method to clean up resources, and close the database
//    @After
//    public void cleanup() {
//        db.close();
//    }
//
//    @Test
//    public void testAddEvent() {
//        Event event = new Event("1", "Test Event", "2023-12-09", "12:00", "Test Address", "12345", "Test City", "Test Note");
//
//        // Call the addEvent method
//        long result = eventDatabase.addEvent(event);
//
//        // Check that the result is not -1 (indicating success)
//        assertNotEquals(-1, result);
//
//        // Check if the event is added to the database
//        Cursor cursor = db.rawQuery("SELECT * FROM events WHERE _id = ?", new String[]{"1"});
//        assertTrue(cursor.moveToFirst());
//        assertEquals("Test Event", cursor.getString(cursor.getColumnIndex("event_title")));
//
//    }
//
//
//    @Test
//    public void testReadAllData() {
//        // Calling the readAllData method
//       eventDatabase.readAllData();
//    }
//
//
//    @Test
//    public void testUpdateEvent() {
//        // Adding an event to the database
//        Event event = new Event("1", "Test Event", "2023-12-09", "12:00", "Test Address", "12345", "Test City", "Test Note");
//        eventDatabase.addEvent(event);
//
//        // Creating an updated event
//        Event updatedEvent = new Event("1", "Updated Event", "2023-02-02", "14:00", "Updated Address", "67890", "Updated City", "Updated Note");
//
//        // Calling the updateEvent method
//        eventDatabase.updateEvent(updatedEvent);
//
//        // Checking if the event details are updated in the database
//        Cursor cursor = db.rawQuery("SELECT * FROM events WHERE _id = ?", new String[]{"1"});
//        assertTrue(cursor.moveToFirst());
//        assertEquals("Updated Event", cursor.getString(cursor.getColumnIndex("event_title")));
//        assertEquals("2023-02-02", cursor.getString(cursor.getColumnIndex("event_date")));
//    }
//
//    @Test
//    public void testDeleteEvent() {
//        // Adding an event to the database
//        Event event = new Event("1", "Test Event", "2023-09-12", "12:00", "Test Address", "12345", "Test City", "Test Note");
//        eventDatabase.addEvent(event);
//
//        // Calling the deleteEvent method
//        eventDatabase.deleteEvent("1");
//
//        // Check if the event is deleted from the database
//        Cursor cursor = db.rawQuery("SELECT * FROM events WHERE _id = ?", new String[]{"1"});
//        // Expecting no rows for the deleted event
//        assertFalse(cursor.moveToFirst());
//
//    }


}

