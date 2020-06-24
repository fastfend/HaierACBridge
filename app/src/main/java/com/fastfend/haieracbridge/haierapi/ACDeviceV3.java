package com.fastfend.haieracbridge.haierapi;

import android.util.Log;

import com.haier.uhome.usdk.api.interfaces.IuSDKCallback;
import com.haier.uhome.usdk.api.uSDKArgument;
import com.haier.uhome.usdk.api.uSDKErrorConst;

import java.util.LinkedList;
import java.util.List;

// Name - AttributeName - AttributeValue
// Power Off            onOffStatus - false
// Power On             onOffStatus - true
// HealthModeOff        healthMode - false
// HealthModeOn         healthMode - true
// Mode (SMART)         operationMode - 0
// Mode (COOL)          operationMode - 1
// Mode (DRY)           operationMode - 2
// Mode (HEAT)          operationMode - 4
// Mode (FAN)           operationMode - 6
// Set Temperature      targetTemperature - <16,30>
// FanSpeed (HIGH)      windSpeed - 1
// FanSpeed (MID)       windSpeed - 2
// FanSpeed (LOW)       windSpeed - 3
// FanSpeed (AUTO)      windSpeed - 5
// HorizontalSway (Off) windDirectionHorizontal - 0
// HorizontalSway (On)  windDirectionHorizontal - 7
// VerticalSway (Off)   windDirectionVertical - 0
// VerticalSway (On)    windDirectionVertical - 8
// CurrentTemperature   indoorTemperature
// CurrentHumidity      indoorHumidity
// OutsideTemperature   outdoorTemperature

public class ACDeviceV3 extends ACDevice {
    public ACDeviceV3(String id, String name, String type, boolean onlineMode) {
        super(id, name, type, onlineMode);
    }

