package uk.ac.aston.cs3mdd.planaheadmobileapplication.weather;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class WeatherResponse implements Serializable {

    private int day;
    private String temperature;


    private String wind;


    private String description;


    private List<Forecast> forecastList;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Forecast> getForecastList() {
        return forecastList;
    }

    public void setForecastList(List<Forecast> forecastList) {
        this.forecastList = forecastList;
    }


}
