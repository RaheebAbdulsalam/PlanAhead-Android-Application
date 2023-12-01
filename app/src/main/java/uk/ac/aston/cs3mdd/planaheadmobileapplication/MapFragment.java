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
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.Manifest;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import uk.ac.aston.cs3mdd.planaheadmobileapplication.databinding.FragmentMapBinding;
import uk.ac.aston.cs3mdd.planaheadmobileapplication.places.MyPlace;
import uk.ac.aston.cs3mdd.planaheadmobileapplication.places.PlacesViewModel;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private FragmentMapBinding binding;
    private MapView mapView;
    private EditText mapSearch;
    private GoogleMap mGoogleMap;

    private ImageView searchIcon;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMapBinding.inflate(inflater, container, false);
        mapView = binding.mapView;
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        mapSearch = binding.inputSearch;
        searchIcon=binding.icMagnify;
        return binding.getRoot();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        // Enable the My Location layer if the permission is granted
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mGoogleMap = googleMap;
            mGoogleMap.setMyLocationEnabled(true);
            UiSettings uiSettings = mGoogleMap.getUiSettings();
            uiSettings.setZoomControlsEnabled(true);


            // Set the initial camera position to focus on the UK
            LatLng ukLatLng = new LatLng(52.4862, -1.8904); // Coordinates for London, UK
            float zoomLevel = 8.0f; // Adjust the zoom level as needed
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ukLatLng, zoomLevel));


            initSearch();
            // Add markers for all nearby places
            addMarkersForPlaces();
        }
    }

    // Method to add markers for all nearby places from the ViewModel
    private void addMarkersForPlaces() {
        PlacesViewModel viewModel = new ViewModelProvider(requireActivity()).get(PlacesViewModel.class);

        viewModel.getAllPlaces().observe(getViewLifecycleOwner(), placeList -> {
            // Check if the list is not empty
            if (placeList != null && !placeList.isEmpty()) {
                for (MyPlace place : placeList) {
                    LatLng placeLatLng = new LatLng(place.getLatitude(), place.getLongitude());
                    //Change the marker plot color
                    BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
                    mGoogleMap.addMarker(new MarkerOptions().position(placeLatLng).title(place.getName()) .icon(bitmapDescriptor));
                }
            }
        });
    }

    //Initialising search functionality for the map
    private void initSearch() {

        // Set OnClickListener for the search icon
        searchIcon.setOnClickListener(v -> searchAndMoveToLocation());

        mapSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE ||
                    (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                searchAndMoveToLocation();
                return true;
            }
            return false;
        });
    }

    //Performs geocoding based on the user-entered search query, moves the camera to the searched location, and adds a marker at that location on the Google Map.
    private void searchAndMoveToLocation() {
        Log.d(TAG, "searchAndMoveToLocation: geolocation");
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
            Log.d(TAG, "searchAndMoveToLocation: found location: " + address.toString());
            float zoomLevel = 16.0f;
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(address.getLatitude(), address.getLongitude()), zoomLevel));
            mGoogleMap.clear();
            mGoogleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(address.getLatitude(), address.getLongitude()))
                    .title("Searched Location"));
        } else {
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