package uk.ac.aston.cs3mdd.planaheadmobileapplication.places;

import android.content.Context;


// A singleton class that holds shared data such as API keys
public class SingletonData {
    private static SingletonData _INSTANCE;
    private Context context;
    private String apiKey;
    private String placesKey;

    private SingletonData() {
        this.context = MyApplication.getAppContext();
    }

    public static SingletonData getInstance() {
        if (_INSTANCE == null) {
            _INSTANCE = new SingletonData();
        }
        return _INSTANCE;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getPlacesKey() {
        return placesKey;
    }

    public void setPlacesKey(String placesKey) {
        this.placesKey = placesKey;
    }
}