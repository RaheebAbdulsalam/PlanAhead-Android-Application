package uk.ac.aston.cs3mdd.planaheadmobileapplication.services;

import retrofit2.Call;
import android.util.Log;

import uk.ac.aston.cs3mdd.planaheadmobileapplication.MainActivity;
import uk.ac.aston.cs3mdd.planaheadmobileapplication.places.PlacesList;
import uk.ac.aston.cs3mdd.planaheadmobileapplication.places.SingletonData;

public class PlacesRepository {
    private GetNearbyPlaces nearbyPlacesService;

    public PlacesRepository(GetNearbyPlaces placesService) {
        this.nearbyPlacesService = placesService;
    }

    public Call<PlacesList> getNearbyPlaces(String latLon, int radius, String type) {
        Log.i(MainActivity.TAG, "Places key is " + SingletonData.getInstance().getPlacesKey());
        // Fields should be specified based on free data available
        // See https://developers.google.com/maps/documentation/places/web-service/usage-and-billing
        // Make sure the MyPlace class also contains these fields
        String fields = "name,icon,place_id,rating,geometry";
        return nearbyPlacesService.getNearbyPlaces(latLon, radius, type, fields, SingletonData.getInstance().getPlacesKey());
    }
}


