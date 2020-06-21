package com.fastfend.haieracbridge.ui.info;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.fastfend.haieracbridge.R;

public class InfoFragment extends Fragment {

    private InfoViewModel infoViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        infoViewModel =
                ViewModelProviders.of(this).get(InfoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_info, container, false);
        final TextView statusTextView = root.findViewById(R.id.text_state);
        final TextView tokenTextView = root.findViewById(R.id.text_token);
        final TextView ipTextView = root.findViewById(R.id.text_ip);
        infoViewModel.getStatusText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                statusTextView.setText(s);
            }
        });
        infoViewModel.getTokenText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                tokenTextView.setText(s);
            }
        });
        infoViewModel.getIPText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                ipTextView.setText(s);
            }
        });
        return root;
    }
}