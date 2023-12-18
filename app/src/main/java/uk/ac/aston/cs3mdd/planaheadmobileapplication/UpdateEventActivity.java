package uk.ac.aston.cs3mdd.planaheadmobileapplication;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import uk.ac.aston.cs3mdd.planaheadmobileapplication.events.Event;
import uk.ac.aston.cs3mdd.planaheadmobileapplication.events.EventViewModel;

public class UpdateEventActivity extends AppCompatActivity {

    private EditText titleInput, addressInput, postcodeInput, cityInput, notesInput;
    private Button dateButton, timeButton, updateButton, deleteButton;
    private EventViewModel eventViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_event);

        titleInput = findViewById(R.id.title_input_update);
        dateButton = findViewById(R.id.date_button_update);
        timeButton = findViewById(R.id.time_button_update);
        addressInput = findViewById(R.id.address_input_update);
        postcodeInput = findViewById(R.id.postcode_input_update);
        cityInput = findViewById(R.id.city_input_update);
        notesInput = findViewById(R.id.notes_input_update);

        updateButton = findViewById(R.id.update_button);
        deleteButton = findViewById(R.id.delete_button);

        // Initialize the ViewModel
        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);

        // Retrieve data from Intent
        Intent intent = getIntent();
        if (intent.hasExtra("EVENT_ID")) {
            int eventId = intent.getIntExtra("EVENT_ID", -1);
            // Load event data from the database using the eventId
            // and populate the UI fields with the event data
            loadEventData(eventId);
        } else {
            // Handle the case where no EVENT_ID is provided
            Toast.makeText(this, "Invalid event ID", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Set click listeners for date and time buttons
        dateButton.setOnClickListener(v -> showDatePickerDialog());
        timeButton.setOnClickListener(v -> showTimePickerDialog());

        updateButton.setOnClickListener(v -> updateEvent());
        deleteButton.setOnClickListener(v -> deleteEvent());
    }

    // Method to load event data from the database and populate UI fields
    private void loadEventData(int eventId) {
        // Retrieve event data from the database using LiveData
        eventViewModel.getEventById(eventId).observe(this, event -> {
            if (event != null) {
                titleInput.setText(event.getTitle());
                dateButton.setText(event.getDate());
                timeButton.setText(event.getTime());
                addressInput.setText(event.getAddress());
                postcodeInput.setText(event.getPostcode());
                cityInput.setText(event.getCity());
                notesInput.setText(event.getNotes());
            }
        });
    }

    // Method to update the event in the database
    private void updateEvent() {
        if (TextUtils.isEmpty(titleInput.getText())) {
            setResult(RESULT_CANCELED);
        } else {
            // Retrieve values from EditText fields
            String title = titleInput.getText().toString();
            String date = dateButton.getText().toString();
            String time = timeButton.getText().toString();
            String address = addressInput.getText().toString();
            String postcode = postcodeInput.getText().toString();
            String city = cityInput.getText().toString();
            String notes = notesInput.getText().toString();

            // Get the eventId from the Intent
            int eventId = getIntent().getIntExtra("EVENT_ID", -1);

            // Create an Event object with the updated values
            Event updatedEvent = new Event(title, date, time, address, postcode, city, notes);
            updatedEvent.setId(eventId); // Set the ID to ensure it's recognized as an existing event

            // Call the update method in the ViewModel to update the event in the database
            eventViewModel.update(updatedEvent);

            // Provide feedback (optional)
            Toast.makeText(this, "Event updated successfully", Toast.LENGTH_SHORT).show();

            // Set result and finish the activity
            setResult(RESULT_OK);
        }
    }

    // Method to delete the event from the database with a confirmation dialog
    private void deleteEvent() {
        // Get the eventId from the Intent
        int eventId = getIntent().getIntExtra("EVENT_ID", -1);

        // Show a confirmation dialog before deleting the event
        new AlertDialog.Builder(this)
                .setTitle("Delete Event")
                .setMessage("Are you sure you want to delete this event?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Call the delete method in the ViewModel to delete the event from the database
                        eventViewModel.delete(eventId);
                        // Provide feedback (optional)
                        Toast.makeText(UpdateEventActivity.this, "Event deleted successfully", Toast.LENGTH_SHORT).show();
                        // Set result and finish the activity
                        setResult(RESULT_OK);
                        finish();
                    }
                })
                .setNegativeButton("No", null) // Do nothing if "No" is clicked
                .show();
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
                        // Update the date_input EditText with the selected date
                        dateButton.setText(String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, day));
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
                        // Update the time_input EditText with the selected time
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.US);
                        timeButton.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                },
                hour, minute, true);

        // Show the TimePickerDialog
        timePickerDialog.show();
    }


}