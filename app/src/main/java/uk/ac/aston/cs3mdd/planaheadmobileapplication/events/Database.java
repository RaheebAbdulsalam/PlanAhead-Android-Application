package uk.ac.aston.cs3mdd.planaheadmobileapplication.events;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;


public class Database extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "Events.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "events";

    private static final String COLUMN_ID = "_id";

    private static final String COLUMN_TITLE = "evnet_title";

    private static final String COLUMN_DATE = "event_date";

    private static final String COLUMN_TIME = "event_time";

    private static final String COLUMN_LOCATION = "event_location";

    private static final String COLUMN_NOTES = "event_note";

    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DATE + " DATE, " +
                COLUMN_TIME + " TIME, " +
                COLUMN_LOCATION + " TEXT, " +
                COLUMN_NOTES + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addEvent(String title, String date, String time, String location, String notes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_DATE, date.toString());
        cv.put(COLUMN_TIME, time.toString());
        cv.put(COLUMN_LOCATION, location);
        cv.put(COLUMN_NOTES, notes);
        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor readAllData() {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public void updateEvent(String row_id,String title, String date, String time, String location, String notes){
        SQLiteDatabase db =this. getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_DATE, date.toString());
        cv.put(COLUMN_TIME, time.toString());
        cv.put(COLUMN_LOCATION, location);
        cv.put(COLUMN_NOTES, notes);

        long result = db.update(TABLE_NAME, cv, COLUMN_ID + "=?", new String[]{row_id});
        Log.d("UpdateEventActivity", "Update result: " + result);

        if (result == -1) {
            Toast.makeText(context, "Failed to Update Event's Information", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "The Event has been updated Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteEvent(String row_id){
        SQLiteDatabase db= this.getWritableDatabase();
        long result=  db.delete(TABLE_NAME,COLUMN_ID + "=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Faild to delete the event", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "The Event has been successfully deleted", Toast.LENGTH_SHORT).show();
        }
    }

}