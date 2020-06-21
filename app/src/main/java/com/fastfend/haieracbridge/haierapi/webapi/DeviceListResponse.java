
package com.fastfend.haieracbridge.haierapi.webapi;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeviceListResponse {

    @SerializedName("cityListResult")
    @Expose
    public List<CityListResult> cityListResult = null;
    @SerializedName("retCode")
    @Expose
    public String retCode;
}
