package uk.ac.aston.cs3mdd.planaheadmobileapplication.model;

import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


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

    // Method to observe changes in the current location
//    public LiveData<Location> observeCurrentLocation() {
//        return currentLocation;
//    }
//
//    // Method to update the current location
//    public void updateCurrentLocation(Location newLocation) {
//        currentLocation.setValue(newLocation);
//    }
}
