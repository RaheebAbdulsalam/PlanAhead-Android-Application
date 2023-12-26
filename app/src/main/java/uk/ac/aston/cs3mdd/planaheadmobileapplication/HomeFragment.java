package uk.ac.aston.cs3mdd.planaheadmobileapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import uk.ac.aston.cs3mdd.planaheadmobileapplication.databinding.FragmentHomeBinding;
import uk.ac.aston.cs3mdd.planaheadmobileapplication.events.Event;
import uk.ac.aston.cs3mdd.planaheadmobileapplication.events.EventAdapter;
import uk.ac.aston.cs3mdd.planaheadmobileapplication.events.EventViewModel;

public class HomeFragment extends Fragment implements EventAdapter.EventClickCallback {

    private FragmentHomeBinding binding;
    private EventViewModel eventViewModel;
    private EventAdapter eventAdapter;

    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize RecyclerView
        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Initialize EventAdapter with the click callback
        eventAdapter = new EventAdapter(new EventAdapter.EventDiff(), this);
        recyclerView.setAdapter(eventAdapter);

        // Initialize EventViewModel
        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);

        // Observe LiveData and update RecyclerView
        eventViewModel.getAllEvents().observe(getViewLifecycleOwner(), events -> {
            //Check if there are no events saved
            if (events != null && events.isEmpty()) {
                // show the empty message
                binding.textViewEmpty.setVisibility(View.VISIBLE);
            } else {
                // There are events, hide the empty message
                binding.textViewEmpty.setVisibility(View.INVISIBLE);
            }

            // Update the UI with the new list of events
            eventAdapter.submitList(events);
        });

        //search and filter
        EditText editTextSearch = binding.editTextSearch;
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int after) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int after) {
                String searchQuery = charSequence.toString().trim();
                searchEvents(searchQuery);
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        // Sorting options
        Spinner sortSpinner = binding.sortSpinner;

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.sort_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(adapter);

        // Set a listener for item selections
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                // Handle sorting based on the selected option
                if (position == 0) { // Sort by date (ascending)
                    eventViewModel.getAllEventsByDate().observe(getViewLifecycleOwner(), events -> {
                        eventAdapter.submitList(events);
                    });
                } else if (position == 1) { // Sort by date (descending)
                    eventViewModel.getAllEventsByDateDescending().observe(getViewLifecycleOwner(), events -> {
                        eventAdapter.submitList(events);
                    });
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

    }

    private void searchEvents(String title) {
        eventViewModel.searchEventsByTitle(title).observe(this, events -> {
            eventAdapter.submitList(events);
        });
    }

    @Override
    public void onItemClick(Event event) {
        Intent intent = new Intent(requireContext(), UpdateEventActivity.class);
        intent.putExtra("EVENT_ID", event.getId());
        startActivity(intent);
    }
}
