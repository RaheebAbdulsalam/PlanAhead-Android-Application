package uk.ac.aston.cs3mdd.planaheadmobileapplication;


import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import uk.ac.aston.cs3mdd.planaheadmobileapplication.databinding.FragmentPlacesBinding;
import uk.ac.aston.cs3mdd.planaheadmobileapplication.model.LocationViewModel;

public class PlacesFragment extends Fragment {

    private FragmentPlacesBinding binding;

    private LocationViewModel model;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentPlacesBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        model = new ViewModelProvider(requireActivity()).get(LocationViewModel.class);
        binding.latitude.setText("Not Set");
        binding.longitude.setText("Not Set");
        binding.timestamp.setText("No timestamp");
        model.getCurrentLocation().observe(getViewLifecycleOwner(), loc -> {
            if (loc != null) {
                // Update the UI.
                binding.latitude.setText("" + loc.getLatitude());
                binding.longitude.setText("" + loc.getLongitude());
                Date date = Calendar.getInstance().getTime();
                date.setTime(loc.getTime());
                DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                String strDate = dateFormat.format(date);
                binding.timestamp.setText(strDate);
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(getContext(), Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

                    String addressFormatted = address + "\n" + city + "\n" + state + "\n" +
                            country + "\n" + postalCode + "\n" + knownName;
                    binding.address.setText(addressFormatted);
                } catch (IOException e) {
                    Log.e(MainActivity.TAG, "Error getting address\n"+e.getMessage());
                }
            }


        });

        // a listener to respond to button presses when updating the location
        binding.updatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.setLocationUpdates(!model.getLocationUpdates().getValue());
                if (model.getLocationUpdates().getValue()) {
                    binding.updatesButton.setText("Stop Location Updates");
                    Log.i(MainActivity.TAG, "Location Updates Started");
                } else {
                    binding.updatesButton.setText("Start Location Updates");
                    Log.i(MainActivity.TAG, "Location Updates Stopped");
                }
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}

