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
import uk.ac.aston.cs3mdd.bottom_navigation.databinding.FragmentNewsBinding;
import uk.ac.aston.cs3mdd.bottom_navigation.databinding.FragmentPlacesBinding;
import uk.ac.aston.cs3mdd.bottom_navigation.model.HomeViewModel;

public class NewsFragment extends Fragment {
    private FragmentNewsBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentNewsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}

