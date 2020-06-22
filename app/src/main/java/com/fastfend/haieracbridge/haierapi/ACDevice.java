package com.fastfend.haieracbridge.haierapi;

import com.haier.uhome.usdk.api.interfaces.IuSDKCallback;
import com.haier.uhome.usdk.api.interfaces.IuSDKDeviceListener;
import com.haier.uhome.usdk.api.uSDKDevice;
import com.haier.uhome.usdk.api.uSDKDeviceAlarm;
import com.haier.uhome.usdk.api.uSDKDeviceAttribute;
import com.haier.uhome.usdk.api.uSDKDeviceStatusConst;
import com.haier.uhome.usdk.api.uSDKErrorConst;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public abstract class ACDevice {
    protected uSDKDevice _device = null;

    private String DeviceID;
    public String getDeviceID() {
        return DeviceID;
    }

    private String UPlusID;
    public String getUPlusID() {
        return UPlusID;
    }

    private String Name;
    public String getName() {
        return Name;
    }

    protected boolean OnlineMode;
    public boolean getOnlineMode() {
        return OnlineMode;
    }

    protected boolean Connected = false;
    public boolean isConnected() {
        return Connected;
    }

    protected boolean PoweredOn = false;
    public boolean getPowerState() {
        return PoweredOn;
    }

    protected boolean HealthMode = false;
    public boolean getHealthMode() {
        return HealthMode;
    }

    protected boolean HorizontalSway = false;
    public boolean getHorizontalSway() {
        return HorizontalSway;
    }

    protected boolean VerticalSway = false;
    public boolean getVerticalSway() {
        return VerticalSway;
    }

    protected ACMode Mode = ACMode.COOL;
    public ACMode getMode() {
        return Mode;
    }

    protected ACFanSpeed FanSpeed = ACFanSpeed.AUTO;
    public ACFanSpeed getFanSpeed() {
        return FanSpeed;
    }

    protected double IndoorTemp = 0;
    public double getIndoorTemp() {
        return IndoorTemp;
    }

    protected int SetTemp = 0;
    public int getSetTemp() {
        return SetTemp;
    }

    protected int Humidity = 0;
    public int getHumidity() {
        return Humidity;
    }

    protected boolean EcoSensor = false;
    public boolean getEcoSensor() {
        return EcoSensor;
    }

    public ACDevice(String id, String name, String type, boolean onlineMode)
    {
        DeviceID = id;
        Name = name;
        UPlusID = type;
        OnlineMode = onlineMode;
    }

    public void LinkSDKDevice(uSDKDevice device)
    {
        _device = device;
        _device.setDeviceListener(new IuSDKDeviceListener() {
            @Override
            public void onDeviceAlarm(uSDKDevice dev, List<uSDKDeviceAlarm> list) {
                for (uSDKDeviceAlarm a : list)
                {
                    Log.println(Log.INFO,"ACDevice[" + getDeviceID() + "]", "Unknown alarm: " + a.getName() + " Value: " + a.getValue());
                }
            }

            @Override
            public void onDeviceAttributeChange(uSDKDevice dev, List<uSDKDeviceAttribute> list) {
                for (uSDKDeviceAttribute attribute : list)
                {
                    onDataUpdate(attribute.getAttrName(), attribute.getAttrValue());
                }
            }

            @Override
            public void onDeviceOnlineStatusChange(uSDKDevice dev, uSDKDeviceStatusConst status, int i) {
                if(status == uSDKDeviceStatusConst.STATUS_CONNECTED)
                {
                    Connected = true;
                    Log.println(Log.INFO,"ACDevice[" + getDeviceID() + "]", "Device is online");
                }
                else
                {
                    Connected = false;
                }
            }

            @Override
            public void onDeviceBaseInfoChange(uSDKDevice dev) {

            }

            @Override
            public void onSubDeviceListChange(uSDKDevice dev, ArrayList<uSDKDevice> list) {

            }
        });

        _device.connectNeedProperties(new IuSDKCallback() {
            @Override
            public void onCallback(uSDKErrorConst uSDKErrorConst) {
                //TODO: Add logs
            }
        });
    }


    public abstract void onDataUpdate(String attributeName, String attributeValue);
    public abstract void SetPower(boolean isOn);
    public abstract void SetHealthMode(boolean isOn);
    public abstract void SetTargetTemperature(int temp);
    public abstract void SetVerticalSway(boolean isOn);
    public abstract void SetHorizontalSway(boolean isOn);
    public abstract void SetFanSpeed(ACFanSpeed speed);
    public abstract void SetMode(ACMode mode);

}
