package com.example.auth_app.ui.shops;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ShopViewModel extends ViewModel{

    private final MutableLiveData<String> mText;

    public ShopViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is station fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
