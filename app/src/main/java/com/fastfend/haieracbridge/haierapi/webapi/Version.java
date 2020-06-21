
package com.fastfend.haieracbridge.haierapi.webapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Version {

    @SerializedName("smartlink")
    @Expose
    public Smartlink smartlink;
    @SerializedName("eProtocolVer")
    @Expose
    public String eProtocolVer;

}
