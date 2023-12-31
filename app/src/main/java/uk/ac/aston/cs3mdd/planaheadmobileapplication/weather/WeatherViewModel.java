package uk.ac.aston.cs3mdd.planaheadmobileapplication.weather;

import static uk.ac.aston.cs3mdd.planaheadmobileapplication.MainActivity.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherViewModel extends ViewModel {

    MutableLiveData<WeatherResponse> weatherData;
    private final WeatherRepository weatherRepository;

    public WeatherViewModel() {
        weatherRepository = new WeatherRepository();
    }

    public LiveData<WeatherResponse> getCurrentWeather(double latitude, double longitude, String apiKey) {
        weatherData = new MutableLiveData<>();

        weatherRepository.getCurrentWeather(latitude, longitude, apiKey).enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    weatherData.setValue(response.body());
                } else {
                    Log.e(TAG, "Failed to fetch current weather: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "Failed to fetch current weather", t);
            }
        });
        return weatherData;
    }

    public LiveData<Forecast> getWeatherForecast(double latitude, double longitude, String apiKey) {
        MutableLiveData<Forecast> forecastData = new MutableLiveData<>();

        weatherRepository.getWeatherForecast(latitude, longitude, apiKey).enqueue(new Callback<Forecast>() {
            @Override
            public void onResponse(@NonNull Call<Forecast> call, @NonNull Response<Forecast> response) {
                if (response.isSuccessful()) {
                    forecastData.setValue(response.body());
                } else {
                    Log.e(TAG, "Failed to fetch weather forecast: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Forecast> call, @NonNull Throwable t) {
                Log.e(TAG, "Failed to fetch weather forecast", t);
            }
        });

        return forecastData;
    }

}
