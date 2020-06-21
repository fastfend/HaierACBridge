
package com.fastfend.haieracbridge.haierapi.webapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthResponse {

    @SerializedName("retInfo")
    @Expose
    public String retInfo;
    @SerializedName("retCode")
    @Expose
    public String retCode;
    @SerializedName("accessToken")
    @Expose
    public String accessToken;
    @SerializedName("userId")
    @Expose
    public String userId;
    @SerializedName("actived")
    @Expose
    public String actived;

}
