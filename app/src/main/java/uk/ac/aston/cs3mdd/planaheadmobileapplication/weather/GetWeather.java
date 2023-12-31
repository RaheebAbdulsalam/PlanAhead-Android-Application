package uk.ac.aston.cs3mdd.planaheadmobileapplication.weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

//Retrofit interface for defining the API endpoint and parameters for fetching current weather and forecast for 5 days 3 hours
public interface GetWeather {
    @GET("weather")
    Call<WeatherResponse> getCurrentWeather(
            @Query("lat") double latitude,
            @Query("lon") double longitude,
            @Query("appid") String apiKey
    );


    @GET("forecast")
    Call<Forecast> getWeatherForecast(
            @Query("lat") double latitude,
            @Query("lon") double longitude,
            @Query("appid") String apiKey
    );
}
