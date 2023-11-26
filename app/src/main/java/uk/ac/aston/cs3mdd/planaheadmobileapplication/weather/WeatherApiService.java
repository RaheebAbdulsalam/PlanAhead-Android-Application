package uk.ac.aston.cs3mdd.planaheadmobileapplication.weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

public interface WeatherApiService {

    @GET("weather/{city}")
    Call<WeatherResponse> getWeather(@Path("city") String city);

    @GET("forecast/{city}")
    Call<List<Forecast>> getForecast(@Path("city") String city);

    // Other methods as needed
}
