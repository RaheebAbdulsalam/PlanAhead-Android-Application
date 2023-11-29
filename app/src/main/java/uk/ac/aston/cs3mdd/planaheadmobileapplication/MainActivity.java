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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import uk.ac.aston.cs3mdd.planaheadmobileapplication.databinding.ActivityMainBinding;
import uk.ac.aston.cs3mdd.planaheadmobileapplication.events.AddEventActivity;
import uk.ac.aston.cs3mdd.planaheadmobileapplication.model.LocationViewModel;
import uk.ac.aston.cs3mdd.planaheadmobileapplication.places.SingletonData;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private BottomNavigationView bottomNavigationView;
    public static final String TAG = "MYTAG";
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

        // Initialize the Places API keys
        try {
            ApplicationInfo applicationInfo =
                    getApplication().getPackageManager()
                            .getApplicationInfo(getApplication().getPackageName(), PackageManager.GET_META_DATA);
            String apiKey = applicationInfo.metaData.getString("com.google.android.geo.API_KEY");
            Log.i(TAG, "MAPS KEY is " + apiKey);
            SingletonData.getInstance().setApiKey(apiKey);
            String placesKey = applicationInfo.metaData.getString("PLACES.API_KEY");
            SingletonData.getInstance().setPlacesKey(placesKey);

        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
//----------------------------------------------------------------------------------------------------------------//
// Getting the device location (GPS)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        model = new ViewModelProvider(this).get(LocationViewModel.class);
        //Adding location permission
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Request location permissions using ActivityResultLauncher
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

            // Launch the location permission request to check whether the app already has the permissions, and whether the app needs to show a permission dialog
            locationPermissionRequest.launch(new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
            });
        } else {
            // Location permissions already granted, get the last known location
            Log.i(TAG, "Location permissions already granted.");
            getLastLocation();
        }

        // Set up location callback to handle location updates
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    Log.i(TAG, "Location Update: NO LOCATION");
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    Log.i(TAG, "Location Update: (" + location.getLatitude() +
                            ", " + location.getLongitude() +
                            ", @ " + location.getTime() + ")");
                    model.setCurrentLocation(location);
                }
            }
        };

        //Observing the model.getLocationUpdates and responding to changes by calling the start and stop methods
        model.getLocationUpdates().observe(this, needUpdates -> {
            if (needUpdates) {
                this.startLocationUpdates();
            } else {
                this.stopLocationUpdates();
            }
        });

    }


    // Method to et the last location, used in multiple places where the last location is needed
    public void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getCurrentLocation(PRIORITY_BALANCED_POWER_ACCURACY, null)
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {

                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            Log.i(TAG, "We got a location: (" + location.getLatitude() +
                                    ", " + location.getLongitude() + ")");
                            model.setCurrentLocation(location);
                        } else {
                            Log.i(TAG, "We failed to get a last location");
                        }
                    }
                });
    }

    // start location updates
    private void startLocationUpdates() {
        if (locationRequest == null) {
            createLocationRequest();
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
    }

    //  A method to stop location updates
    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    // A method to initialise the locationRequest
    protected void createLocationRequest() {
        locationRequest = new LocationRequest.Builder(10000)
                .setMinUpdateIntervalMillis(5000)
                .setPriority(PRIORITY_BALANCED_POWER_ACCURACY)
                .build();
    }

    // A method to check if the location updates were previously requested
    @Override
    protected void onResume() {
        super.onResume();
        if (model.getLocationUpdates().getValue()) {
            startLocationUpdates();
        }
    }

    // A method to turn the location updates off while the app is not in the foreground
    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

}