
package com.fastfend.haieracbridge.haierapi.webapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Type {

    @SerializedName("typeIdentifier")
    @Expose
    public String typeIdentifier;
    @SerializedName("subType")
    @Expose
    public String subType;
    @SerializedName("specialCode")
    @Expose
    public String specialCode;
    @SerializedName("type")
    @Expose
    public String type;

}
