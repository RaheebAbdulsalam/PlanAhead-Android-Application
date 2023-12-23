package uk.ac.aston.cs3mdd.planaheadmobileapplication;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.ac.aston.cs3mdd.planaheadmobileapplication.databinding.FragmentPlacesBinding;
import uk.ac.aston.cs3mdd.planaheadmobileapplication.model.LocationViewModel;
import uk.ac.aston.cs3mdd.planaheadmobileapplication.places.MyPlace;
import uk.ac.aston.cs3mdd.planaheadmobileapplication.places.PlaceListAdapter;
import uk.ac.aston.cs3mdd.planaheadmobileapplication.places.PlacesViewModel;
import uk.ac.aston.cs3mdd.planaheadmobileapplication.places.GetNearbyPlaces;
import uk.ac.aston.cs3mdd.planaheadmobileapplication.places.PlacesRepository;

public class PlacesFragment extends Fragment {
    private PlacesViewModel viewModel;
    private FragmentPlacesBinding binding;
    private RecyclerView mRecyclerView;
    private PlaceListAdapter mAdapter;
    private EditText searchLocationEditText;
    private Button searchButton, useCurrentLocationButton,clearButton;
    private GetNearbyPlaces service;
    private Retrofit retrofit;
    private Spinner placeTypeSpinner;
    private LocationViewModel locationViewModel;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        viewModel = new ViewModelProvider(requireActivity()).get(PlacesViewModel.class);
        binding = FragmentPlacesBinding.inflate(inflater, container, false);
        locationViewModel = new ViewModelProvider(requireActivity()).get(LocationViewModel.class);

        View view = binding.getRoot();

        // Initialize views
        searchLocationEditText = binding.searchLocationEditText;
        searchButton = binding.searchButton;
        clearButton=binding.clearButton;
        placeTypeSpinner = binding.placeTypeSpinner;

        // Set up the Spinner with the array of place types
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.place_types,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        placeTypeSpinner.setAdapter(adapter);


        //clear search bar
        clearButton.setOnClickListener(v -> {
            // Clear the search bar
            searchLocationEditText.getText().clear();
        });

        // Set onClickListener for the search button
        searchButton.setOnClickListener(v -> {
            // Get the user's input
            String location = searchLocationEditText.getText().toString();
            // Perform the search
            performSearch(location);
        });

        // The button to use the user current location
        useCurrentLocationButton = binding.useCurrentLocationButton;
        useCurrentLocationButton.setOnClickListener(v -> {
            try {
                useCurrentLocation();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        // Create the Retrofit instance
        retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/place/nearbysearch/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create the service instance
        service = retrofit.create(GetNearbyPlaces.class);

        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Create the observer which updates the UI.
        final Observer<List<MyPlace>> userListObserver = new Observer<List<MyPlace>>() {
            @Override
            public void onChanged(@Nullable final List<MyPlace> placeList) {
                // Update the UI, in this case, a Toast.
                assert placeList != null;
                Toast.makeText(getContext(),
                        "We got a list of " + placeList.size() + " places",
                        Toast.LENGTH_LONG).show();
                mAdapter.updateData(placeList);
            }
        };

        // Get a handle to the RecyclerView.
        mRecyclerView = view.findViewById(R.id.recyclerview);
        // Create an adapter and supply the data to be displayed.
        mAdapter = new PlaceListAdapter(getContext(), viewModel.getAllPlaces().getValue());
        // Connect the adapter with the RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        // Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel.getAllPlaces().observe(getViewLifecycleOwner(), userListObserver);
    }

    private void performSearch(String location) {
        viewModel.getAllPlaces().getValue().clear();
        // Get the selected place type from the Spinner
        String selectedPlaceType = placeTypeSpinner.getSelectedItem().toString();

        // Use the provided location and place type for the search
        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocationName(location, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                double latitude = address.getLatitude();
                double longitude = address.getLongitude();

                // Perform the search using the obtained latitude, longitude, and selected place type
                viewModel.requestNearbyPlaces(new PlacesRepository(service), latitude + "," + longitude, 5000, selectedPlaceType);

                // Display the formatted address in the UI
                String addressFormatted = address.getAddressLine(0) + "\n" ;
                binding.searchLocationEditText.setText(addressFormatted);
            } else {
                Toast.makeText(getContext(), R.string.no_places_found, Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Toast.makeText(getContext(), R.string.search_error + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    //Method to use the user current location and adding it to the search bar
    private void useCurrentLocation() {
        // Check if the location data is available in the ViewModel
        Location currentLocation = locationViewModel.getCurrentLocation().getValue();
        if (currentLocation != null) {
            // Use Geocoder to get the address from latitude and longitude
            Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = geocoder.getFromLocation(
                        currentLocation.getLatitude(),
                        currentLocation.getLongitude(),
                        1
                );
                if (addresses != null && !addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    // Get the formatted address
                    String addressFormatted = address.getAddressLine(0) + "\n";
                    // Set the current address in the search bar
                    searchLocationEditText.setText(addressFormatted);
                } else {
                    Toast.makeText(getContext(), R.string.address_not_found_for_current_location, Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                Toast.makeText(getContext(), R.string.error_retrieving_address + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(MainActivity.TAG, "Error getting address\n"+e.getMessage());
            }
        } else {
            Toast.makeText(getContext(), R.string.current_location_not_available, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        // Check if the current location is available and update the search bar
        locationViewModel.setSearchBarWithCurrentLocation(requireContext(), searchLocationEditText);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
