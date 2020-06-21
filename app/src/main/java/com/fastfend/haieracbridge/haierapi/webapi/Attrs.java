
package com.fastfend.haieracbridge.haierapi.webapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Attrs {

    @SerializedName("model")
    @Expose
    public String model;
    @SerializedName("brand")
    @Expose
    public Object brand;

}
