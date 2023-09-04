package com.efojug.assetsmgr.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.efojug.assetsmgr.R;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {

        setPreferencesFromResource(R.xml.fragment_settings, rootKey);

        SwitchPreferenceCompat darkModeSwitch =
                findPreference("dark_mode");

        SwitchPreferenceCompat autoDarkModeSwitch =
                findPreference("auto_dark_mode");

        darkModeSwitch.setEnabled(autoDarkModeSwitch.isChecked() ? false : true);

        darkModeSwitch.setOnPreferenceChangeListener((preference, newValue) -> {
            if ((boolean) newValue)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            return true;
        });

        autoDarkModeSwitch.setOnPreferenceChangeListener((preference, newValue) -> {
            if ((boolean) newValue)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            else
                AppCompatDelegate.setDefaultNightMode(darkModeSwitch.isChecked() ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
            // 当自动切换深色模式开关被启用时，禁用深色模式开关
            darkModeSwitch.setEnabled(!(boolean) newValue);
            return true;
        });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("dark_mode")) {
            boolean isDarkModeOn = sharedPreferences.getBoolean(key, false);
            AppCompatDelegate.setDefaultNightMode(isDarkModeOn ?
                    AppCompatDelegate.MODE_NIGHT_YES :
                    AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

}