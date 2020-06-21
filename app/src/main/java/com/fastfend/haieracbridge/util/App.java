package com.fastfend.haieracbridge.util;

import android.app.Application;

import com.fastfend.haieracbridge.haierapi.ACDeviceManager;
import com.fastfend.haieracbridge.haierapi.UserManager;
import com.haier.uhome.account.api.interfaces.IAccountListener;
import com.haier.uhome.account.api.uAccount;
import com.haier.uhome.account.model.RespCommonModel;
import com.haier.uhome.usdk.api.uSDKManager;
import com.bugfender.sdk.Bugfender;
import com.bugfender.android.BuildConfig;

import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

//        Bugfender.init(this, "ZB2iVWdZpya16BHx3rpDELg1PbsCCG62", BuildConfig.DEBUG);
//        Bugfender.enableCrashReporting();
//        Bugfender.enableUIEventLogging(this);
//        Bugfender.enableLogcatLogging();
    }

}
