package uk.ac.aston.cs3mdd.planaheadmobileapplication.weather;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherRepository {
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";
    private final GetWeather weatherService;


    public WeatherRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        weatherService = retrofit.create(GetWeather .class);
    }

    public Call<WeatherResponse> getCurrentWeather(double latitude, double longitude, String apiKey) {
        return weatherService.getCurrentWeather(latitude, longitude, apiKey);
    }

    public Call<Forecast> getWeatherForecast(double latitude, double longitude, String apiKey) {
        return weatherService.getWeatherForecast(latitude, longitude, apiKey);
    }

}
