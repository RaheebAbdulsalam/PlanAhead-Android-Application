package uk.ac.aston.cs3mdd.planaheadmobileapplication;

import static uk.ac.aston.cs3mdd.planaheadmobileapplication.MainActivity.TAG;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
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

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import uk.ac.aston.cs3mdd.planaheadmobileapplication.databinding.FragmentWeatherBinding;
import uk.ac.aston.cs3mdd.planaheadmobileapplication.weather.WeatherViewModel;

public class WeatherFragment extends Fragment {
    private FragmentWeatherBinding binding;
    private WeatherViewModel weatherViewModel;
    private TextView cityNameTextView, temperatureTextView, descriptionTextView, dateTimeTextView, windTextView, sunriseTextView, sunsetTextView;
    private EditText searchEditText;
    private Button searchButton;
    private ImageView weatherIconImageView;



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
        windTextView = binding.windTextView;
        sunriseTextView = binding.sunriseTextView;
        sunsetTextView = binding.sunsetTextView;
        weatherIconImageView = binding.weatherIconImageView;
        searchEditText = binding.searchEditText;
        searchButton = binding.searchButton;

        weatherViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);

        String apiKey = getApiKey(requireContext());
        searchButton.setOnClickListener(v -> {
            String cityName = searchEditText.getText().toString();
            if (!cityName.isEmpty()) {
                double[] coordinates = getCoordinatesFromCityName(cityName);
                if (coordinates != null) {
                    double latitude = coordinates[0];
                    double longitude = coordinates[1];

                    weatherViewModel.getCurrentWeather(latitude, longitude, apiKey).observe(getViewLifecycleOwner(), weatherResponse -> {
                        if (weatherResponse != null) {
                            cityNameTextView.setText(weatherResponse.name);
                            temperatureTextView.setText(String.format(Locale.getDefault(), "%.2f °C", weatherResponse.main.temp - 273.15));
                            descriptionTextView.setText(weatherResponse.weather.get(0).description);
                            windTextView.setText(String.format(Locale.getDefault(), "Wind: %.2f m/s, %d°", weatherResponse.wind.speed, weatherResponse.wind.deg));
                            // Display additional data
                            long sunriseTimestamp = weatherResponse.sys.sunrise * 1000L;
                            long sunsetTimestamp = weatherResponse.sys.sunset * 1000L;
                            String sunriseTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date(sunriseTimestamp));
                            String sunsetTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date(sunsetTimestamp));
                            sunriseTextView.setText("Sunrise: " + sunriseTime);
                            sunsetTextView.setText("Sunset: " + sunsetTime);

                            // Display current time in the city
                            double timezoneOffset = weatherResponse.timezone / 3600.0; // Convert seconds to hours
                            long utcTimestamp = System.currentTimeMillis() / 1000L; // UTC timestamp in seconds
                            long localTimestamp = utcTimestamp + (long) (timezoneOffset * 3600); // Convert to local time
                            String localTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                                    .format(new Date(localTimestamp * 1000L));
                            dateTimeTextView.setText(localTime);

                            //Display the weather icon
                            String iconUrl = "https://openweathermap.org/img/w/" + weatherResponse.weather.get(0).icon + ".png";
                            Picasso.get().load(iconUrl).into(weatherIconImageView);
                        } else {
                            // Handle null response or error
                            Toast.makeText(requireContext(), "Error fetching weather data. Please try again.", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "Weather data response is null or an error occurred");
                        }
                    });
                } else {
                    // Handle case where coordinates couldn't be retrieved for the given address
                    Toast.makeText(requireContext(), "Could not retrieve coordinates for the given address.", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Coordinates are null for the given city name");
                }
            } else {
                // Handle case where the address name is empty
                Toast.makeText(requireContext(), "Please enter an address.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "City name is empty");
            }

        });
    }

    private String getApiKey(Context context) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = applicationInfo.metaData;
            return bundle.getString("WEATHER.API_KEY");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

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




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

