package uk.ac.aston.cs3mdd.planaheadmobileapplication.places;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.ac.aston.cs3mdd.planaheadmobileapplication.MainActivity;


public class PlacesViewModel extends ViewModel {

    private MutableLiveData<List<MyPlace>> allPlaces;

    public PlacesViewModel() {
        super();
        allPlaces = new MutableLiveData<List<MyPlace>>(new ArrayList<MyPlace>());
    }

    public LiveData<List<MyPlace>> getAllPlaces() {
        return allPlaces;
    }

    public void requestNearbyPlaces(PlacesRepository placesRepository, String loc, int radius, String type) {
        if (allPlaces.getValue().size() == 0) {
            Call<PlacesList> userCall = placesRepository.getNearbyPlaces(loc, radius, type);
            userCall.enqueue(new Callback<PlacesList>() {
                @Override
                public void onResponse(@NonNull Call<PlacesList> call, @NonNull Response<PlacesList> response) {
                    if (response.isSuccessful()) {
                        Log.i(MainActivity.TAG, "The response is " + response.body().toString());
                        addAll(response.body());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<PlacesList> call, @NonNull Throwable t) {
                    // show error message to user
                    Log.i(MainActivity.TAG, "Error: " + t.toString());
                }
            });
        } else {
            Log.i("AJB", "Already got a list of users, not getting any more");
        }
    }

    private void addAll(PlacesList list) {
        allPlaces.setValue(list.getResults());
        for (MyPlace place : allPlaces.getValue()) {
            Log.i("AJB", place.toString());
        }
    }


}