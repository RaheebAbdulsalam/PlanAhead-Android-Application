package uk.ac.aston.cs3mdd.planaheadmobileapplication.weather;

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
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    weatherData.setValue(response.body());
                } else {
                    // Handle error
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                // Handle failure
            }
        });

        return weatherData;
    }
}
