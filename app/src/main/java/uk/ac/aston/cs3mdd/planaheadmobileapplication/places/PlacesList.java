package uk.ac.aston.cs3mdd.planaheadmobileapplication.places;

import androidx.annotation.NonNull;

import java.util.ArrayList;


//This class represents a list of MyPlace objects along with a status.
public class PlacesList {
    private ArrayList<MyPlace> results;
    private String status;

    public ArrayList<MyPlace> getResults() {
        return results;
    }

    public void setResults(ArrayList<MyPlace> results) {
        this.results = results;
    }

    @NonNull
    @Override
    public String toString() {
        return getClass().getName() + " with " + (results == null ? " no places " : results.size()) + " Places " +
                " Status is: " + getStatus();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
