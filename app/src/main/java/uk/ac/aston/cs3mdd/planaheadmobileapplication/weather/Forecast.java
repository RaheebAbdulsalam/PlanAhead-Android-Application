package uk.ac.aston.cs3mdd.planaheadmobileapplication.weather;

import com.google.gson.annotations.SerializedName;

public class Forecast {

    private int day;

    private String temperature;

    private String wind;

    // Constructors, getters, and setters

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }
}
