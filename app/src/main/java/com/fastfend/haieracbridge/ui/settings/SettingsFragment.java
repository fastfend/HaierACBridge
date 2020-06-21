package com.fastfend.haieracbridge.ui.settings;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.fastfend.haieracbridge.R;
import com.fastfend.haieracbridge.haierapi.ACDeviceManager;
import com.fastfend.haieracbridge.haierapi.ACDeviceManagerState;
import com.fastfend.haieracbridge.haierapi.UserManager;
import com.fastfend.haieracbridge.haierapi.webapi.WebApiResultStatus;
import com.haier.uhome.usdk.api.uSDKErrorConst;

public class SettingsFragment extends Fragment {
    private final ACDeviceManager deviceManager = ACDeviceManager.getInstance();
    private final UserManager userManager = UserManager.getInstance();

    private SettingsViewModel settingsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel =
                ViewModelProviders.of(this).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        Activity activity = this.getActivity();
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);


        final TextView loginTitleView = root.findViewById(R.id.loginTitle);
        settingsViewModel.getTitleText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                loginTitleView.setText(s);
            }
        });

        final EditText loginTextBox = root.findViewById(R.id.login_textbox);
        final EditText passwordTextBox = root.findViewById(R.id.password_textbox);
        final ProgressBar connectingBar = root.findViewById(R.id.connectingBar);
        final Button resetToken = root.findViewById(R.id.resettoken_button);
        final Button loginButton = root.findViewById(R.id.login_button);
        final CheckBox autoStartCheckBox = root.findViewById(R.id.autoStartCheckBox);

        autoStartCheckBox.setChecked(userManager.getAutoStart());

        if(userManager.isLoggedIn())
        {
            loginButton.setText(R.string.button_logout_text);
            loginTextBox.setVisibility(View.GONE);
            passwordTextBox.setVisibility(View.GONE);
            loginTitleView.setText(getString(R.string.logged_in_info, userManager.getAuthUserName()));
        }
        else
        {
            loginButton.setText(R.string.button_login_text);
            resetToken.setVisibility(View.GONE);
            autoStartCheckBox.setVisibility(View.GONE);
        }

        autoStartCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked)
            {
                userManager.setAutoStart(true);
            }
            else {
                userManager.setAutoStart(false);
            }
        });

        resetToken.setOnClickListener(v ->
        {
            resetToken.setEnabled(false);
            loginButton.setEnabled(false);
            autoStartCheckBox.setEnabled(false);

            deviceManager.stop(devval -> {
                deviceManager.setApiToken(deviceManager.getNewApiToken());
                userManager.GetUserDevices(userManager.getAuthToken(), (status, list) -> {
                    if(status == WebApiResultStatus.OK)
                    {
                        ACDeviceManager mgr = ACDeviceManager.getInstance();
                        mgr.start(userManager.getAuthToken(), list, value -> {
                            if(value == uSDKErrorConst.RET_USDK_OK)
                            {
                                sendToastSafe(R.string.token_regen_ok, activity);
                            }
                            activity.runOnUiThread(() -> {
                                resetToken.setEnabled(true);
                                autoStartCheckBox.setEnabled(true);
                                loginButton.setEnabled(true);
                            });
                        });
                    }
                    else
                    {
                        activity.runOnUiThread(() -> {
                            resetToken.setEnabled(true);
                            autoStartCheckBox.setEnabled(true);
                            loginButton.setEnabled(true);
                        });
                    }
                });
            });
        });

        loginButton.setOnClickListener(v -> {
            loginButton.setEnabled(false);
            connectingBar.setVisibility(View.VISIBLE);
            loginTextBox.setEnabled(false);
            passwordTextBox.setEnabled(false);

            if(userManager.isLoggedIn())
            {
                if(deviceManager.getState() == ACDeviceManagerState.NOT_LOGGED_IN || deviceManager.getState() == ACDeviceManagerState.DISABLED)
                {
                    userManager.resetAuthData();
                    activity.runOnUiThread(() -> {
                        loginTitleView.setText(R.string.login_info);
                        connectingBar.setVisibility(View.INVISIBLE);
                        resetToken.setVisibility(View.GONE);
                        autoStartCheckBox.setVisibility(View.GONE);

                        loginTextBox.setVisibility(View.VISIBLE);
                        loginTextBox.setEnabled(true);

                        passwordTextBox.setVisibility(View.VISIBLE);
                        passwordTextBox.setEnabled(true);

                        loginButton.setText(R.string.button_login_text);
                        loginButton.setEnabled(true);

                        sendToastUnsafe(R.string.toast_logged_out, activity);
                    });
                }
                else {
                    deviceManager.stop(value -> {
                        userManager.resetAuthData();
                        activity.runOnUiThread(() -> {
                            loginTitleView.setText(R.string.login_info);
                            connectingBar.setVisibility(View.INVISIBLE);
                            resetToken.setVisibility(View.GONE);
                            autoStartCheckBox.setVisibility(View.GONE);

                            loginTextBox.setVisibility(View.VISIBLE);
                            loginTextBox.setEnabled(true);

                            passwordTextBox.setVisibility(View.VISIBLE);
                            passwordTextBox.setEnabled(true);

                            loginButton.setText(R.string.button_login_text);
                            loginButton.setEnabled(true);

                            sendToastUnsafe(R.string.toast_logged_out, activity);
                        });
                    });
                }
            }
            else
            {
                userManager.GetLoginTokenFromHaier(loginTextBox.getText().toString(), passwordTextBox.getText().toString(), (authstatus, token) -> {
                    if(authstatus == WebApiResultStatus.OK)
                    {
                        userManager.setAuthToken(token);
                        userManager.setAuthUserName(loginTextBox.getText().toString());
                        userManager.GetUserDevices(token, (status, list) -> {
                            if (status == WebApiResultStatus.OK) {
                                deviceManager.start(token, list, value -> {
                                    if(value == uSDKErrorConst.RET_USDK_OK)
                                    {
                                        activity.runOnUiThread(() -> {
                                            connectingBar.setVisibility(View.INVISIBLE);
                                            loginTitleView.setText(getString(R.string.logged_in_info, loginTextBox.getText().toString()));
                                            loginButton.setText(R.string.button_logout_text);
                                            loginButton.setEnabled(true);
                                            loginTextBox.setVisibility(View.GONE);
                                            passwordTextBox.setVisibility(View.GONE);
                                            resetToken.setVisibility(View.VISIBLE);
                                            autoStartCheckBox.setVisibility(View.VISIBLE);
                                            sendToastUnsafe(R.string.toast_connected, activity);
                                        });

                                    }
                                    else
                                    {
                                        activity.runOnUiThread(() -> {
                                            connectingBar.setVisibility(View.INVISIBLE);
                                            loginTitleView.setText(getString(R.string.logged_in_info, loginTextBox.getText().toString()));
                                            loginButton.setText(R.string.button_logout_text);
                                            loginButton.setEnabled(true);
                                            loginTextBox.setVisibility(View.GONE);
                                            passwordTextBox.setVisibility(View.GONE);
                                            sendToastUnsafe(getString(R.string.toast_sdk_start_failed, value.name()), activity);
                                        });
                                    }
                                });
                            } else {
                                activity.runOnUiThread(() -> {
                                    connectingBar.setVisibility(View.INVISIBLE);
                                    loginButton.setText(R.string.button_logout_text);
                                    loginButton.setEnabled(true);
                                    loginTextBox.setVisibility(View.GONE);
                                    passwordTextBox.setVisibility(View.GONE);
                                    sendToastUnsafe(R.string.toast_getting_devices_failed, activity);
                                });
                            }
                        });
                    }
                    else
                    {
                        activity.runOnUiThread(() -> {
                            connectingBar.setVisibility(View.INVISIBLE);
                            loginButton.setEnabled(true);
                            loginTextBox.setEnabled(true);
                            passwordTextBox.setEnabled(true);
                            loginTextBox.setVisibility(View.VISIBLE);
                            passwordTextBox.setVisibility(View.VISIBLE);
                            sendToastUnsafe(R.string.toast_wrong_auth, activity);
                        });
                    }
                });
            }
        });

        return root;
    }
    public void sendToastSafe(int text, Activity activity)
    {
        activity.runOnUiThread(() -> {
            sendToastUnsafe(text, activity);
        });
    }
    public void sendToastSafe(String text, Activity activity)
    {
        activity.runOnUiThread(() -> {
            sendToastUnsafe(text, activity);
        });
    }
    public void sendToastUnsafe(int text, Activity activity)
    {
        Toast.makeText(activity.getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
    public void sendToastUnsafe(String text, Activity activity)
    {
        Toast.makeText(activity.getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
}