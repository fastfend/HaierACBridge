
package com.fastfend.haieracbridge.haierapi.webapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Location {

    @SerializedName("latitude")
    @Expose
    public String latitude;
    @SerializedName("longitude")
    @Expose
    public String longitude;
    @SerializedName("cityCode")
    @Expose
    public String cityCode;

}
