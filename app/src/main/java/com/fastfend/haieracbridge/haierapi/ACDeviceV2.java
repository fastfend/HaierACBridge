package com.fastfend.haieracbridge.haierapi;

import android.util.Log;

import com.haier.uhome.usdk.api.interfaces.IuSDKCallback;
import com.haier.uhome.usdk.api.uSDKErrorConst;

// Name - AttributeName - AttributeValue
// Power Off            202001 - 202001
// Power On             202002 - 202002
// ???                  202003 - 202003 // something related to turning off
// ???                  202004 - 202004 // something related to turning on
// HealthModeOff        202005 - 202005
// HealthModeOn         202006 - 202006
// ???                  202007 - 202007 // something related to turning off
// ???                  202008 - 202008 // something related to turning on
// ???                  202009 - 202009 // something related to turning off humidifier
// ???                  20200a - 20200a // something related to turning on humidifier
// ???                  20200j - 20200j // something related to turning on lock
// ???                  20200k - 20200k // something related to turning on unlock
// ???                  20200l - 20200l // something related to turning off from
// ???                  20200m - 20200m // something related to turning on from
// ???                  20200n - 20200n // something related to cloud services
// ???                  20200o - 20200o // something related to sleep line
// Mode (SMART)         20200D - 302000
// Mode (COOL)          20200D - 302001
// Mode (DRY)           20200D - 302002
// Mode (HEAT)          20200D - 302004
// Mode (FAN)           20200D - 302006
// Set Temperature      20200E - <16,30>
// FanSpeed (HIGH)      20200F - 302001
// FanSpeed (MID)       20200F - 302002
// FanSpeed (LOW)       20200F - 302003
// FanSpeed (AUTO)      20200F - 302005
// ???                  20200G - <?,?> // something related to setting desired humidity
// EcoSensor (Off)      20200H - 302000
// EcoSensor (On)       20200H - 302003
// HorizontalSway (Off) 20200I - 302000
// HorizontalSway (On)  20200I - 302008
// VerticalSway (Off)   20200J - 302000
// VerticalSway (On)    20200J - 302007
// ???                  20200M - 302000 or 302001 //something related to power eco
// ???                  20200N - 302000 or 302001 //something related to Fahrenheit and Celsius settings
// ???                  20200O - 302000 or 302001 //something related to power
// ???                  20200P - 302000 or 302001 //something related to quiet mode
// ???                  20200Q - 302000 or 302001 //something related to health blow
// ???                  20200R - 302000 or 302001 //something related to health
// ???                  20200S - 302000 or 302001 //something related to display
// ???                  20200T - 302000 or 302001 //something related to 0.5 temperature
// CurrentTemperature   602001
// CurrentHumidity      602002
// OutsideTemperature   602003
// AirQuality           602004
// ???                  602007 //something related to low power
// ???                  602009 //something related to high power
// PMValue              602008
// PlusHalfDegree (no)  60200a - 302000
// PlusHalfDegree (yes) 60200a - 302001

public class ACDeviceV2 extends ACDevice {
    public ACDeviceV2(String id, String name, String type, boolean onlineMode) {
        super(id, name, type, onlineMode);
    }

