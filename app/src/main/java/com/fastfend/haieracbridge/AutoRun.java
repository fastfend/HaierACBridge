package com.fastfend.haieracbridge;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;


public class AutoRun extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent arg1) {

        boolean autorun = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()).getBoolean("auto_start", false);
        if(autorun) {
            Intent intent = new Intent(context, BackgroundService.class);
            context.startService(intent);
        }
    }
}
