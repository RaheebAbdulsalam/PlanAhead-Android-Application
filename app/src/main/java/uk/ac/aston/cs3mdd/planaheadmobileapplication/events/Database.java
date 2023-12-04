package uk.ac.aston.cs3mdd.planaheadmobileapplication.events;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;


// Database class responsible for managing the SQLite database for events
public class Database extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "Events.db";
    private static final int DATABASE_VERSION = 3;
    private static final String TABLE_NAME = "events";

    private static final String COLUMN_ID = "_id";

    private static final String COLUMN_TITLE = "event_title";

    private static final String COLUMN_DATE = "event_date";

    private static final String COLUMN_TIME = "event_time";

    private static final String COLUMN_ADDRESS = "event_address";

    private static final String COLUMN_POSTCODE = "event_postcode";
    private static final String COLUMN_CITY = "event_city";

    private static final String COLUMN_NOTES = "event_note";

    private static final String MESSAGE_FAILED = "Failed";
    private static final String MESSAGE_SUCCESS = "Your Event has been added successfully!";

    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    // Called when the database is created for the first time
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL query to create the 'events' table
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DATE + " DATE, " +
                COLUMN_TIME + " TIME, " +
                COLUMN_ADDRESS + " TEXT, " +
                COLUMN_POSTCODE + " TEXT, " +
                COLUMN_CITY + " TEXT, " +
                COLUMN_NOTES + " TEXT);";
        db.execSQL(query);
    }

    // Called when the database needs to be upgraded
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the existing 'events' table and recreate it
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Method to add a new event to the database
    public void addEvent(String title, String date, String time, String address, String postcode, String city, String notes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        // Put event details into ContentValues object
        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_DATE, date.toString());
        cv.put(COLUMN_TIME, time.toString());
        cv.put(COLUMN_ADDRESS, address);
        cv.put(COLUMN_POSTCODE, postcode);
        cv.put(COLUMN_CITY, city);
        cv.put(COLUMN_NOTES, notes);

        // Insert the event into the 'events' table
        long result = db.insert(TABLE_NAME, null, cv);

        if (result == -1) {
            Toast.makeText(context, MESSAGE_FAILED, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, MESSAGE_SUCCESS, Toast.LENGTH_SHORT).show();
        }
    }

    // Method to retrieve all events from the database
    public Cursor readAllData() {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        // Execute the query and store the result in a Cursor
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    // Method to update an existing event in the database
    public void updateEvent(String row_id, String title, String date, String time, String address, String postcode, String city, String notes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_DATE, date.toString());
        cv.put(COLUMN_TIME, time.toString());
        cv.put(COLUMN_ADDRESS, address);
        cv.put(COLUMN_POSTCODE, postcode);
        cv.put(COLUMN_CITY, city);
        cv.put(COLUMN_NOTES, notes);

        // Update the event in the 'events' table based on the row_id
        long result = db.update(TABLE_NAME, cv, COLUMN_ID + "=?", new String[]{row_id});
        Log.d("UpdateEventActivity", "Update result: " + result);

        if (result == -1) {
            Toast.makeText(context, "Failed to Update Event's Information", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "The Event has been updated Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to delete an event from the database
    public void deleteEvent(String row_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Delete the event from the 'events' table based on the row_id
        long result = db.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{row_id});

        if (result == -1) {
            Toast.makeText(context, "Failed to delete the event", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "The Event has been successfully deleted", Toast.LENGTH_SHORT).show();
        }
    }


    // Method to search for events by title
    public Cursor searchData(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_TITLE + " LIKE ?";
        return db.rawQuery(query, new String[]{"%" + title + "%"});
    }

}