    @Override
    public void onDataUpdate(String attributeName, String attributeValue) {
        switch (attributeName)
        {
            case "onOffStatus":
                PoweredOn = attributeValue.equals("true");
                Log.println(Log.INFO,"ACDevice[" + getDeviceID() + "]", "Power set to: " + (PoweredOn ? "ON" : "OFF"));
                break;
            case "healthMode":
                HealthMode = attributeValue.equals("true");;
                Log.println(Log.INFO,"ACDevice[" + getDeviceID() + "]", "HealthMode set to: " + (HealthMode ? "ON" : "OFF"));
                break;
            case "operationMode":
                switch (attributeValue)
                {
                    case "0":
                        Mode = ACMode.SMART;
                        Log.println(Log.INFO,"ACDevice[" + getDeviceID() + "]", "Mode set to: SMART");
                        break;
                    case "1":
                        Mode = ACMode.COOL;
                        Log.println(Log.INFO,"ACDevice[" + getDeviceID() + "]", "Mode set to: COOL");
                        break;
                    case "2":
                        Mode = ACMode.DRY;
                        Log.println(Log.INFO,"ACDevice[" + getDeviceID() + "]", "Mode set to: DRY");
                        break;
                    case "4":
                        Mode = ACMode.HEAT;
                        Log.println(Log.INFO,"ACDevice[" + getDeviceID() + "]", "Mode set to: HEAT");
                        break;
                    case "6":
                        Mode = ACMode.FAN;
                        Log.println(Log.INFO,"ACDevice[" + getDeviceID() + "]", "Mode set to: FAN");
                        break;
                }
                break;
            case "targetTemperature":
                SetTemp = (int) Double.parseDouble(attributeValue);
                Log.println(Log.INFO,"ACDevice[" + getDeviceID() + "]", "Set Temperature changed to: " + attributeValue);
                break;
            case "windSpeed":
                switch (attributeValue)
                {
                    case "1":
                        Mode = ACMode.SMART;
                        Log.println(Log.INFO,"ACDevice[" + getDeviceID() + "]", "FanSpeed set to: HIGH");
                        break;
                    case "2":
                        Mode = ACMode.COOL;
                        Log.println(Log.INFO,"ACDevice[" + getDeviceID() + "]", "FanSpeed set to: MID");
                        break;
                    case "3":
                        Mode = ACMode.DRY;
                        Log.println(Log.INFO,"ACDevice[" + getDeviceID() + "]", "FanSpeed set to: LOW");
                        break;
                    case "5":
                        Mode = ACMode.HEAT;
                        Log.println(Log.INFO,"ACDevice[" + getDeviceID() + "]", "FanSpeed set to: AUTO");
                        break;
                }
                break;
            case "windDirectionVertical":
                HorizontalSway = attributeValue.equals("8");
                Log.println(Log.INFO,"ACDevice[" + getDeviceID() + "]", "HorizontalSway set to: " + (HorizontalSway ? "ON" : "OFF"));
                break;
            case "windDirectionHorizontal":
                VerticalSway = attributeValue.equals("7");
                Log.println(Log.INFO,"ACDevice[" + getDeviceID() + "]", "VerticalSway set to: " + (VerticalSway ? "ON" : "OFF"));
                break;
            case "indoorTemperature":
                IndoorTemp = Double.parseDouble(attributeValue);
                Log.println(Log.INFO,"ACDevice[" + getDeviceID() + "]", "IndoorTemp changed to: " + attributeValue);
                break;
            case "indoorHumidity":
                Humidity = Integer.decode(attributeValue);
                Log.println(Log.INFO,"ACDevice[" + getDeviceID() + "]", "Humidity changed to: " + attributeValue);
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
            _device.writeAttribute("onOffStatus", "true", new IuSDKCallback() {
                @Override
                public void onCallback(uSDKErrorConst uSDKErrorConst) {
                    if(uSDKErrorConst != uSDKErrorConst.RET_USDK_OK)
                        Log.println(Log.ERROR, "ACDevice[" + getDeviceID() + "]", uSDKErrorConst.name());
                }
            });
        }
        else
        {
            _device.writeAttribute("onOffStatus", "false", new IuSDKCallback() {
                @Override
                public void onCallback(uSDKErrorConst uSDKErrorConst) {
                    if(uSDKErrorConst != uSDKErrorConst.RET_USDK_OK)
                        Log.println(Log.ERROR, "ACDevice[" + getDeviceID() + "]", uSDKErrorConst.name());
                }
            });
        }
        PoweredOn = isOn;
    }

    @Override
    public void SetHealthMode(boolean isOn) {
        if(PoweredOn) {
            HealthMode = isOn;
        }
    }

    @Override
    public void SetTargetTemperature(int temp) {
        if(PoweredOn) {
            SetTemp = temp;
        }
    }

    @Override
    public void SetVerticalSway(boolean isOn) {
        if(PoweredOn) {
            VerticalSway = isOn;
        }
    }

    @Override
    public void SetHorizontalSway(boolean isOn) {
        if(PoweredOn) {
            HorizontalSway = isOn;
        }
    }

    @Override
    public void SetFanSpeed(ACFanSpeed speed) {
        if(PoweredOn) {
            FanSpeed = speed;
        }
    }

    @Override
    public void SetMode(ACMode mode) {
        if(PoweredOn) {
            Mode = mode;
        }
    }

