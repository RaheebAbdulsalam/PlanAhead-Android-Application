package uk.ac.aston.cs3mdd.planaheadmobileapplication.events;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import uk.ac.aston.cs3mdd.planaheadmobileapplication.R;

public class AddEventActivity extends AppCompatActivity {
    EditText title_input, date_input, time_input, location_input, notes_input;
    Button save_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        title_input = findViewById(R.id.title_input);
        date_input = findViewById(R.id.date_input);
        time_input = findViewById(R.id.time_input);
        location_input = findViewById(R.id.location_input);
        notes_input = findViewById(R.id.notes_input);
        save_button = findViewById(R.id.save_button);
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Database myDB = new Database(AddEventActivity.this);
                myDB.addEvent(title_input.getText().toString().trim(),
                        date_input.getText().toString().trim(),
                        time_input.getText().toString().trim(),
                        location_input.getText().toString().trim(),
                        notes_input.getText().toString().trim());
            }
        });
    }
}