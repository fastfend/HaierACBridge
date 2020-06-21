
package com.fastfend.haieracbridge.haierapi.webapi;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CityListResult {

    @SerializedName("cityId")
    @Expose
    public String cityId;
    @SerializedName("roomDeviceResultList")
    @Expose
    public List<RoomDeviceResultList> roomDeviceResultList = null;

}
