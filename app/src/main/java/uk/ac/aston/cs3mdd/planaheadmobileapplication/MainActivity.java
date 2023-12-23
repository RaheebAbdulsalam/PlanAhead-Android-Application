package uk.ac.aston.cs3mdd.planaheadmobileapplication;


import static com.google.android.gms.location.Priority.PRIORITY_BALANCED_POWER_ACCURACY;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import uk.ac.aston.cs3mdd.planaheadmobileapplication.databinding.ActivityMainBinding;
import uk.ac.aston.cs3mdd.planaheadmobileapplication.model.LocationViewModel;
import uk.ac.aston.cs3mdd.planaheadmobileapplication.places.SingletonData;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private BottomNavigationView bottomNavigationView;
    public static final String TAG = "MY-TAG";
    private LocationViewModel model;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private FloatingActionButton add_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up the layout
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Bottom Navigation
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_map, R.id.navigation_places, R.id.navigation_weather)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                updateActionBarTitle(destination.getId());
            }
        });

        // FloatingActionButton to add an Event
        add_button = findViewById(R.id.add);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to AddEventFragment when FloatingActionButton is clicked
                Intent intent = new Intent(MainActivity.this, AddEventActivity.class);
                startActivity(intent);
            }
        });

        // Initialising Places API keys
        getPlacesApiKeys();

        // Getting the device location (GPS)
        setUpLocationServices();

    }

    private void updateActionBarTitle(int destinationId) {
        if (destinationId == R.id.navigation_home) {
            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.home);
        } else if (destinationId == R.id.navigation_map) {
            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.map_view);
        } else if (destinationId == R.id.navigation_places) {
            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.nearby_places);
        } else if (destinationId == R.id.navigation_weather) {
            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.weather_forecast);
        }
    }


    private void getPlacesApiKeys() {
        try {
            ApplicationInfo applicationInfo =
                    getApplication().getPackageManager()
                            .getApplicationInfo(getApplication().getPackageName(), PackageManager.GET_META_DATA);
            String apiKey = applicationInfo.metaData.getString("com.google.android.geo.API_KEY");
            SingletonData.getInstance().setApiKey(apiKey);
            String placesKey = applicationInfo.metaData.getString("PLACES.API_KEY");
            SingletonData.getInstance().setPlacesKey(placesKey);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //----------------// Following methods are used to get the device location (GPS) and to handle location permissions //----------------------//
                          //Note: Most Code have been taken from week(3) lab//
    private void setUpLocationServices() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        model = new ViewModelProvider(this).get(LocationViewModel.class);

        // Check and request location permissions
        if (checkLocationPermissions()) {
            getLastLocation();
        }

        setUpLocationCallback();

        // Observe location updates
        model.getLocationUpdates().observe(this, needUpdates -> {
            if (needUpdates) {
                startLocationUpdates();
            } else {
                stopLocationUpdates();
            }
        });
    }

    //  A method to check location permissions
    private boolean checkLocationPermissions() {
        // Check if location permission are granted, if not, request it
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermissions();
            return false;
        } else {
            return true;
        }
    }

    // A method to request location permissions
    private void requestLocationPermissions() {
        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(), result -> {
                            Boolean fineLocationGranted = result.getOrDefault(
                                    android.Manifest.permission.ACCESS_FINE_LOCATION, false);
                            Boolean coarseLocationGranted = result.getOrDefault(
                                    android.Manifest.permission.ACCESS_COARSE_LOCATION, false);
                            if (fineLocationGranted != null && fineLocationGranted) {
                                Log.i(TAG, "Precise location access granted.");
                                getLastLocation();
                            } else if (coarseLocationGranted != null && coarseLocationGranted) {
                                Log.i(TAG, "Only approximate location access granted.");
                                getLastLocation();
                            } else {
                                Log.i(TAG, "No location access granted.");
                            }
                        }
                );

        locationPermissionRequest.launch(new String[]{
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
        });
    }

    // A method to get the last known location
    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getCurrentLocation(PRIORITY_BALANCED_POWER_ACCURACY, null)
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            Log.i(TAG, "We got a location: (" + location.getLatitude() +
                                    ", " + location.getLongitude() + ")");
                            model.setCurrentLocation(location);
                        } else {
                            Log.i(TAG, "We failed to get a last location");
                        }
                    }
                });
    }

    // A method for handling location updates
    private void setUpLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    Log.i(TAG, "Location Update: (" + location.getLatitude() +
                            ", " + location.getLongitude() +
                            ", @ " + location.getTime() + ")");
                    model.setCurrentLocation(location);
                }
            }
        };
    }

    // A method to start location updates
    private void startLocationUpdates() {
        if (locationRequest == null) {
            createLocationRequest();
        }
        if (checkLocationPermissions()) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            fusedLocationClient.requestLocationUpdates(locationRequest,
                    locationCallback,
                    Looper.getMainLooper());
        }
    }

    // A method to stop location updates
    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    // A method to create Location Request
    protected void createLocationRequest() {
        locationRequest = new LocationRequest.Builder(10000)
                .setMinUpdateIntervalMillis(5000)
                .setPriority(PRIORITY_BALANCED_POWER_ACCURACY)
                .build();
    }

    // A method to resume location updates if needed
    @Override
    protected void onResume() {
        super.onResume();
        if (model.getLocationUpdates().getValue()) {
            startLocationUpdates();
        }
    }

    // A method to pause location updates when the app is in the background
    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

}