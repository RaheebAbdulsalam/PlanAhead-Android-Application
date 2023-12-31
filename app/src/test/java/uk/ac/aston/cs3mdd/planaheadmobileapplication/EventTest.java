package uk.ac.aston.cs3mdd.planaheadmobileapplication;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import uk.ac.aston.cs3mdd.planaheadmobileapplication.events.Event;

public class EventTest {

    @Test
    public void testEvent() {
        // Arrange
        String title = "Test Event";
        String date = "2023-12-31";
        String time = "12:00 PM";
        String address = "Test Address";
        String postcode = "B15 9RJ";
        String city = "Test City";
        String notes = "Test Notes";

        // Act
        Event event = new Event(title, date, time, address, postcode, city, notes);

        // Assert
        assertEquals(title, event.getTitle());
        assertEquals(date, event.getDate());
        assertEquals(time, event.getTime());
        assertEquals(address, event.getAddress());
        assertEquals(postcode, event.getPostcode());
        assertEquals(city, event.getCity());
        assertEquals(notes, event.getNotes());
    }


    @Test
    public void testGetterAndSetter() {
        // Arrange
        Event event = new Event("Initial Event", "2022-01-01", "10:00 AM", "Initial Address", "54321", "Initial City", "Initial Notes");

        // Act
        event.setTitle("Updated Event");
        event.setDate("2023-01-01");
        event.setTime("11:00 AM");
        event.setAddress("Updated Address");
        event.setPostcode("98765");
        event.setCity("Updated City");
        event.setNotes("Updated Notes");

        // Assert
        assertEquals("Updated Event", event.getTitle());
        assertEquals("2023-01-01", event.getDate());
        assertEquals("11:00 AM", event.getTime());
        assertEquals("Updated Address", event.getAddress());
        assertEquals("98765", event.getPostcode());
        assertEquals("Updated City", event.getCity());
        assertEquals("Updated Notes", event.getNotes());
    }
}

