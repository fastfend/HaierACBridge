package com.fastfend.haieracbridge.haierapi;

import com.bugfender.sdk.Bugfender;
import com.haier.uhome.usdk.api.interfaces.IuSDKCallback;
import com.haier.uhome.usdk.api.interfaces.IuSDKDeviceListener;
import com.haier.uhome.usdk.api.interfaces.IuSDKGetDeviceNetQualityCallback;
import com.haier.uhome.usdk.api.uSDKDevice;
import com.haier.uhome.usdk.api.uSDKDeviceAlarm;
import com.haier.uhome.usdk.api.uSDKDeviceAttribute;
import com.haier.uhome.usdk.api.uSDKDeviceNetQuality;
import com.haier.uhome.usdk.api.uSDKDeviceStatusConst;
import com.haier.uhome.usdk.api.uSDKErrorConst;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ACDevice {
    private uSDKDevice _device = null;

    private String DeviceID;
    public String getDeviceID() {
        return DeviceID;
    }

    private String UPlusID = "00000000000000008080000000041410";
    public String getUPlusID() {
        return UPlusID;
    }

    private String Name;
    public String getName() {
        return Name;
    }

    private boolean OnlineMode = false;
    public boolean getOnlineMode() {
        return OnlineMode;
    }

    private boolean Connected = false;
    public boolean isConnected() {
        return Connected;
    }

    private boolean PoweredOn = false;
    public boolean getPowerState() {
        return PoweredOn;
    }

    private boolean HealthMode = false;
    public boolean getHealthMode() {
        return HealthMode;
    }

    private boolean SwingUpDown = false;
    public boolean getSwingUpDown() {
        return SwingUpDown;
    }

    private boolean SwingLeftRight = false;
    public boolean getSwingLeftRight() {
        return SwingLeftRight;
    }

    private ACMode Mode = ACMode.COOL;
    public ACMode getMode() {
        return Mode;
    }

    private ACFanSpeed FanSpeed = ACFanSpeed.AUTO;
    public ACFanSpeed getFanSpeed() {
        return FanSpeed;
    }

    private int AroundTemp = 0;
    public int getAroundTemp() {
        return AroundTemp;
    }

    private int SetTemp = 0;
    public int getSetTemp() {
        return SetTemp;
    }

    private int Humidity = 0;
    public int getHumidity() {
        return Humidity;
    }

    private boolean SafeFan = false;
    public boolean getSafeFan() {
        return SafeFan;
    }

    public ACDevice(String id, String name)
    {
        DeviceID = id;
        Name = name;
    }

    public ACDevice(String id, String name, String type)
    {
        DeviceID = id;
        Name = name;
        UPlusID = type;
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
                    Log.println(Log.INFO,"ACDevice", "Unknown alarm: " + a.getName() + " Value: " + a.getValue());
                }
            }

            @Override
            public void onDeviceAttributeChange(uSDKDevice dev, List<uSDKDeviceAttribute> list) {
                for (uSDKDeviceAttribute attribute : list)
                {
                    Bugfender.d(attribute.getAttrName(), attribute.getAttrValue());

                    if(attribute.getAttrName().equals("202001"))
                    {
                        if (attribute.getAttrValue().equals("202001"))
                        {
                            Log.println(Log.INFO,"ACDevice", "Power set to: OFF");
                            PoweredOn = false;
                        }
                    }
                    else if(attribute.getAttrName().equals("202002"))
                    {
                        if (attribute.getAttrValue().equals("202002"))
                        {
                            Log.println(Log.INFO,"ACDevice", "Power set to: ON");
                            PoweredOn = true;
                        }
                    }
                    else if(attribute.getAttrName().equals("202006"))
                    {
                        if (attribute.getAttrValue().equals("202006"))
                        {
                            Log.println(Log.INFO,"ACDevice", "HealthMode set to: ON");
                            HealthMode = true;
                        }
                    }
                    else if(attribute.getAttrName().equals("202005"))
                    {
                        if (attribute.getAttrValue().equals("202005"))
                        {
                            Log.println(Log.INFO,"ACDevice", "HealthMode set to: OFF");
                            HealthMode = false;
                        }
                    }
                    else if(attribute.getAttrName().equals("20200J"))
                    {
                        if (attribute.getAttrValue().equals("302000"))
                        {
                            Log.println(Log.INFO,"ACDevice", "Swing LeftRight set to: OFF");
                            SwingLeftRight = false;
                        }
                        if (attribute.getAttrValue().equals("302007"))
                        {
                            Log.println(Log.INFO,"ACDevice", "Swing LeftRight set to: ON");
                            SwingLeftRight = true;
                        }
                    }
                    else if(attribute.getAttrName().equals("20200I"))
                    {
                        if (attribute.getAttrValue().equals("302000"))
                        {
                            Log.println(Log.INFO,"ACDevice", "Swing UpDown set to: OFF");
                            SwingUpDown = false;
                        }
                        if (attribute.getAttrValue().equals("302008"))
                        {
                            Log.println(Log.INFO,"ACDevice", "Swing UpDown set to: ON");
                            SwingUpDown = true;
                        }
                    }
                    else if(attribute.getAttrName().equals("20200F"))
                    {
                        if (attribute.getAttrValue().equals("302003"))
                        {
                            Log.println(Log.INFO,"ACDevice", "Fan Speed set to: LOW");
                            FanSpeed = ACFanSpeed.LOW;
                        }
                        if (attribute.getAttrValue().equals("302002"))
                        {
                            Log.println(Log.INFO,"ACDevice", "Fan Speed set to: MID");
                            FanSpeed = ACFanSpeed.MID;
                        }
                        if (attribute.getAttrValue().equals("302001"))
                        {
                            Log.println(Log.INFO,"ACDevice", "Fan Speed set to: HIGH");
                            FanSpeed = ACFanSpeed.HIGH;
                        }
                        if (attribute.getAttrValue().equals("302005"))
                        {
                            Log.println(Log.INFO,"ACDevice", "Fan Speed set to: AUTO");
                            FanSpeed = ACFanSpeed.AUTO;
                        }
                    }
                    else if(attribute.getAttrName().equals("20200H"))
                    {
                        if (attribute.getAttrValue().equals("302003"))
                        {
                            Log.println(Log.INFO,"ACDevice", "Fan Safety set to: ON");
                            SafeFan = true;
                        }
                        if (attribute.getAttrValue().equals("302000"))
                        {
                            Log.println(Log.INFO,"ACDevice", "Fan Safety set to: OFF");
                            SafeFan = false;
                        }
                    }
                    else if(attribute.getAttrName().equals("20200D"))
                    {
                        if (attribute.getAttrValue().equals("302006"))
                        {
                            Log.println(Log.INFO,"ACDevice", "Mode set to: FAN");
                            Mode = ACMode.FAN;
                        }
                        if (attribute.getAttrValue().equals("302000"))
                        {
                            Log.println(Log.INFO,"ACDevice", "Mode set to: SMART");
                            Mode = ACMode.SMART;
                        }
                        if (attribute.getAttrValue().equals("302002"))
                        {
                            Log.println(Log.INFO,"ACDevice", "Mode set to: DRY");
                            Mode = ACMode.DRY;
                        }
                        if (attribute.getAttrValue().equals("302004"))
                        {
                            Log.println(Log.INFO,"ACDevice", "Mode set to: HEAT");
                            Mode = ACMode.HEAT;
                        }
                        if (attribute.getAttrValue().equals("302001"))
                        {
                            Log.println(Log.INFO,"ACDevice", "Mode set to: COOL");
                            Mode = ACMode.COOL;
                        }
                    }
                    else if(attribute.getAttrName().equals("20200E"))
                    {
                        Log.println(Log.INFO,"ACDevice", "Set Temperature changed to: " + attribute.getAttrValue());
                        SetTemp = Integer.decode(attribute.getAttrValue());
                    }
                    else if(attribute.getAttrName().equals("602001"))
                    {
                        Log.println(Log.INFO,"ACDevice", "Around Temperature changed to: " + attribute.getAttrValue());
                        AroundTemp = Integer.decode(attribute.getAttrValue());
                    }
                    else if(attribute.getAttrName().equals("602003"))
                    {
                        Log.println(Log.INFO,"ACDevice", "Humidity changed to: " + attribute.getAttrValue());
                        Humidity = Integer.decode(attribute.getAttrValue());
                    }
                    else
                    {
                        Log.println(Log.WARN,"ACDevice", "Unknown attribute: " + attribute.getAttrName() + " set to: " + attribute.getAttrValue());
                    }
                }
            }

            @Override
            public void onDeviceOnlineStatusChange(uSDKDevice dev, uSDKDeviceStatusConst status, int i) {
                if(status == uSDKDeviceStatusConst.STATUS_CONNECTED)
                {
                    Connected = true;
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
                Log.println(Log.INFO, "TEST", uSDKErrorConst.toString());
            }
        });
    }


    public void SetPower(boolean isOn) throws Exception
    {
        if(isOn)
        {
            _device.writeAttribute("202002", "202002", new IuSDKCallback() {
                @Override
                public void onCallback(uSDKErrorConst status) {
                    if(status != uSDKErrorConst.RET_USDK_OK)
                    {
                        try {
                            Log.println(Log.ERROR,"ACDevice", status.name());
                            throw new Exception("Error during sending command");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
        else
        {
            _device.writeAttribute("202001", "202001", new IuSDKCallback() {
                @Override
                public void onCallback(uSDKErrorConst status) {
                    if(status != uSDKErrorConst.RET_USDK_OK)
                    {
                        try {
                            Log.println(Log.ERROR,"ACDevice", status.name());
                            throw new Exception("Error during sending command");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }
    public void SetHealthMode(boolean isOn) throws Exception
    {
        if(isOn)
        {
            _device.writeAttribute("202006", "202006", new IuSDKCallback() {
                @Override
                public void onCallback(uSDKErrorConst status) {
                    if(status != uSDKErrorConst.RET_USDK_OK)
                    {
                        try {
                            Log.println(Log.ERROR,"ACDevice", status.name());
                            throw new Exception("Error during sending command");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
        else
        {
            _device.writeAttribute("202005", "202005", new IuSDKCallback() {
                @Override
                public void onCallback(uSDKErrorConst status) {
                    if(status != uSDKErrorConst.RET_USDK_OK)
                    {
                        try {
                            Log.println(Log.ERROR,"ACDevice", status.name());
                            throw new Exception("Error during sending command");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }
    public void SetTargetTemperature(int temp) throws Exception {
        int settemp = temp;
        if (settemp > 30) {
            settemp = 30;
        }
        if (settemp < 16)
        {
            settemp = 16;
        }
        _device.writeAttribute("20200E", Integer.toString(settemp), new IuSDKCallback() {
            @Override
            public void onCallback(uSDKErrorConst status) {
                if(status != uSDKErrorConst.RET_USDK_OK)
                {
                    try {
                        Log.println(Log.ERROR,"ACDevice", status.name());
                        throw new Exception("Error during sending command");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void SetSwingLeftRight(boolean isOn) throws Exception
    {
        if(isOn)
        {
            _device.writeAttribute("20200J", "302007", new IuSDKCallback() {
                @Override
                public void onCallback(uSDKErrorConst status) {
                    if(status != uSDKErrorConst.RET_USDK_OK)
                    {
                        try {
                            Log.println(Log.ERROR,"ACDevice", status.name());
                            throw new Exception("Error during sending command");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
        else
        {
            _device.writeAttribute("20200J", "302000", new IuSDKCallback() {
                @Override
                public void onCallback(uSDKErrorConst status) {
                    if(status != uSDKErrorConst.RET_USDK_OK)
                    {
                        try {
                            Log.println(Log.ERROR,"ACDevice", status.name());
                            throw new Exception("Error during sending command");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }
    public void SetSwingUpDown(boolean isOn) throws Exception
    {
        if(isOn)
        {
            _device.writeAttribute("20200I", "302008", new IuSDKCallback() {
                @Override
                public void onCallback(uSDKErrorConst status) {
                    if(status != uSDKErrorConst.RET_USDK_OK)
                    {
                        try {
                            Log.println(Log.ERROR,"ACDevice", status.name());
                            throw new Exception("Error during sending command");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
        else
        {
            _device.writeAttribute("20200I", "302000", new IuSDKCallback() {
                @Override
                public void onCallback(uSDKErrorConst status) {
                    if(status != uSDKErrorConst.RET_USDK_OK)
                    {
                        try {
                            Log.println(Log.ERROR,"ACDevice", status.name());
                            throw new Exception("Error during sending command");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }
    public void SetFanSpeed(ACFanSpeed speed) throws Exception
    {
        switch (speed)
        {
            case AUTO:
                _device.writeAttribute("20200F", "302005", new IuSDKCallback() {
                    @Override
                    public void onCallback(uSDKErrorConst status) {
                        if(status != uSDKErrorConst.RET_USDK_OK)
                        {
                            try {
                                Log.println(Log.ERROR,"ACDevice", status.name());
                                throw new Exception("Error during sending command");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                break;
            case HIGH:
                _device.writeAttribute("20200F", "302001", new IuSDKCallback() {
                    @Override
                    public void onCallback(uSDKErrorConst status) {
                        if(status != uSDKErrorConst.RET_USDK_OK)
                        {
                            try {
                                Log.println(Log.ERROR,"ACDevice", status.name());
                                throw new Exception("Error during sending command");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                break;
            case MID:
                _device.writeAttribute("20200F", "302002", new IuSDKCallback() {
                    @Override
                    public void onCallback(uSDKErrorConst status) {
                        if(status != uSDKErrorConst.RET_USDK_OK)
                        {
                            try {
                                Log.println(Log.ERROR,"ACDevice", status.name());
                                throw new Exception("Error during sending command");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                break;
            case LOW:
                _device.writeAttribute("20200F", "302003", new IuSDKCallback() {
                    @Override
                    public void onCallback(uSDKErrorConst status) {
                        if(status != uSDKErrorConst.RET_USDK_OK)
                        {
                            try {
                                Log.println(Log.ERROR,"ACDevice", status.name());
                                throw new Exception("Error during sending command");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                break;
        }
    }
    public void SetMode(ACMode mode) throws Exception
    {
        switch (mode)
        {
            case FAN:
                _device.writeAttribute("20200D", "302006", new IuSDKCallback() {
                    @Override
                    public void onCallback(uSDKErrorConst status) {
                        if(status != uSDKErrorConst.RET_USDK_OK)
                        {
                            try {
                                Log.println(Log.ERROR,"ACDevice", status.name());
                                throw new Exception("Error during sending command");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                break;
            case SMART:
                _device.writeAttribute("20200D", "302000", new IuSDKCallback() {
                    @Override
                    public void onCallback(uSDKErrorConst status) {
                        if(status != uSDKErrorConst.RET_USDK_OK)
                        {
                            try {
                                Log.println(Log.ERROR,"ACDevice", status.name());
                                throw new Exception("Error during sending command");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                break;
            case DRY:
                _device.writeAttribute("20200D", "302002", new IuSDKCallback() {
                    @Override
                    public void onCallback(uSDKErrorConst status) {
                        if(status != uSDKErrorConst.RET_USDK_OK)
                        {
                            try {
                                Log.println(Log.ERROR,"ACDevice", status.name());
                                throw new Exception("Error during sending command");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                break;
            case HEAT:
                _device.writeAttribute("20200D", "302004", new IuSDKCallback() {
                    @Override
                    public void onCallback(uSDKErrorConst status) {
                        if(status != uSDKErrorConst.RET_USDK_OK)
                        {
                            try {
                                Log.println(Log.ERROR,"ACDevice", status.name());
                                throw new Exception("Error during sending command");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                break;
            case COOL:
                _device.writeAttribute("20200D", "302001", new IuSDKCallback() {
                    @Override
                    public void onCallback(uSDKErrorConst status) {
                        if(status != uSDKErrorConst.RET_USDK_OK)
                        {
                            try {
                                Log.println(Log.ERROR,"ACDevice", status.name());
                                throw new Exception("Error during sending command");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                break;
        }
    }

}
