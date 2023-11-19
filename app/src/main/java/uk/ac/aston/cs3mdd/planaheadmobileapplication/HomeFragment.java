package uk.ac.aston.cs3mdd.planaheadmobileapplication;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import uk.ac.aston.cs3mdd.planaheadmobileapplication.databinding.FragmentHomeBinding;
import uk.ac.aston.cs3mdd.planaheadmobileapplication.events.CustomAdapter;
import uk.ac.aston.cs3mdd.planaheadmobileapplication.events.Database;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;
    private Database db;
    private ArrayList<String> event_id, event_title, event_date, event_time, event_location, event_notes;
    private CustomAdapter customAdapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);

        db = new Database(requireContext());
        event_id = new ArrayList<>();
        event_title = new ArrayList<>();
        event_date = new ArrayList<>();
        event_time = new ArrayList<>();
        event_location = new ArrayList<>();
        event_notes = new ArrayList<>();
        customAdapter = new CustomAdapter(requireContext(), event_id, event_title, event_date, event_time, event_location, event_notes);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        storeDataInArrays();
    }

    public void storeDataInArrays() {
        Cursor cursor = db.readAllData();
        if (cursor.getCount() == 0) {
            Toast.makeText(requireContext(), "No data.", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                event_id.add(cursor.getString(0));
                event_title.add(cursor.getString(1));
                event_date.add(cursor.getString(2));
                event_time.add(cursor.getString(3));
                event_location.add(cursor.getString(4));
                event_notes.add(cursor.getString(5));
            }
            customAdapter.notifyDataSetChanged(); // Notify the adapter that the data has changed
        }
    }

    // This method is called when the view associated with the fragment is being destroyed.
    // It releases any resources associated with the view.
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
