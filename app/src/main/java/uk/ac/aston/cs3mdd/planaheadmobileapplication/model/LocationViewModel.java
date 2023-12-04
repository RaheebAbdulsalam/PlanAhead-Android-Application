package uk.ac.aston.cs3mdd.planaheadmobileapplication.model;

import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Locale;

public class LocationViewModel extends ViewModel {
    private MutableLiveData<Location> currentLocation;

    private MutableLiveData<Boolean> locationUpdates;

    private LocationViewModel() {
        super();
        currentLocation = new MutableLiveData<>(null);
        locationUpdates = new MutableLiveData<Boolean>(false);
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

}


