package uk.ac.aston.cs3mdd.planaheadmobileapplication.places;

import androidx.annotation.NonNull;


// This Model class represents the latitude and longitude of a location.
public class MyLocation {
    private float lat;
    private float lng;

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    @NonNull
    @Override
    public String toString() {
        return "("+lat+","+ lng +")";
    }
}
