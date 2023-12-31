package uk.ac.aston.cs3mdd.planaheadmobileapplication.places;

import retrofit2.Call;
import android.util.Log;

import uk.ac.aston.cs3mdd.planaheadmobileapplication.MainActivity;


// Repository class
public class PlacesRepository {
    private GetNearbyPlaces nearbyPlacesService;

    public PlacesRepository(GetNearbyPlaces placesService) {
        this.nearbyPlacesService = placesService;
    }

    public Call<PlacesList> getNearbyPlaces(String latLon, int radius, String type) {
        Log.i(MainActivity.TAG, "Places key is " + SingletonData.getInstance().getPlacesKey());
        String fields = "name,icon,place_id,rating,geometry";
        return nearbyPlacesService.getNearbyPlaces(latLon, radius, type, fields, SingletonData.getInstance().getPlacesKey());
    }
}


