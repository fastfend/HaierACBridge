package com.fastfend.haieracbridge.haierapi;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.fastfend.haieracbridge.haierapi.webapi.AuthResponse;
import com.fastfend.haieracbridge.haierapi.webapi.CityListResult;
import com.fastfend.haieracbridge.haierapi.webapi.DeviceList;
import com.fastfend.haieracbridge.haierapi.webapi.DeviceListResponse;
import com.fastfend.haieracbridge.haierapi.webapi.RoomDeviceResultList;
import com.fastfend.haieracbridge.haierapi.webapi.WebApiAuthCallback;
import com.fastfend.haieracbridge.haierapi.webapi.WebApiDevicesCallback;
import com.fastfend.haieracbridge.haierapi.webapi.WebApiResultStatus;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import javax.net.ssl.HttpsURLConnection;

public class UserManager {
    static URL loginUserAPI;
    static URL userDevicesAPI;
    static SharedPreferences preferences;

    static {
        try {
            loginUserAPI = new URL("https://uhome.haieriot.net:7263/uhome/acbiz/login");
            userDevicesAPI = new URL("https://uhome.haieriot.net:7263/uhome/acbiz/device/v25/get_userDevlist");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private static UserManager instance;
    public static UserManager getInstance()
    {
        if (instance== null) {
            synchronized(UserManager.class) {
                if (instance == null)
                    instance = new UserManager();
            }
        }
        // Return the instance
        return instance;
    }
    public static UserManager getInstance(UserManager baseinstance)
    {
        if (instance== null) {
            synchronized(UserManager.class) {
                if (instance == null)
                    instance = baseinstance;
            }
        }
        // Return the instance
        return instance;
    }

    public void init(Context applicationContext)
    {
        preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
    }

    public void resetAuthData()
    {
        setAuthUserName("");
        setAuthToken("");
    }

    public boolean isLoggedIn()
    {
        if(getAuthToken().equals(""))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public boolean getAutoStart()
    {
        return preferences.getBoolean("auto_start", false);
    }

    public void setAutoStart(boolean start) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("auto_start", start);
        editor.commit();
    }

    public String getAuthToken()
    {

        //return "TGT28EVNFLCW3D3U2MDXPTZ0Z5Y7L0";
        return preferences.getString("auth_token", "");
    }

    public void setAuthToken(String token)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("auth_token", token);
        editor.commit();
    }

    public String getAuthUserName()
    {
        return preferences.getString("auth_user", "");
    }

    public void setAuthUserName(String userName)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("auth_user", userName);
        editor.commit();
    }

