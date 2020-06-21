package com.fastfend.haieracbridge.haierapi.webapi;

import com.fastfend.haieracbridge.haierapi.ACDevice;
import java.util.List;

public interface WebApiDevicesCallback {
     void onFinish(WebApiResultStatus status, List<ACDevice> list);
}
