package com.fastfend.haieracbridge.haierapi;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.fastfend.haieracbridge.util.RandomStringGenerator;
import com.fastfend.haieracbridge.webserver.ServerFactory;
import com.haier.uhome.usdk.api.interfaces.IuSDKCallback;
import com.haier.uhome.usdk.api.uSDKDeviceInfo;
import com.haier.uhome.usdk.api.uSDKDeviceManager;
import com.haier.uhome.usdk.api.uSDKDeviceTypeConst;
import com.haier.uhome.usdk.api.uSDKErrorConst;
import com.haier.uhome.usdk.api.uSDKLogLevelConst;
import com.haier.uhome.usdk.api.uSDKManager;

import org.restlet.Server;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

public class ACDeviceManager
{

    public interface ACDeviceManagerListner
    {
        void onChangedState(ACDeviceManagerState changedto);
    }
    private ACDeviceManagerListner listener;
    public void setListener(ACDeviceManagerListner listener) {
        this.listener = listener;
    }

    private static uSDKManager mSDKManager
            = uSDKManager.getSingleInstance();
    private static uSDKDeviceManager mDeviceManager
            = uSDKDeviceManager.getSingleInstance();

    private static ACDeviceManager instance;
    public static ACDeviceManager getInstance(ACDeviceManager baseinstance)
    {
        if (instance== null) {
            synchronized(ACDeviceManager.class) {
                if (instance == null)
                    instance = baseinstance;
            }
        }
        // Return the instance
        return instance;
    }
    public static ACDeviceManager getInstance()
    {
        if (instance== null) {
            synchronized(ACDeviceManager.class) {
                if (instance == null)
                    instance = new ACDeviceManager();
            }
        }
        // Return the instance
        return instance;
    }

    public String getNewApiToken()
    {
        return RandomStringGenerator.getRandomString(15);
    }

    public String getApiToken()
    {
        String token = preferences.getString("api_token", "");
        if(token.equals(""))
        {
            token = getNewApiToken();
            setApiToken(token);
        }
        return token;
    }

    public void setApiToken(String token)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("api_token", token);
        editor.commit();
    }

    static SharedPreferences preferences;

    public void init(Context context)
    {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        mSDKManager.initLog(uSDKLogLevelConst.USDK_LOG_DEBUG, false, new IuSDKCallback() {
            @Override
            public void onCallback(uSDKErrorConst result) {

            }
        });

        mSDKManager.init(context);

        if(getApiToken().equals(""))
        {
            State = ACDeviceManagerState.NOT_LOGGED_IN;
        }
        else
        {
            State = ACDeviceManagerState.LOGGED_IN;
        }

        if (listener != null)
        {
            listener.onChangedState(State);
        }
        Log.println(Log.INFO, "HACB_Manager", "Init complete");
    }

    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); ((Enumeration) en).hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }


    private static List<ACDevice> Devices
            = new LinkedList<>();
    private static Server ApiServer;

    public void stop(ACDeviceManagerCallback callback)
    {
        try {
            ApiServer.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mSDKManager.stopSDK(result ->
        {
            if(getApiToken().equals(""))
            {
                State = ACDeviceManagerState.NOT_LOGGED_IN;
            }
            else
            {
                State = ACDeviceManagerState.DISABLED;
            }

            if (listener != null)
            {
                listener.onChangedState(State);
            }
            callback.onFinish(result);
        });
    }

    public void start(String authtoken, List<ACDevice> list, ACDeviceManagerCallback callback)
    {
        String apitoken = getApiToken();
        if(apitoken.equals(""))
        {
            callback.onFinish(uSDKErrorConst.ERR_USDK_CLOUD_COMMON_ERROR);
            //TODO: Change to new callback type
        }
        else
        {
            start(getApiToken(), authtoken, list, callback);
        }

    }

    public void start(String apitoken, String authtoken, List<ACDevice> list, ACDeviceManagerCallback callback) {
        Log.println(Log.INFO, "HACB_Manager", "SDK starting");
        mSDKManager.startSDK(uSDKStartResult -> {
            if (uSDKStartResult == uSDKErrorConst.RET_USDK_OK) {
                Log.println(Log.INFO, "HACB_Manager", "SDK started");
                mSDKManager.enableFeatures(uSDKManager.SDK_FEATURE_NONE);

                List<uSDKDeviceInfo> remotedCtrledDeviceCollection = new ArrayList<uSDKDeviceInfo>();
                for (ACDevice dev : list) {
                    uSDKDeviceInfo sdkdev = new uSDKDeviceInfo(
                            dev.getDeviceID(),
                            dev.getUPlusID(),
                            dev.getOnlineMode());
                    remotedCtrledDeviceCollection.add(sdkdev);
                }

                ArrayList types = new ArrayList<uSDKDeviceTypeConst>();
                types.add(uSDKDeviceTypeConst.COMMERCIAL_AIRCONDITION);
                types.add(uSDKDeviceTypeConst.SPLIT_AIRCONDITION);
                types.add(uSDKDeviceTypeConst.PACKAGE_AIRCONDITION);
                mDeviceManager.setInterestedDeviceTypes(types);


                Log.println(Log.INFO, "HACB_Manager", "Connecting to gateway");
                mDeviceManager.connectToGateway(authtoken, "gw-gea-euro.haieriot.net", 56815, remotedCtrledDeviceCollection, new IuSDKCallback() {
                    @Override
                    public void onCallback(uSDKErrorConst result) {
                        State = ACDeviceManagerState.LOGGED_IN;

                        for (ACDevice acdev : list) {
                            acdev.LinkSDKDevice(mDeviceManager.getDevice(acdev.getDeviceID()));
                        }

                        if (listener != null)
                        {
                            listener.onChangedState(State);
                        }

                        ApiServer = ServerFactory.createServer(list, apitoken);
                        try {
                            ApiServer.start();
                            State = ACDeviceManagerState.RUNNING;
                            if (listener != null)
                            {
                                listener.onChangedState(State);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        callback.onFinish(result);
                    }
                });
            }
        });
    }

    private ACDeviceManagerState State = ACDeviceManagerState.DISABLED;
    public ACDeviceManagerState getState() {
        return State;
    }
}