
package com.fastfend.haieracbridge.haierapi.webapi;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RoomDeviceResultList {

    @SerializedName("deviceList")
    @Expose
    public List<DeviceList> deviceList = null;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("id")
    @Expose
    public String id;

}
