package uk.ac.aston.cs3mdd.planaheadmobileapplication.places;

import androidx.annotation.NonNull;

public class MyPlace {
    private String name;
    private String icon;
    private String place_id;
    private float rating;
    private Geometry geometry;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    // Method to get latitude
    public double getLatitude() {
        if (geometry != null && geometry.getLocation() != null) {
            return geometry.getLocation().getLat();
        }
        return 0.0;
    }

    // Method to get longitude
    public double getLongitude() {
        if (geometry != null && geometry.getLocation() != null) {
            return geometry.getLocation().getLng();
        }
        return 0.0;
    }

    @NonNull
    @Override
    public String toString() {
        return "Place called " + name + " at " + geometry.toString();
    }
}
