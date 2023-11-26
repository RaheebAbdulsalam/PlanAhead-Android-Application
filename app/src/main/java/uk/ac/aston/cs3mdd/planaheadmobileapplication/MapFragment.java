package uk.ac.aston.cs3mdd.planaheadmobileapplication;

import static android.content.ContentValues.TAG;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.Manifest;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import uk.ac.aston.cs3mdd.planaheadmobileapplication.databinding.FragmentMapBinding;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private FragmentMapBinding binding;
    private MapView mapView;
    private EditText mapSearch;
    private GoogleMap mGoogleMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMapBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        mapView = binding.mapView;
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        mapSearch = binding.inputSearch;

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Enable the My Location layer if the permission is granted
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mGoogleMap = googleMap;
            mGoogleMap.setMyLocationEnabled(true);
            UiSettings uiSettings = mGoogleMap.getUiSettings();
            uiSettings.setZoomControlsEnabled(true);
            // Initialising search functionality
            initSearch();
        }
    }


    //Initialising search functionality for the map
    private void initSearch() {
        mapSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event.getAction() == KeyEvent.ACTION_DOWN
                        && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    // Handling search action
                    searchAndMoveToLocation(); // Call the method to perform geocoding and move the camera
                    return true;
                }
                return false;
            }
        });
    }

    //Performs geocoding based on the user-entered search query, moves the camera to the searched location, and adds a marker at that location on the Google Map.
    private void searchAndMoveToLocation() {
        Log.d(TAG, "searchAndMoveToLocation: geolocating");
        String searchString = mapSearch.getText().toString();
        Geocoder geocoder = new Geocoder(requireContext());
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            Log.e(TAG, "searchAndMoveToLocation: IOException: " + e.getMessage());
            e.printStackTrace();
        }
        if (list.size() > 0) {
            Address address = list.get(0);

            // Log the address details for debugging
            Log.d(TAG, "searchAndMoveToLocation: found location: " + address.toString());

            // Move the camera to the searched location
            float zoomLevel = 16.0f; // Adjust this value as needed
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(address.getLatitude(), address.getLongitude()), zoomLevel));

            // Add a marker at the searched location
            mGoogleMap.clear(); // Clear existing markers
            mGoogleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(address.getLatitude(), address.getLongitude()))
                    .title("Searched Location"));
        } else {
            // Log a message if no location is found
            Log.d(TAG, "searchAndMoveToLocation: location not found");
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}