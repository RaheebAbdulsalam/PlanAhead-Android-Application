package uk.ac.aston.cs3mdd.bottom_navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import uk.ac.aston.cs3mdd.bottom_navigation.databinding.FragmentHomeBinding;
import uk.ac.aston.cs3mdd.bottom_navigation.databinding.FragmentPlacesBinding;
import uk.ac.aston.cs3mdd.bottom_navigation.model.HomeViewModel;

public class HomeFragment extends Fragment {

    //  An auto-generated class which is generated from the layout file (fragment_home.xml)
    private FragmentHomeBinding binding;

    // ViewModel class to store and manage UI-related data
    private HomeViewModel homeViewModel;


    // The following method is called when the fragment is being created.
    // It inflates the layout defined in fragment_home.xml using data binding and returns the root view.
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    // This method is called after onCreateView and is used for any view setup or data binding.
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

    }

    // This method is called when the view associated with the fragment is being destroyed.
    // It releases any resources associated with the view.
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
