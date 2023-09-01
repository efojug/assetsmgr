package com.efojug.assetsmgr.ui.settings;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceFragmentCompat;

import com.efojug.assetsmgr.R;

public class SettingsFragment extends PreferenceFragmentCompat {


    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.fragment_settings, rootKey);
        SettingsViewModel settingsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);
    }
}