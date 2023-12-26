package uk.ac.aston.cs3mdd.planaheadmobileapplication;

import static uk.ac.aston.cs3mdd.planaheadmobileapplication.MainActivity.TAG;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import uk.ac.aston.cs3mdd.planaheadmobileapplication.databinding.FragmentWeatherBinding;
import uk.ac.aston.cs3mdd.planaheadmobileapplication.model.LocationViewModel;
import uk.ac.aston.cs3mdd.planaheadmobileapplication.weather.ForecastAdapter;
import uk.ac.aston.cs3mdd.planaheadmobileapplication.weather.WeatherResponse;
import uk.ac.aston.cs3mdd.planaheadmobileapplication.weather.WeatherViewModel;

public class WeatherFragment extends Fragment {
    private FragmentWeatherBinding binding;
    private WeatherViewModel weatherViewModel;
    private TextView cityNameTextView, temperatureTextView, descriptionTextView, dateTimeTextView;
    private EditText searchEditText;
    private Button searchButton, clearButton, useCurrentLocationButton;
    private ImageView weatherIconImageView;
    private LocationViewModel locationViewModel;
    private RecyclerView forecastRecyclerView;
    private ForecastAdapter forecastAdapter;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentWeatherBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cityNameTextView = binding.cityNameTextView;
        temperatureTextView = binding.temperatureTextView;
        descriptionTextView = binding.descriptionTextView;
        dateTimeTextView = binding.dateTimeTextView;
        weatherIconImageView = binding.weatherIconImageView;
        searchEditText = binding.searchEditText;
        searchButton = binding.searchButton;
        clearButton = binding.clearButton;
        forecastRecyclerView = binding.forecastRecyclerView;

        //ViewModels
        weatherViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);
        locationViewModel = new ViewModelProvider(requireActivity()).get(LocationViewModel.class);

        // RecyclerView for forecast data
        forecastAdapter = new ForecastAdapter();
        forecastRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        forecastRecyclerView.setAdapter(forecastAdapter);

        // Check if the current location is available and fetch weather automatically
        Location currentLocation = locationViewModel.getCurrentLocation().getValue();
        if (currentLocation != null) {
            fetchWeather(currentLocation.getLatitude(), currentLocation.getLongitude());
        }

        // Clear search bar
        clearButton.setOnClickListener(v -> searchEditText.getText().clear());

        // The button to use the user current location
        useCurrentLocationButton = binding.useCurrentLocationButton;
        useCurrentLocationButton.setOnClickListener(v -> {
            try {
                useCurrentLocation();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Search bar
        searchButton.setOnClickListener(v -> {
            String cityName = searchEditText.getText().toString();
            if (!cityName.isEmpty()) {
                double[] coordinates = getCoordinatesFromCityName(cityName);
                if (coordinates != null) {
                    double latitude = coordinates[0];
                    double longitude = coordinates[1];
                    fetchWeather(latitude, longitude);
                } else {
                    Toast.makeText(requireContext(), R.string.coordinates_null, Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Coordinates are null for the given city name");
                }
            } else {
                Toast.makeText(requireContext(), R.string.empty_city_name, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "location name is empty");
            }
        });
    }

    //Method to fetch weather data
    private void fetchWeather(double latitude, double longitude) {
        String apiKey = getApiKey(requireContext());
        weatherViewModel.getCurrentWeather(latitude, longitude, apiKey).observe(getViewLifecycleOwner(), weatherResponse -> {
            if (weatherResponse != null) {
                // Display current weather data
               displayCurrentWeatherData(weatherResponse);

                // Fetch and display weather forecast
                weatherViewModel.getWeatherForecast(latitude, longitude, apiKey).observe(getViewLifecycleOwner(), forecast -> {
                    if (forecast != null && forecast.getList() != null) {
                        displayForecastData(forecast.getList());
                    } else {
                        Log.e(TAG, "Forecast data response is null");
                    }
                });
            } else {
                Toast.makeText(requireContext(), R.string.error_fetching_weather, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Display current weather data
    private void displayCurrentWeatherData(WeatherResponse weatherResponse) {
        cityNameTextView.setText(weatherResponse.name);
        temperatureTextView.setText(String.format(Locale.getDefault(), "%.0f °C", weatherResponse.main.temp - 273.15));
        descriptionTextView.setText(weatherResponse.weather.get(0).description);

        // Convert timestamp to local time
        double timezoneOffset = weatherResponse.timezone / 3600.0;
        long utcTimestamp = System.currentTimeMillis() / 1000L;
        long localTimestamp = utcTimestamp + (long) (timezoneOffset * 3600);
        String localTime = new SimpleDateFormat("EEEE, MMMM dd, yyyy HH:mm:ss", Locale.getDefault())
                .format(new Date(localTimestamp * 1000L));
        dateTimeTextView.setText(localTime);

        // Load weather icon using Picasso library
        String iconUrl = "https://openweathermap.org/img/w/" + weatherResponse.weather.get(0).icon + ".png";
        Picasso.get().load(iconUrl).into(weatherIconImageView);

        // Display min and max temperatures
        TextView tempMinTextView = binding.getRoot().findViewById(R.id.tempMinTextView);
        TextView tempMaxTextView = binding.getRoot().findViewById(R.id.tempMaxTextView);
        tempMinTextView.setText(String.format(Locale.getDefault(), "L: %.0f °C", weatherResponse.main.temp_min - 273.15));
        tempMaxTextView.setText(String.format(Locale.getDefault(), "H: %.0f °C", weatherResponse.main.temp_max - 273.15));
    }

    // Display weather forecast data
    private void displayForecastData(List<WeatherResponse> forecastData) {
        forecastAdapter.setForecastData(forecastData);
    }

    // Get latitude and longitude from a city name using Geocoder
    private double[] getCoordinatesFromCityName(String cityName) {
        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(cityName, 1);
            if (addresses != null && addresses.size() > 0) {
                double latitude = addresses.get(0).getLatitude();
                double longitude = addresses.get(0).getLongitude();
                return new double[]{latitude, longitude};
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get OpenWeatherMap API key from the app's metadata
    private String getApiKey(Context context) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = applicationInfo.metaData;
            return bundle.getString("WEATHER.API_KEY");
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void useCurrentLocation() {
        locationViewModel.useCurrentLocation(searchEditText, requireContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set the search bar text to the current location
        locationViewModel.setSearchBarWithCurrentLocation(requireContext(), searchEditText);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}


