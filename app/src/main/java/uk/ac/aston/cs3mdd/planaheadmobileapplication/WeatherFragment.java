package uk.ac.aston.cs3mdd.planaheadmobileapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.ac.aston.cs3mdd.planaheadmobileapplication.databinding.FragmentWeatherBinding;
import uk.ac.aston.cs3mdd.planaheadmobileapplication.weather.WeatherApiService;
import uk.ac.aston.cs3mdd.planaheadmobileapplication.weather.WeatherResponse;

public class WeatherFragment extends Fragment {
    private FragmentWeatherBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentWeatherBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Create Retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://goweather.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create the API service
        WeatherApiService apiService = retrofit.create(WeatherApiService.class);

        // Get references to UI elements
        EditText cityEditText = view.findViewById(R.id.cityEditText);
        Button searchButton = view.findViewById(R.id.searchButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = cityEditText.getText().toString();
                if (!city.isEmpty()) {
                    // Make the API call with the user-inputted city
                    Call<WeatherResponse> call = apiService.getWeather(city);

                    // Make the API call
                    call.enqueue(new Callback<WeatherResponse>() {
                        @Override
                        public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                            if (response.isSuccessful()) {
                                WeatherResponse weatherResponse = response.body();
                                // Handle the response data here
                                updateUI(weatherResponse);
                            } else {
                                Log.e("WeatherFragment", "Weather API error: " + response.message());
                            }
                        }

                        @Override
                        public void onFailure(Call<WeatherResponse> call, Throwable t) {
                            Log.e("WeatherFragment", "Weather API failure", t);
                        }
                    });
                }
            }
        });
    }

    private void updateUI(WeatherResponse weatherResponse) {
        // Update UI elements with the weather data
        binding.temperatureTextView.setText(weatherResponse.getTemperature());
        binding.windTextView.setText(weatherResponse.getWind());
        binding.descriptionTextView.setText(weatherResponse.getDescription());
        // Update other UI elements as needed
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

