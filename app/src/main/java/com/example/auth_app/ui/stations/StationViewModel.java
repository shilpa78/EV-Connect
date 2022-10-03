package com.example.auth_app.ui.stations;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StationViewModel extends ViewModel{

    private final MutableLiveData<String> mText;

    public StationViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is station fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
