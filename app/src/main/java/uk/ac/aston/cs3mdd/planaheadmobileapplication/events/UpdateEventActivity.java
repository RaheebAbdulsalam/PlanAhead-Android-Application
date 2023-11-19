package uk.ac.aston.cs3mdd.planaheadmobileapplication.events;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import uk.ac.aston.cs3mdd.planaheadmobileapplication.R;

public class UpdateEventActivity extends AppCompatActivity {
    EditText title_input, date_input, time_input, location_input, notes_input;
    Button update_button;
    String id, title, date, time, location, notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_event);

        title_input = findViewById(R.id.title_input_update);
        date_input = findViewById(R.id.date_input_update);
        time_input = findViewById(R.id.time_input_update);
        location_input = findViewById(R.id.location_input_update);
        notes_input = findViewById(R.id.notes_input_update);
        update_button = findViewById(R.id.update_button);

        getIntentDataEvent();

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Database db = new Database(UpdateEventActivity.this);
                title = title_input.getText().toString().trim();
                date = date_input.getText().toString().trim();
                time = time_input.getText().toString().trim();
               location = location_input.getText().toString().trim();
                notes = notes_input.getText().toString().trim();
                db.updateData(id, title, date, time, location, notes);
            }
        });

    }

    public void getIntentDataEvent() {
        if (getIntent().hasExtra("id") && getIntent().hasExtra("title") &&
                getIntent().hasExtra("date") && getIntent().hasExtra("time") &&
                getIntent().hasExtra("location") && getIntent().hasExtra("notes")) {
            //Getting Data from Intent
            id = getIntent().getStringExtra("id");
            title = getIntent().getStringExtra("title");
            date = getIntent().getStringExtra("date");
            time = getIntent().getStringExtra("time");
            location = getIntent().getStringExtra("location");
            notes = getIntent().getStringExtra("notes");

            //Setting Intent Data
            title_input.setText(title);
            date_input.setText(date);
            time_input.setText(time);
            location_input.setText(location);
            notes_input.setText(notes);
        } else {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
    }

}


