package com.fastfend.haieracbridge.haierapi;

import com.fastfend.haieracbridge.haierapi.webapi.WebApiResultStatus;
import com.haier.uhome.usdk.api.uSDKErrorConst;

import java.util.LinkedList;
import java.util.List;
import java.util.TimerTask;

public class DeviceChecker extends TimerTask {
    private final UserManager userManager;
    private final ACDeviceManager deviceManager;
    private List<ACDevice> baselist;

    public DeviceChecker(UserManager _userManager, ACDeviceManager _deviceManager, List<ACDevice> _baselist)
    {
        deviceManager = _deviceManager;
        userManager = _userManager;
        baselist = _baselist;
    }

    public void run() {
        userManager.GetUserDevices(userManager.getAuthToken(), (status, updatelist) -> {
            if(status == WebApiResultStatus.OK) {
                if(!CheckEquality(baselist, updatelist)) {
                    deviceManager.stop(value -> {
                        if(value == uSDKErrorConst.RET_USDK_OK)
                        {
                            deviceManager.start(userManager.getAuthToken(), updatelist, restartstatus ->
                            {
                                baselist = updatelist;
                            });
                        }
                    });
                }
            }
        });
    }

    private boolean CheckEquality(List<ACDevice> first, List<ACDevice> second)
    {
        if(first.size() != second.size())
        {
            return false;
        }
        else
        {
            LinkedList<String> firstIDs = new LinkedList<String>();
            for(ACDevice dev : first)
            {
                firstIDs.add(dev.getDeviceID());
            }

            LinkedList<String> secondIDs = new LinkedList<String>();
            for(ACDevice dev : second)
            {
                secondIDs.add(dev.getDeviceID());
            }

            if(firstIDs.containsAll(secondIDs))
            {
                return true;
            }
            else
            {
                return false;
            }
        }

    }
}
