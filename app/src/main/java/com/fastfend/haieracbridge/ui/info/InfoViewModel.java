package com.fastfend.haieracbridge.ui.info;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fastfend.haieracbridge.R;
import com.fastfend.haieracbridge.haierapi.ACDeviceManager;
import com.fastfend.haieracbridge.haierapi.ACDeviceManagerState;

public class InfoViewModel extends AndroidViewModel {

    private MutableLiveData<String> statusText;
    private MutableLiveData<String> tokenText;
    private MutableLiveData<String> ipText;

    private ACDeviceManager deviceManager = ACDeviceManager.getInstance();

    public Context context;
    public InfoViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        statusText = new MutableLiveData<>();
        tokenText = new MutableLiveData<>();
        ipText = new MutableLiveData<>();

        SetStuff(deviceManager.getState());
        deviceManager.setListener(changedto -> {
            SetStuff(changedto);
        });
    }
    private void SetStuff(ACDeviceManagerState changedto)
    {
        //TODO: Fix current state names
        switch (changedto)
        {
            case RUNNING:
                statusText.setValue(context.getResources().getString(R.string.status_running));
                tokenText.setValue(deviceManager.getApiToken());
                ipText.setValue(deviceManager.getLocalIpAddress());
                break;
            case LOGGED_IN:
                statusText.setValue(context.getResources().getString(R.string.status_logged_in));
                tokenText.setValue(context.getResources().getString(R.string.status_unknown));
                ipText.setValue(context.getResources().getString(R.string.status_unknown));
                break;
            case DISABLED:
                statusText.setValue(context.getResources().getString(R.string.status_disabled));
                tokenText.setValue(context.getResources().getString(R.string.status_unknown));
                ipText.setValue(context.getResources().getString(R.string.status_unknown));
                break;
            case NOT_LOGGED_IN:
                statusText.setValue(context.getResources().getString(R.string.status_not_logged_in));
                tokenText.setValue(context.getResources().getString(R.string.status_unknown));
                ipText.setValue(context.getResources().getString(R.string.status_unknown));
                break;
        }
    }

    public LiveData<String> getStatusText() { return statusText; }
    public LiveData<String> getTokenText() {
        return tokenText;
    }
    public LiveData<String> getIPText() {
        return ipText;
    }
}