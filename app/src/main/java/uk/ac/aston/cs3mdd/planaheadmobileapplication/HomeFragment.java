package uk.ac.aston.cs3mdd.planaheadmobileapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import uk.ac.aston.cs3mdd.planaheadmobileapplication.events.Event;
import uk.ac.aston.cs3mdd.planaheadmobileapplication.events.EventAdapter;
import uk.ac.aston.cs3mdd.planaheadmobileapplication.events.EventViewModel;

public class HomeFragment extends Fragment implements EventAdapter.EventClickCallback {
    private EventViewModel eventViewModel;
    private EventAdapter eventAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Initialize EventAdapter with the click callback
        eventAdapter = new EventAdapter(new EventAdapter.EventDiff(), this);
        recyclerView.setAdapter(eventAdapter);

        // Initialize EventViewModel
        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);

        // Observe LiveData and update RecyclerView
        eventViewModel.getAllEvents().observe(getViewLifecycleOwner(), events -> {
            // Update the UI with the new list of events
            eventAdapter.submitList(events);
        });

        //search and filter
        EditText editTextSearch = view.findViewById(R.id.editTextSearch);
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

        return view;
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
