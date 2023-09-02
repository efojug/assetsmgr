package com.efojug.assetsmgr.ui.settings;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;

import com.efojug.assetsmgr.R;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.fragment_settings, rootKey);

        SwitchPreferenceCompat darkModeSwitch =
                findPreference("dark_mode");
        boolean isDarkModeOn = darkModeSwitch.isChecked();

        SwitchPreferenceCompat autoDarkModeSwitch =
                findPreference("auto_dark_mode");
        boolean isAutoDarkModeOn = autoDarkModeSwitch.isChecked();

        if (isAutoDarkModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            darkModeSwitch.setEnabled(false);
        } else {
            int nightMode = isDarkModeOn ? AppCompatDelegate.MODE_NIGHT_YES :
                    AppCompatDelegate.MODE_NIGHT_NO;
            AppCompatDelegate.setDefaultNightMode(nightMode);
            darkModeSwitch.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean darkModeEnabled = (boolean) newValue;
                int nightModeF = darkModeEnabled ? AppCompatDelegate.MODE_NIGHT_YES :
                        AppCompatDelegate.MODE_NIGHT_NO;
                AppCompatDelegate.setDefaultNightMode(nightModeF);
                return true;
            });
        }
        autoDarkModeSwitch.setOnPreferenceChangeListener((preference, newValue) -> {
            boolean autoDarkModeEnabled = (boolean) newValue;
            AppCompatDelegate.setDefaultNightMode(autoDarkModeEnabled ?
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM :
                    AppCompatDelegate.MODE_NIGHT_NO);
            // 当自动切换深色模式开关被启用时，禁用深色模式开关
            darkModeSwitch.setEnabled(!autoDarkModeEnabled);
            return true;
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(getContext()).unregisterOnSharedPreferenceChangeListener(this);
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