    public void GetLoginTokenFromHaier(String user, String password, WebApiAuthCallback callback) {
        new Thread(() -> {
            try {
                HttpsURLConnection myConnection =
                        (HttpsURLConnection) loginUserAPI.openConnection();

                myConnection.setRequestMethod("POST");

                myConnection.setRequestProperty("appKey", BaseInfo.AppKey);
                myConnection.setRequestProperty("appId", BaseInfo.AppId);
                myConnection.setRequestProperty("appVersion", BaseInfo.AppVersion);
                myConnection.setRequestProperty("clientId", BaseInfo.ClientId);
                myConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                myConnection.setRequestProperty("Content-Encoding", "utf-8");
                myConnection.setRequestProperty("Accept", "application/json");
                myConnection.setDoOutput(true);

                JsonObject mainobject = new JsonObject();
                mainobject.addProperty("loginId", user);
                mainobject.addProperty("password", password);
                mainobject.addProperty("accType", 0);
                mainobject.addProperty("loginType", 2);
                mainobject.addProperty("sequenceId", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "000000");

                String jsonInputString = new Gson().toJson(mainobject);

                try (OutputStream os = myConnection.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                if (myConnection.getResponseCode() == 200) {
                    InputStream responseBody = myConnection.getInputStream();
                    InputStreamReader responseBodyReader =
                            new InputStreamReader(responseBody, "UTF-8");
                    JsonReader jsonReader = new JsonReader(responseBodyReader);

                    Gson gson = new Gson();
                    AuthResponse response = gson.fromJson(jsonReader, AuthResponse.class);
                    jsonReader.close();
                    responseBodyReader.close();
                    responseBody.close();
                    myConnection.disconnect();

                    if (response.retCode.equals("B22109-22109")) {
                        callback.onFinish(WebApiResultStatus.ERROR_BAD_AUTH, "");
                    } else if (response.retCode.equals("00000")) {
                        callback.onFinish(WebApiResultStatus.OK, response.accessToken);
                    } else {
                        callback.onFinish(WebApiResultStatus.ERROR_OTHER, "");
                    }
                }
                else
                {
                    callback.onFinish(WebApiResultStatus.ERROR_OTHER, "");
                }
            }
            catch (IOException ex)
            {
                callback.onFinish(WebApiResultStatus.ERROR_OTHER, "");
            }
        }).start();
    }

    public void GetUserDevices(String token, WebApiDevicesCallback callback) {
        new Thread(() -> {
            LinkedList<ACDevice> data = new LinkedList<>();
            try {
                HttpsURLConnection myConnection =
                        (HttpsURLConnection) userDevicesAPI.openConnection();

                myConnection.setRequestMethod("POST");

                myConnection.setRequestProperty("appKey", BaseInfo.AppKey);
                myConnection.setRequestProperty("appId", BaseInfo.AppId);
                myConnection.setRequestProperty("appVersion", BaseInfo.AppVersion);
                myConnection.setRequestProperty("clientId", BaseInfo.ClientId);
                myConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                myConnection.setRequestProperty("Content-Encoding", "utf-8");
                myConnection.setRequestProperty("Accept", "application/json");
                myConnection.setRequestProperty("accessToken", token);
                myConnection.setDoOutput(true);

                JsonObject sctxobject = new JsonObject();
                sctxobject.addProperty("appId", BaseInfo.AppId);
                sctxobject.addProperty("appVersion", BaseInfo.AppVersion);
                sctxobject.addProperty("clientId", BaseInfo.ClientId);

                JsonObject devicelistobject = new JsonObject();
                devicelistobject.add("sctx", sctxobject);

                JsonObject mainobject = new JsonObject();
                mainobject.add("get_devlist_info", devicelistobject);

                String jsonInputString = new Gson().toJson(mainobject);

                try (OutputStream os = myConnection.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                if (myConnection.getResponseCode() == 200) {
                    InputStream responseBody = myConnection.getInputStream();
                    InputStreamReader responseBodyReader =
                            new InputStreamReader(responseBody, "UTF-8");
                    JsonReader jsonReader = new JsonReader(responseBodyReader);

                    Gson gson = new Gson();
                    DeviceListResponse response = gson.fromJson(jsonReader, DeviceListResponse.class);
                    jsonReader.close();
                    responseBodyReader.close();
                    responseBody.close();
                    myConnection.disconnect();

                    if (response.retCode.equals("B00007-21018")) {
                        callback.onFinish(WebApiResultStatus.ERROR_BAD_TOKEN, data);
                    } else if (response.retCode.equals("00000")) {
                        for (CityListResult cityListResult : response.cityListResult) {
                            for (RoomDeviceResultList roomDeviceResultList : cityListResult.roomDeviceResultList) {
                                for (DeviceList deviceList : roomDeviceResultList.deviceList) {
                                    //TODO: Change recognition pattern because typeId doesn't provide that information
                                    ACDevice dev;
                                    if(deviceList.type.typeIdentifier.length() > 32)
                                    {
                                        dev = new ACDeviceV3(deviceList.id, deviceList.name, deviceList.type.typeIdentifier, deviceList.status.online);
                                    }
                                    else {
                                        dev = new ACDeviceV2(deviceList.id, deviceList.name, deviceList.type.typeIdentifier, deviceList.status.online);
                                    }
                                    data.add(dev);
                                }
                            }
                        }
                        callback.onFinish(WebApiResultStatus.OK, data);
                    } else {
                        callback.onFinish(WebApiResultStatus.ERROR_OTHER, data);
                    }
                }
            }
            catch (IOException ex)
            {
                callback.onFinish(WebApiResultStatus.ERROR_OTHER, data);
            }
        }).start();
    }

    String getSign(String appId, String appKey, String timestamp)
    {
        appKey = appKey.trim();
        appKey = appKey.replaceAll("\"", "");

        StringBuffer sb = new StringBuffer();
        sb.append(appId).append(appKey).append(timestamp);

        MessageDigest md = null;
        byte[] bytes = null;
        try {
            md = MessageDigest.getInstance("MD5");
            bytes = md.digest(sb.toString().getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return BinaryToHexString(bytes);
    }

    String BinaryToHexString(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        String hexStr = "0123456789abcdef";
        for (int i = 0; i < bytes.length; i++) {
            hex.append(String.valueOf(hexStr.charAt((bytes[i] & 0xF0) >> 4)));
            hex.append(String.valueOf(hexStr.charAt(bytes[i] & 0x0F)));
        }
        return hex.toString();
    }


}
