package uk.ac.aston.cs3mdd.planaheadmobileapplication.events;

public class Event {
    private String id, title, date, time, address, postcode, city, notes;

    public Event(String id, String title, String date, String time, String address, String postcode, String city, String notes) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.time = time;
        this.address = address;
        this.postcode = postcode;
        this.city = city;
        this.notes = notes;
    }
    public Event() {
        // Default constructor
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
