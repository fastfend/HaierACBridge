package com.fastfend.haieracbridge;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.fastfend.haieracbridge.haierapi.ACDeviceManager;
import com.fastfend.haieracbridge.haierapi.ACDeviceManagerState;
import com.fastfend.haieracbridge.haierapi.DeviceChecker;
import com.fastfend.haieracbridge.haierapi.UserManager;
import com.fastfend.haieracbridge.haierapi.webapi.WebApiResultStatus;

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

        userManager.init(this.getApplicationContext());
        deviceManager.init(this.getApplicationContext());

        String authToken = userManager.getAuthToken();
        if(!authToken.equals(""))
        {
            userManager.GetUserDevices(authToken, (status, list) -> {
                if(status == WebApiResultStatus.OK)
                {
                    deviceManager.start(authToken, list, value -> {
                        Toast.makeText(this, "Started", Toast.LENGTH_LONG).show();

                        Timer deviceChecker = new Timer();

                        TimerTask checkDevices = new DeviceChecker(userManager, deviceManager, list);
                        deviceChecker.scheduleAtFixedRate(checkDevices, 0, 5000);

                    });
                    createNotificationChannel();
                    Intent notificationIntent = new Intent(this, MainActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this,
                            0, notificationIntent, 0);
                    Notification notification = new NotificationCompat.Builder(this, NOTIFICATION_SERVICE)
                            .setContentTitle("HaierACBridge")
                            .setContentText("Running")
                            .setContentIntent(pendingIntent)
                            .build();
                    startForeground(1, notification);
                    //TODO: Add icons etc
                }
                else
                {
                    stopSelf();
                }
            });
        }

        return super.onStartCommand(intent, flags, startId);
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
