
package com.fastfend.haieracbridge.haierapi.webapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Smartlink {

    @SerializedName("smartLinkHardwareVersion")
    @Expose
    public String smartLinkHardwareVersion;
    @SerializedName("smartLinkDevfileVersion")
    @Expose
    public String smartLinkDevfileVersion;
    @SerializedName("smartLinkPlatform")
    @Expose
    public String smartLinkPlatform;
    @SerializedName("smartLinkSoftwareVersion")
    @Expose
    public String smartLinkSoftwareVersion;

}
