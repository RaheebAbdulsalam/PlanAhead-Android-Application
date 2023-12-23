package uk.ac.aston.cs3mdd.planaheadmobileapplication.model;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import uk.ac.aston.cs3mdd.planaheadmobileapplication.MainActivity;
import uk.ac.aston.cs3mdd.planaheadmobileapplication.R;


public class LocationViewModel extends ViewModel {
    private MutableLiveData<Location> currentLocation;
    private MutableLiveData<Boolean> locationUpdates;

    public LocationViewModel() {
        currentLocation = new MutableLiveData<>(null);
        locationUpdates = new MutableLiveData<>(false);
    }

    public LiveData<Location> getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location mCurrentLocation) {
        this.currentLocation.setValue(mCurrentLocation);
    }

    public LiveData<Boolean> getLocationUpdates() {
        return locationUpdates;
    }

    public void setLocationUpdates(Boolean locationUpdates) {
        this.locationUpdates.setValue(locationUpdates);
    }


    public void setSearchBarWithCurrentLocation(Context context, EditText searchEditText) {
        // Check if the location data is available in the ViewModel
        Location currentLocation = getCurrentLocation().getValue();
        if (currentLocation != null) {
            // Use Geocoder to get the address from latitude and longitude
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
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
                    searchEditText.setText(addressFormatted);
                } else {
                    Toast.makeText(context, R.string.address_not_found_for_current_location, Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                Toast.makeText(context, R.string.error_retrieving_address + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(MainActivity.TAG, "Error getting address\n"+e.getMessage());
            }
        } else {
            Toast.makeText(context, R.string.current_location_not_available, Toast.LENGTH_SHORT).show();
        }
    }

}