    @Override
    public void sendSetData()
    {
        if(PoweredOn) {
            List<uSDKArgument> arguments = new LinkedList<>();
            // Target Temp
            arguments.add(new uSDKArgument("targetTemperature", String.valueOf(SetTemp)));
            // Horizontal Sway
            arguments.add(new uSDKArgument("windDirectionVertical", HorizontalSway ? "8" : "0"));
            //Mode
            String tempmode = "0";
            switch (Mode)
            {
                case SMART:
                    tempmode = "0";
                    break;
                case COOL:
                    tempmode = "1";
                    break;
                case DRY:
                    tempmode = "2";
                    break;
                case HEAT:
                    tempmode = "4";
                    break;
                case FAN:
                    tempmode = "6";
                    break;

            }
            arguments.add(new uSDKArgument("operationMode", tempmode));
            //
            arguments.add(new uSDKArgument("specialMode", getValueFromAttribute("specialMode")));
            //Wind Speed
            String windmode = "0";
            switch (FanSpeed)
            {
                case HIGH:
                    windmode = "1";
                    break;
                case MID:
                    windmode = "2";
                    break;
                case LOW:
                    windmode = "3";
                    break;
                case AUTO:
                    windmode = "5";
                    break;
            }
            arguments.add(new uSDKArgument("windSpeed", windmode));
            //Other
            arguments.add(new uSDKArgument("energySavePeriod", getValueFromAttribute("energySavePeriod")));
            arguments.add(new uSDKArgument("tempUnit", getValueFromAttribute("tempUnit")));
            arguments.add(new uSDKArgument("pmvStatus", getValueFromAttribute("pmvStatus")));
            arguments.add(new uSDKArgument("intelligenceStatus", getValueFromAttribute("intelligenceStatus")));
            arguments.add(new uSDKArgument("halfDegreeSettingStatus", getValueFromAttribute("halfDegreeSettingStatus")));
            arguments.add(new uSDKArgument("screenDisplayStatus", getValueFromAttribute("screenDisplayStatus")));
            arguments.add(new uSDKArgument("10degreeHeatingStatus", getValueFromAttribute("10degreeHeatingStatus")));
            arguments.add(new uSDKArgument("echoStatus", getValueFromAttribute("echoStatus")));
            arguments.add(new uSDKArgument("lockStatus", getValueFromAttribute("lockStatus")));
            arguments.add(new uSDKArgument("silentSleepStatus", getValueFromAttribute("silentSleepStatus")));
            arguments.add(new uSDKArgument("muteStatus", getValueFromAttribute("muteStatus")));
            arguments.add(new uSDKArgument("rapidMode", getValueFromAttribute("rapidMode")));
            arguments.add(new uSDKArgument("electricHeatingStatus", getValueFromAttribute("electricHeatingStatus")));
            //HealthMode
            arguments.add(new uSDKArgument("healthMode", (HealthMode ? "true" : "false")));
            //PowerMode
            arguments.add(new uSDKArgument("onOffStatus", (PoweredOn ? "true" : "false")));
            //Other
            arguments.add(new uSDKArgument("targetHumidity", getValueFromAttribute("targetHumidity")));
            arguments.add(new uSDKArgument("humanSensingStatus", getValueFromAttribute("humanSensingStatus")));
            //VerticalSway
            arguments.add(new uSDKArgument("windDirectionHorizontal", VerticalSway ? "7" : "0"));
            //Other
            arguments.add(new uSDKArgument("cloudFilterChangeFlag", getValueFromAttribute("cloudFilterChangeFlag")));
            arguments.add(new uSDKArgument("cleaningTimeStatus", getValueFromAttribute("cleaningTimeStatus")));
            arguments.add(new uSDKArgument("energySavingStatus", getValueFromAttribute("energySavingStatus")));
            arguments.add(new uSDKArgument("lightStatus", getValueFromAttribute("lightStatus")));
            arguments.add(new uSDKArgument("selfCleaningStatus", getValueFromAttribute("selfCleaningStatus")));
            arguments.add(new uSDKArgument("ch2oCleaningStatus", getValueFromAttribute("ch2oCleaningStatus")));
            arguments.add(new uSDKArgument("pm2p5CleaningStatus", getValueFromAttribute("pm2p5CleaningStatus")));
            arguments.add(new uSDKArgument("humidificationStatus", getValueFromAttribute("humidificationStatus")));
            arguments.add(new uSDKArgument("freshAirStatus", getValueFromAttribute("freshAirStatus")));

            _device.execOperation("grSetDAC", arguments, new IuSDKCallback() {
                @Override
                public void onCallback(uSDKErrorConst uSDKErrorConst) {
                    Log.println(Log.ERROR, "ACDevice[" + getDeviceID() + "]", uSDKErrorConst.name());
                }
            });
        }
    }
}
