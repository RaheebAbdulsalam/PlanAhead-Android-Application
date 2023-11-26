package uk.ac.aston.cs3mdd.planaheadmobileapplication.events;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EventViewModel extends ViewModel {
    private final MutableLiveData<Boolean> dataChanged = new MutableLiveData<>();

    public LiveData<Boolean> getDataChanged() {
        return dataChanged;
    }

    public void setDataChanged(Boolean value) {
        dataChanged.setValue(value);
    }
}