    @Override
    public void onDataUpdate(String attributeName, String attributeValue) {
        switch (attributeName)
        {
            case "202001":
                if(attributeValue.equals("202001"))
                {
                    PoweredOn = false;
                }
                Log.println(Log.INFO,"ACDevice[" + getDeviceID() + "]", "Power set to: OFF");
                break;
            case "202002":
                if(attributeValue.equals("202002"))
                {
                    PoweredOn = true;
                }
                Log.println(Log.INFO,"ACDevice[" + getDeviceID() + "]", "Power set to: ON");
                break;
            case "202005":
                if(attributeValue.equals("202005"))
                {
                    HealthMode = false;
                }
                Log.println(Log.INFO,"ACDevice[" + getDeviceID() + "]", "HealthMode set to: OFF");
                break;
            case "202006":
                if(attributeValue.equals("202006"))
                {
                    HealthMode = true;
                }
                Log.println(Log.INFO,"ACDevice[" + getDeviceID() + "]", "HealthMode set to: ON");
                break;
            case "20200D":
                switch (attributeValue)
                {
                    case "302000":
                        Mode = ACMode.SMART;
                        Log.println(Log.INFO,"ACDevice[" + getDeviceID() + "]", "Mode set to: SMART");
                        break;
                    case "302001":
                        Mode = ACMode.COOL;
                        Log.println(Log.INFO,"ACDevice[" + getDeviceID() + "]", "Mode set to: COOL");
                        break;
                    case "302002":
                        Mode = ACMode.DRY;
                        Log.println(Log.INFO,"ACDevice[" + getDeviceID() + "]", "Mode set to: DRY");
                        break;
                    case "302004":
                        Mode = ACMode.HEAT;
                        Log.println(Log.INFO,"ACDevice[" + getDeviceID() + "]", "Mode set to: HEAT");
                        break;
                    case "302006":
                        Mode = ACMode.FAN;
                        Log.println(Log.INFO,"ACDevice[" + getDeviceID() + "]", "Mode set to: FAN");
                        break;
                }
                break;
            case "20200E":
                SetTemp = Integer.decode(attributeValue);
                Log.println(Log.INFO,"ACDevice[" + getDeviceID() + "]", "Set Temperature changed to: " + attributeValue);
                break;
            case "20200F":
                switch (attributeValue)
                {
                    case "302001":
                        FanSpeed = ACFanSpeed.HIGH;
                        Log.println(Log.INFO,"ACDevice[" + getDeviceID() + "]", "FanSpeed set to: HIGH");
                        break;
                    case "302002":
                        FanSpeed = ACFanSpeed.MID;
                        Log.println(Log.INFO,"ACDevice[" + getDeviceID() + "]", "FanSpeed set to: MID");
                        break;
                    case "302003":
                        FanSpeed = ACFanSpeed.LOW;
                        Log.println(Log.INFO,"ACDevice[" + getDeviceID() + "]", "FanSpeed set to: LOW");
                        break;
                    case "302005":
                        FanSpeed = ACFanSpeed.AUTO;
                        Log.println(Log.INFO,"ACDevice[" + getDeviceID() + "]", "FanSpeed set to: AUTO");
                        break;
                }
                break;
            case "20200H":
                EcoSensor = attributeValue.equals("302003");
                Log.println(Log.INFO,"ACDevice[" + getDeviceID() + "]", "EcoSensor set to: " + (EcoSensor ? "ON" : "OFF"));
                break;
            case "20200I":
                HorizontalSway = attributeValue.equals("302008");
                Log.println(Log.INFO,"ACDevice[" + getDeviceID() + "]", "HorizontalSway set to: " + (HorizontalSway ? "ON" : "OFF"));
                break;
            case "20200J":
                VerticalSway = attributeValue.equals("302007");
                Log.println(Log.INFO,"ACDevice[" + getDeviceID() + "]", "VerticalSway set to: " + (VerticalSway ? "ON" : "OFF"));
                break;
            case "602001":
                //IndoorTemp = Double.parseDouble(attributeValue);
                Log.println(Log.INFO,"ACDevice[" + getDeviceID() + "]", "BaseIndoorTemp changed to: " + attributeValue);
                break;
            case "602002":
                Humidity = Integer.decode(attributeValue);
                Log.println(Log.INFO,"ACDevice[" + getDeviceID() + "]", "Humidity changed to: " + attributeValue);
                break;
            case "60200a":
                double temp = Double.parseDouble(_device.getAttributeMap().get("602001").getAttrValue());
                if(attributeValue.equals("302001"))
                {
                    IndoorTemp = temp + 0.5;
                }
                else
                {
                    IndoorTemp = temp;
                }
                Log.println(Log.INFO,"ACDevice[" + getDeviceID() + "]", "IndoorTemp changed to: " + IndoorTemp);
                break;
            default:
                Log.println(Log.DEBUG,"ACDevice[" + getDeviceID() + "]", "Unknown attribute: " + attributeName + " set to: " + attributeValue);
                break;
        }
    }

