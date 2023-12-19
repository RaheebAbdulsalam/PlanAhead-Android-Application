package uk.ac.aston.cs3mdd.planaheadmobileapplication.places;

import androidx.annotation.NonNull;

// This class contains a MyLocation object, representing the geometry of a place
public class Geometry {
    private MyLocation location;

    public MyLocation getLocation() {
        return location;
    }

    public void setLocation(MyLocation location) {
        this.location = location;
    }

    @NonNull
    @Override
    public String toString() {
        return location.toString();
    }
}

