package uk.ac.aston.cs3mdd.planaheadmobileapplication;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import uk.ac.aston.cs3mdd.planaheadmobileapplication.events.Event;
import uk.ac.aston.cs3mdd.planaheadmobileapplication.events.EventViewModel;

// Activity class for adding a new event
public class AddEventActivity extends AppCompatActivity {
    private EditText title_input,  address_input, postcode_input, city_input, notes_input;
    private Button date_button, time_button, save_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        // Initialising the layout elements by finding them using their IDs
        title_input = findViewById(R.id.title_input);
        date_button = findViewById(R.id.date_button);
        time_button= findViewById(R.id.time_button);
        address_input = findViewById(R.id.address_input);
        postcode_input = findViewById(R.id.postcode_input);
        city_input = findViewById(R.id.city_input);
        notes_input = findViewById(R.id.notes_input);
        save_button = findViewById(R.id.save_button);

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

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventViewModel eventViewModel = new EventViewModel(AddEventActivity.this.getApplication());

                Event newEvent = new Event(
                        null,
                        title_input.getText().toString().trim(),
                        date_button.getText().toString().trim(),
                        time_button.getText().toString().trim(),
                        address_input.getText().toString().trim(),
                        postcode_input.getText().toString().trim(),
                        city_input.getText().toString().trim(),
                        notes_input.getText().toString().trim()
                );

                // Add a new event to the database
                eventViewModel.addEvent(newEvent);

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


}
