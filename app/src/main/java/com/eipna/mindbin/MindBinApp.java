package com.eipna.mindbin;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import com.eipna.mindbin.data.enums.Theme;
import com.eipna.mindbin.util.SharedPreferenceUtil;
import com.google.android.material.color.DynamicColors;

public class MindBinApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferenceUtil sharedPreferenceUtil = new SharedPreferenceUtil(this);
        String themeVal = sharedPreferenceUtil.getString("theme", Theme.get(Theme.SYSTEM));
        boolean dynamicColorsVal = sharedPreferenceUtil.getBoolean("dynamic_colors", false);

        if (dynamicColorsVal) {
            DynamicColors.applyToActivitiesIfAvailable(this);
        }

        if (themeVal.equals(Theme.get(Theme.SYSTEM))) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        } else if (themeVal.equals(Theme.get(Theme.LIGHT))) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else if (themeVal.equals(Theme.get(Theme.DARK))) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else if (themeVal.equals(Theme.get(Theme.BATTERY_SAVING))) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
        }
    }
}