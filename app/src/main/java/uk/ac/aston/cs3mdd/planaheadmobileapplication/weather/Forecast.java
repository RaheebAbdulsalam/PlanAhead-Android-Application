package uk.ac.aston.cs3mdd.planaheadmobileapplication.weather;

import java.util.List;

public class Forecast {

    public List<WeatherResponse> list;

    public List<WeatherResponse> getList() {
        return list;
    }

    public void setList(List<WeatherResponse> list) {
        this.list = list;
    }

}
