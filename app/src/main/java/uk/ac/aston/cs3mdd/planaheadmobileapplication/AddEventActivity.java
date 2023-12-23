package uk.ac.aston.cs3mdd.planaheadmobileapplication;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import uk.ac.aston.cs3mdd.planaheadmobileapplication.events.Event;
import uk.ac.aston.cs3mdd.planaheadmobileapplication.events.EventViewModel;

public class AddEventActivity extends AppCompatActivity {
    private EditText title_input, address_input, postcode_input, city_input, notes_input;
    private Button date_button, time_button, save_button;
    private EventViewModel eventViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        // Initialising UI elements
        title_input = findViewById(R.id.title_input);
        date_button = findViewById(R.id.date_button);
        time_button = findViewById(R.id.time_button);
        address_input = findViewById(R.id.address_input);
        postcode_input = findViewById(R.id.postcode_input);
        city_input = findViewById(R.id.city_input);
        notes_input = findViewById(R.id.notes_input);

        // Initialize save_button
        save_button = findViewById(R.id.save_button);

        // Initialize EventRepository
        eventViewModel = new EventViewModel(getApplication());

        // Set click listeners for date and time buttons
        date_button.setOnClickListener(v -> showDatePickerDialog());
        time_button.setOnClickListener(v -> showTimePickerDialog());

        save_button.setOnClickListener(view -> saveEvent());
    }

    private void saveEvent() {
        // Check if the title is empty; if yes, do not save
        String eventTitle=title_input.getText().toString();
        String eventPostcode = postcode_input.getText().toString();

        if (eventTitle.isEmpty()) {
            title_input.setError("Event's Title is required");
            return;
        }
        if (!isValidUKPostcode(eventPostcode)) {
            postcode_input.setError("Enter a valid postcode");
            return;
        }

            // Retrieve values from EditText fields
            String title = title_input.getText().toString();
            String date = date_button.getText().toString();
            String time = time_button.getText().toString();
            String address = address_input.getText().toString();
            String postcode = postcode_input.getText().toString();
            String city = city_input.getText().toString();
            String notes = notes_input.getText().toString();

            // Create an Event object
            Event event = new Event(title, date, time, address, postcode, city, notes);

            // Insert the event into the database using the repository
            eventViewModel.insert(event);

            Toast.makeText(this, R.string.event_added_successfully, Toast.LENGTH_SHORT).show();

            // Set the result to RESULT_OK
            setResult(RESULT_OK);

        // Finish the activity
        finish();
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
                        // Update the time_input EditText with the selected time
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


    // Method to validate UK postcode format using a regular expression
    private boolean isValidUKPostcode(String postcode) {
        String ukPostcodeRegex = "(?i)^([A-Z]{1,2}[0-9R][0-9A-Z]? [0-9][ABD-HJLNP-UW-Z]{2}|[A-Z]{1,2}[0-9R][0-9A-Z]?)$";
        return postcode.matches(ukPostcodeRegex);
    }

}
