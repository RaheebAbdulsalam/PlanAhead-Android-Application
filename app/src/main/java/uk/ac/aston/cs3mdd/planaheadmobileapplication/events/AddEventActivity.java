package uk.ac.aston.cs3mdd.planaheadmobileapplication.events;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import uk.ac.aston.cs3mdd.planaheadmobileapplication.R;

// Activity class for adding a new event
public class AddEventActivity extends AppCompatActivity {
    EditText title_input, date_input, time_input, address_input,postcode_input,city_input, notes_input;
    Button save_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        // Initialising the layout elements by finding them using their IDs
        title_input = findViewById(R.id.title_input);
        date_input = findViewById(R.id.date_input);
        time_input = findViewById(R.id.time_input);
        address_input = findViewById(R.id.address_input);
        postcode_input = findViewById(R.id.postcode_input);
        city_input = findViewById(R.id.city_input);
        notes_input = findViewById(R.id.notes_input);
        save_button = findViewById(R.id.save_button);
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //New instance of the Database class with the current activity context
                Database myDB = new Database(AddEventActivity.this);
                // Add a new event to the database
                myDB.addEvent(title_input.getText().toString().trim(),
                        date_input.getText().toString().trim(),
                        time_input.getText().toString().trim(),
                        address_input.getText().toString().trim(),
                        postcode_input.getText().toString().trim(),
                        city_input.getText().toString().trim(),
                        notes_input.getText().toString().trim());
            }
        });
    }
}