package com.eipna.mindbin;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import com.eipna.mindbin.data.enums.Theme;
import com.eipna.mindbin.util.SharedPreferenceUtil;

public class MindBinApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferenceUtil sharedPreferenceUtil = new SharedPreferenceUtil(this);
        String applicationTheme = sharedPreferenceUtil.getString("theme", Theme.SYSTEM.value);

        if (applicationTheme.equals(Theme.SYSTEM.value)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        } else if (applicationTheme.equals(Theme.LIGHT.value)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else if (applicationTheme.equals(Theme.DARK.value)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else if (applicationTheme.equals(Theme.BATTERY_SAVING.value)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
        }
    }
}