    @Override
    public void SetPower(boolean isOn) {
        if(isOn)
        {
            _device.writeAttribute("202002", "202002", new IuSDKCallback() {
                @Override
                public void onCallback(uSDKErrorConst status) {
                    if(status != uSDKErrorConst.RET_USDK_OK)
                    {
                        try {
                            Log.println(Log.ERROR,"ACDevice[" + getDeviceID() + "]", status.name());
                            throw new Exception("Error during sending command");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        PoweredOn = false;
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
                            Log.println(Log.ERROR,"ACDevice[" + getDeviceID() + "]", status.name());
                            throw new Exception("Error during sending command");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        PoweredOn = true;
                    }
                }
            });
        }
        PoweredOn = isOn;
    }

    @Override
    public void SetHealthMode(boolean isOn) {
        if(PoweredOn) {
            if (isOn) {
                _device.writeAttribute("202006", "202006", new IuSDKCallback() {
                    @Override
                    public void onCallback(uSDKErrorConst status) {
                        if (status != uSDKErrorConst.RET_USDK_OK) {
                            try {
                                Log.println(Log.ERROR, "ACDevice[" + getDeviceID() + "]", status.name());
                                throw new Exception("Error during sending command");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            HealthMode = true;
                        }
                    }
                });
            } else {
                _device.writeAttribute("202005", "202005", new IuSDKCallback() {
                    @Override
                    public void onCallback(uSDKErrorConst status) {
                        if (status != uSDKErrorConst.RET_USDK_OK) {
                            try {
                                Log.println(Log.ERROR, "ACDevice[" + getDeviceID() + "]", status.name());
                                throw new Exception("Error during sending command");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            HealthMode = false;
                        }
                    }
                });
            }
        }
    }

    @Override
    public void SetTargetTemperature(int temp) {
        if(PoweredOn) {
            int settemp = temp;
            if (settemp > 30) {
                settemp = 30;
            }
            if (settemp < 16) {
                settemp = 16;
            }
            _device.writeAttribute("20200E", Integer.toString(settemp), new IuSDKCallback() {
                @Override
                public void onCallback(uSDKErrorConst status) {
                    if (status != uSDKErrorConst.RET_USDK_OK) {
                        try {
                            Log.println(Log.ERROR, "ACDevice[" + getDeviceID() + "]", status.name());
                            throw new Exception("Error during sending command");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        SetTemp = temp;
                    }
                }
            });
        }
    }

    @Override
    public void SetVerticalSway(boolean isOn) {
        if(PoweredOn) {
            if (isOn) {
                _device.writeAttribute("20200J", "302007", new IuSDKCallback() {
                    @Override
                    public void onCallback(uSDKErrorConst status) {
                        if (status != uSDKErrorConst.RET_USDK_OK) {
                            try {
                                Log.println(Log.ERROR, "ACDevice[" + getDeviceID() + "]", status.name());
                                throw new Exception("Error during sending command");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            VerticalSway = true;
                        }
                    }
                });
            } else {
                _device.writeAttribute("20200J", "302000", new IuSDKCallback() {
                    @Override
                    public void onCallback(uSDKErrorConst status) {
                        if (status != uSDKErrorConst.RET_USDK_OK) {
                            try {
                                Log.println(Log.ERROR, "ACDevice[" + getDeviceID() + "]", status.name());
                                throw new Exception("Error during sending command");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            VerticalSway = false;
                        }
                    }
                });
            }
        }
    }

    @Override
    public void SetHorizontalSway(boolean isOn) {
        if(PoweredOn) {
            if (isOn) {
                _device.writeAttribute("20200I", "302008", new IuSDKCallback() {
                    @Override
                    public void onCallback(uSDKErrorConst status) {
                        if (status != uSDKErrorConst.RET_USDK_OK) {
                            try {
                                Log.println(Log.ERROR, "ACDevice[" + getDeviceID() + "]", status.name());
                                throw new Exception("Error during sending command");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            HorizontalSway = true;
                        }
                    }
                });
            } else {
                _device.writeAttribute("20200I", "302000", new IuSDKCallback() {
                    @Override
                    public void onCallback(uSDKErrorConst status) {
                        if (status != uSDKErrorConst.RET_USDK_OK) {
                            try {
                                Log.println(Log.ERROR, "ACDevice[" + getDeviceID() + "]", status.name());
                                throw new Exception("Error during sending command");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            HorizontalSway = false;
                        }
                    }
                });
            }
        }
    }

    @Override
    public void SetFanSpeed(ACFanSpeed speed) {
        if(PoweredOn) {
            switch (speed) {
                case AUTO:
                    _device.writeAttribute("20200F", "302005", new IuSDKCallback() {
                        @Override
                        public void onCallback(uSDKErrorConst status) {
                            if (status != uSDKErrorConst.RET_USDK_OK) {
                                try {
                                    Log.println(Log.ERROR, "ACDevice[" + getDeviceID() + "]", status.name());
                                    throw new Exception("Error during sending command");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            else
                            {
                                FanSpeed = speed;
                            }
                        }
                    });
                    break;
                case HIGH:
                    _device.writeAttribute("20200F", "302001", new IuSDKCallback() {
                        @Override
                        public void onCallback(uSDKErrorConst status) {
                            if (status != uSDKErrorConst.RET_USDK_OK) {
                                try {
                                    Log.println(Log.ERROR, "ACDevice[" + getDeviceID() + "]", status.name());
                                    throw new Exception("Error during sending command");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            else
                            {
                                FanSpeed = speed;
                            }
                        }
                    });
                    break;
                case MID:
                    _device.writeAttribute("20200F", "302002", new IuSDKCallback() {
                        @Override
                        public void onCallback(uSDKErrorConst status) {
                            if (status != uSDKErrorConst.RET_USDK_OK) {
                                try {
                                    Log.println(Log.ERROR, "ACDevice[" + getDeviceID() + "]", status.name());
                                    throw new Exception("Error during sending command");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            else
                            {
                                FanSpeed = speed;
                            }
                        }
                    });
                    break;
                case LOW:
                    _device.writeAttribute("20200F", "302003", new IuSDKCallback() {
                        @Override
                        public void onCallback(uSDKErrorConst status) {
                            if (status != uSDKErrorConst.RET_USDK_OK) {
                                try {
                                    Log.println(Log.ERROR, "ACDevice[" + getDeviceID() + "]", status.name());
                                    throw new Exception("Error during sending command");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            else
                            {
                                FanSpeed = speed;
                            }
                        }
                    });
                    break;
            }
        }
    }

    @Override
    public void SetMode(ACMode mode) {
        if(PoweredOn)
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
                                    Log.println(Log.ERROR,"ACDevice[" + getDeviceID() + "]", status.name());
                                    throw new Exception("Error during sending command");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            else
                            {
                                Mode = mode;
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
                                    Log.println(Log.ERROR,"ACDevice[" + getDeviceID() + "]", status.name());
                                    throw new Exception("Error during sending command");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            else
                            {
                                Mode = mode;
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
                                    Log.println(Log.ERROR,"ACDevice[" + getDeviceID() + "]", status.name());
                                    throw new Exception("Error during sending command");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            else
                            {
                                Mode = mode;
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
                                    Log.println(Log.ERROR,"ACDevice[" + getDeviceID() + "]", status.name());
                                    throw new Exception("Error during sending command");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            else
                            {
                                Mode = mode;
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
                                    Log.println(Log.ERROR,"ACDevice[" + getDeviceID() + "]", status.name());
                                    throw new Exception("Error during sending command");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            else
                            {
                                Mode = mode;
                            }
                        }
                    });
                    break;
            }
        }
    }

    @Override
    public void sendSetData() {

    }
}
