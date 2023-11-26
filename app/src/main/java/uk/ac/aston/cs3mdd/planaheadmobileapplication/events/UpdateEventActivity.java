package uk.ac.aston.cs3mdd.planaheadmobileapplication.events;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import uk.ac.aston.cs3mdd.planaheadmobileapplication.HomeFragment;
import uk.ac.aston.cs3mdd.planaheadmobileapplication.R;

public class UpdateEventActivity extends AppCompatActivity {
    EditText title_input, date_input, time_input, location_input, notes_input;
    Button update_button, delete_button;
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
        delete_button=findViewById(R.id.delete_button);

        getIntentDataEvent();

        //Setting the Event Title as the action bar title when clicking on an event
        ActionBar actionBar=getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle(title);
        }

        //Update Button
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Database db = new Database(UpdateEventActivity.this);
                title = title_input.getText().toString().trim();
                date = date_input.getText().toString().trim();
                time = time_input.getText().toString().trim();
                location = location_input.getText().toString().trim();
                notes = notes_input.getText().toString().trim();
                db.updateEvent(id, title, date, time, location, notes);
            }
        });

        //Delete Button
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmMessage();
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


    //A dialog message when user clicks on delete button
    public void confirmMessage(){
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle(" Delete " + title + " ? ");
        builder.setMessage(" Are you sure you want to delete "+ title + " ? ");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Database db= new Database(UpdateEventActivity.this);
                db.deleteEvent(id);
                // Notify HomeFragment to refresh the data
                ((HomeFragment)getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment)).storeDataInArrays();
                finish();

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

}
