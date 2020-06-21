package com.fastfend.haieracbridge.ui.settings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SettingsViewModel extends ViewModel {

    private boolean isLoggedIn = false;

    private MutableLiveData<String> titleText;

    public SettingsViewModel() {
        titleText = new MutableLiveData<>();
    }

    public void loginClick()
    {

    }

    public LiveData<String> getTitleText() {
        return titleText;
    }
}