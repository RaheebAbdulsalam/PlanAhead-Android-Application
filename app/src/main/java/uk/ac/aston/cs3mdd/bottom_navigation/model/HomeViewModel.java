package uk.ac.aston.cs3mdd.bottom_navigation.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Welcome to PlanAhead App!");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
