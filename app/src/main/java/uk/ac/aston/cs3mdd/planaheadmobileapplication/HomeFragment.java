package uk.ac.aston.cs3mdd.planaheadmobileapplication;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import uk.ac.aston.cs3mdd.planaheadmobileapplication.databinding.FragmentHomeBinding;
import uk.ac.aston.cs3mdd.planaheadmobileapplication.events.Database;
import uk.ac.aston.cs3mdd.planaheadmobileapplication.events.EventAdapter;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;
    private Database db;
    private ArrayList<String> event_id, event_title, event_date, event_time, event_address, event_postcode, event_city, event_notes;
    public EventAdapter eventAdapter;

    private EditText editTextSearch; // Added EditText for search

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize RecyclerView and Database
        recyclerView = view.findViewById(R.id.recyclerView);
        editTextSearch = view.findViewById(R.id.editTextSearch); // Initialize EditText
        db = new Database(requireContext());

        // Initialize ArrayLists for event details and create an EventAdapter
        event_id = new ArrayList<>();
        event_title = new ArrayList<>();
        event_date = new ArrayList<>();
        event_time = new ArrayList<>();
        event_address = new ArrayList<>();
        event_postcode = new ArrayList<>();
        event_city = new ArrayList<>();
        event_notes = new ArrayList<>();
        eventAdapter = new EventAdapter(requireContext(), event_id, event_title, event_date, event_time, event_address, event_postcode, event_city, event_notes);

        // Set the adapter and layout manager for the RecyclerView
        recyclerView.setAdapter(eventAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Fetch the arrays with data from the database
        storeDataInArrays();

        // Set up the TextWatcher for EditText (search)
        setupSearchTextListener();
    }

    // Method to retrieve data from the database
    public void storeDataInArrays() {
        Cursor cursor = db.readAllData();
        if (cursor.getCount() == 0) {
            Toast.makeText(requireContext(), "No data.", Toast.LENGTH_SHORT).show();
        } else {
            // Clear existing data before populating the arrays
            event_id.clear();
            event_title.clear();
            event_date.clear();
            event_time.clear();
            event_address.clear();
            event_postcode.clear();
            event_city.clear();
            event_notes.clear();
            while (cursor.moveToNext()) {
                // Add data from the cursor to the arrays
                event_id.add(cursor.getString(0));
                event_title.add(cursor.getString(1));
                event_date.add(cursor.getString(2));
                event_time.add(cursor.getString(3));
                event_address.add(cursor.getString(4));
                event_postcode.add(cursor.getString(5));
                event_city.add(cursor.getString(6));
                event_notes.add(cursor.getString(7));
            }
            // Notify the adapter that the data has changed
            eventAdapter.notifyDataSetChanged();
        }
    }
// Search and Filter Events by their titles //
    // Method to set up TextWatcher for EditText (search)
    private void setupSearchTextListener() {
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Handle search query change
                filter(charSequence.toString());
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    // Method to filter the data based on the search query
    private void filter(String query) {
        Cursor cursor;
        if (query.isEmpty()) {
            // If the query is empty, load all data
            cursor = db.readAllData();
        } else {
            // If there is a search query, load filtered data
            cursor = db.searchData(query);
        }
        // Clear existing data before populating the arrays
        event_id.clear();
        event_title.clear();

        // Populate the arrays with data from the cursor
        while (cursor.moveToNext()) {
            // Add data from the cursor to the arrays
            event_id.add(cursor.getString(0));
            event_title.add(cursor.getString(1));
        }
        // Notify the adapter that the data has changed
        eventAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
