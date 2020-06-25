package com.fastfend.haieracbridge;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.fastfend.haieracbridge.haierapi.ACDeviceManager;
import com.fastfend.haieracbridge.haierapi.ACDeviceManagerState;
import com.fastfend.haieracbridge.haierapi.DeviceChecker;
import com.fastfend.haieracbridge.haierapi.UserManager;
import com.fastfend.haieracbridge.haierapi.webapi.WebApiResultStatus;
import com.haier.uhome.usdk.api.uSDKErrorConst;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class BackgroundService extends Service
{
    public final UserManager userManager = UserManager.getInstance();
    public final ACDeviceManager deviceManager = ACDeviceManager.getInstance();

    private final IBinder binder = new LocalBinder();

    public class LocalBinder extends Binder {
        BackgroundService getService() {
            return BackgroundService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void onDestroy() {
        if(deviceManager.getState() == ACDeviceManagerState.RUNNING)
        {
            deviceManager.stop(value -> {

            });
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.println(Log.INFO, "HACB", "Creating notification service...");
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, NOTIFICATION_SERVICE)
                .setContentTitle("HaierACBridge")
                .setContentText("Running")
                .setContentIntent(pendingIntent)
                .build();

        Log.println(Log.INFO, "HACB", "Starting notification service...");
        startForeground(1, notification);
        //TODO: Add icons etc

        try {
            Process process = Runtime.getRuntime().exec("logcat > /sdcard/Download/log.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }


        Log.println(Log.INFO, "HACB", "Initializing service...");
        userManager.init(this.getApplicationContext());
        deviceManager.init(this.getApplicationContext());

        String authToken = userManager.getAuthToken();
        if(!authToken.equals(""))
        {
            Log.println(Log.INFO, "HACB", "Getting devices...");
            userManager.GetUserDevices(authToken, (status, list) -> {
                if(status == WebApiResultStatus.OK)
                {
                    Log.println(Log.INFO, "HACB", "Starting SDK...");
                    deviceManager.start(authToken, list, value -> {
                        if(value == uSDKErrorConst.RET_USDK_OK)
                        {
                            Log.println(Log.INFO, "HACB", "SDK started...");

                            Timer deviceChecker = new Timer();

                            TimerTask checkDevices = new DeviceChecker(userManager, deviceManager, list);
                            deviceChecker.scheduleAtFixedRate(checkDevices, 0, 5000);
                        }
                        else
                        {
                            stopSelf();
                        }
                    });

                }
                else
                {
                    stopSelf();
                }
            });
        }

        return START_STICKY;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    NOTIFICATION_SERVICE,
                    "HaierACBridge",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}
