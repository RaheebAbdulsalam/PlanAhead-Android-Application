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
import uk.ac.aston.cs3mdd.planaheadmobileapplication.events.Event;
import uk.ac.aston.cs3mdd.planaheadmobileapplication.events.EventAdapter;
import uk.ac.aston.cs3mdd.planaheadmobileapplication.events.EventRepository;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;
    private EventRepository eventRepository;
    private ArrayList<Event> events;
    public EventAdapter eventAdapter;
    private EditText editTextSearch;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        editTextSearch = view.findViewById(R.id.editTextSearch);
        eventRepository = new EventRepository(requireContext());

        events = new ArrayList<>();
        eventAdapter = new EventAdapter(requireContext(), events);

        recyclerView.setAdapter(eventAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        storeDataInArrays();
        setupSearchTextListener();
    }

    public void storeDataInArrays() {
        Cursor cursor = eventRepository.getAllEvents();
        if (cursor.getCount() == 0) {
            Toast.makeText(requireContext(), "No data.", Toast.LENGTH_SHORT).show();
        } else {
            events.clear();
            while (cursor.moveToNext()) {
                Event event = new Event(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7)
                );
                events.add(event);
            }
            eventAdapter.notifyDataSetChanged();
        }
    }

    private void setupSearchTextListener() {
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                filter(charSequence.toString());
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void filter(String query) {
        Cursor cursor;
        if (query.isEmpty()) {
            cursor = eventRepository.getAllEvents();
        } else {
            cursor = eventRepository.searchEvents(query);
        }
        events.clear();
        while (cursor.moveToNext()) {
            Event event = new Event(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7)
            );
            events.add(event);
        }
        eventAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}