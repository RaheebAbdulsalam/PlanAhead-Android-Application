package uk.ac.aston.cs3mdd.planaheadmobileapplication.events;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import uk.ac.aston.cs3mdd.planaheadmobileapplication.HomeFragment;
import uk.ac.aston.cs3mdd.planaheadmobileapplication.R;

// Activity class for updating and deleting an event
public class UpdateEventActivity extends AppCompatActivity {
    EditText title_input, address_input, postcode_input, city_input, notes_input;
    Button date_button, time_button, update_button, delete_button;
    String id, title, date, time, address, postcode, city, notes;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_event);
        // Initialising layout elements by finding them using their IDs
        title_input = findViewById(R.id.title_input_update);
        date_button = findViewById(R.id.date_button_update);
        time_button = findViewById(R.id.time_button_update);
        address_input = findViewById(R.id.address_input_update);
        postcode_input = findViewById(R.id.postcode_input_update);
        city_input = findViewById(R.id.city_input_update);
        notes_input = findViewById(R.id.notes_input_update);
        update_button = findViewById(R.id.update_button);
        delete_button = findViewById(R.id.delete_button);

        // Retrieve data from the Intent
        getIntentDataEvent();

        // Setting the Event Title as the action bar title when clicking on an event
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }

        // Set up a DatePickerDialog for the date_button
        date_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        // Set up a TimePickerDialog for the time_button
        time_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog();
            }
        });

        // Update Button
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventRepository eventRepository = new EventRepository(UpdateEventActivity.this);

                Event updatedEvent = new Event(
                        id,
                        title_input.getText().toString().trim(),
                        date_button.getText().toString().trim(),
                        time_button.getText().toString().trim(),
                        address_input.getText().toString().trim(),
                        postcode_input.getText().toString().trim(),
                        city_input.getText().toString().trim(),
                        notes_input.getText().toString().trim()
                );

                // Update the event in the database
                eventRepository.updateEvent(updatedEvent);
            }
        });


        // Delete Button
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show a confirmation dialog before deleting the event
                confirmMessage();
            }
        });
    }

    // Method to show the DatePickerDialog
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        // Update the date_button with the selected date
                        date_button.setText(String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, day));
                    }
                },
                year, month, day);

        // Show the DatePickerDialog
        datePickerDialog.show();
    }

    // Method to show the TimePickerDialog
    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        // Update the time_button with the selected time
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.US);
                        time_button.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                },
                hour, minute, true);

        // Show the TimePickerDialog
        timePickerDialog.show();
    }

    // Retrieve event data from the Intent
    public void getIntentDataEvent() {
        if (getIntent().hasExtra("id") && getIntent().hasExtra("title") &&
                getIntent().hasExtra("date") && getIntent().hasExtra("time") &&
                getIntent().hasExtra("address") &&
                getIntent().hasExtra("postcode") &&
                getIntent().hasExtra("city") && getIntent().hasExtra("notes")) {
            // Getting Data from Intent
            id = getIntent().getStringExtra("id");
            title = getIntent().getStringExtra("title");
            date = getIntent().getStringExtra("date");
            time = getIntent().getStringExtra("time");
            address = getIntent().getStringExtra("address");
            postcode = getIntent().getStringExtra("postcode");
            city = getIntent().getStringExtra("city");
            notes = getIntent().getStringExtra("notes");

            // Setting Intent Data to input fields
            title_input.setText(title);
            date_button.setText(date);
            time_button.setText(time);
            address_input.setText(address);
            postcode_input.setText(postcode);
            city_input.setText(city);
            notes_input.setText(notes);
        } else {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
    }

    // A method to display a confirmation dialog before deleting the event
    public void confirmMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(" Delete " + title + " ? ");
        builder.setMessage(" Are you sure you want to delete " + title + " ? ");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EventRepository eventRepository = new EventRepository(UpdateEventActivity.this);
                eventRepository.deleteEvent(id);
                // Notify HomeFragment to refresh the data
                ((HomeFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment)).storeDataInArrays();
                // Finish the activity
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Cancel the delete process if the user chooses not to delete
            }
        });
        builder.create().show();
    }
}
