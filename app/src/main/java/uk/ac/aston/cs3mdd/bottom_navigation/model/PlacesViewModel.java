package uk.ac.aston.cs3mdd.bottom_navigation.model;

import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PlacesViewModel extends ViewModel {


    private MutableLiveData<Location> currentLocation;

    private PlacesViewModel() {
        super();
        currentLocation = new MutableLiveData<>(null);
    }

    public LiveData<Location> getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location mCurrentLocation) {
        this.currentLocation.setValue(mCurrentLocation);
    }
}


