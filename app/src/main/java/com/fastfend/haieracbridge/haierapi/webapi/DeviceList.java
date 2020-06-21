
package com.fastfend.haieracbridge.haierapi.webapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeviceList {

    @SerializedName("userId")
    @Expose
    public Object userId;
    @SerializedName("filterState")
    @Expose
    public String filterState;
    @SerializedName("cityId")
    @Expose
    public String cityId;
    @SerializedName("devType")
    @Expose
    public Object devType;
    @SerializedName("taskTimingStart")
    @Expose
    public boolean taskTimingStart;
    @SerializedName("attrs")
    @Expose
    public Attrs attrs;
    @SerializedName("baseboard")
    @Expose
    public Object baseboard;
    @SerializedName("version")
    @Expose
    public Version version;
    @SerializedName("status")
    @Expose
    public Status status;
    @SerializedName("mac")
    @Expose
    public String mac;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("location")
    @Expose
    public Location location;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("type")
    @Expose
    public Type type;

}
