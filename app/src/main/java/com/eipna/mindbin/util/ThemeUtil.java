package com.eipna.mindbin.util;

import android.app.Activity;

import androidx.appcompat.app.AppCompatDelegate;

import com.eipna.mindbin.R;
import com.eipna.mindbin.data.Contrast;
import com.eipna.mindbin.data.Theme;
import com.google.android.material.color.DynamicColors;

public class ThemeUtil {

    private final Activity activity;
    private final PreferenceUtil preferences;

    public ThemeUtil(Activity activity) {
        this.activity = activity;
        this.preferences = new PreferenceUtil(activity);
    }

    public void setTheme() {
        String theme = preferences.getTheme();
        if (theme.equals(Theme.SYSTEM.value)) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        if (theme.equals(Theme.LIGHT.value)) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        if (theme.equals(Theme.DARK.value)) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        if (theme.equals(Theme.BATTERY_SAVING.value)) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
    }

    public void setContrast() {
        String contrast = preferences.getContrast();
        if (contrast.equals(Contrast.LOW.value)) activity.setTheme(R.style.Theme_MindBin);
        if (contrast.equals(Contrast.MEDIUM.value)) activity.setTheme(R.style.Theme_MindBin_MediumContrast);
        if (contrast.equals(Contrast.HIGH.value)) activity.setTheme(R.style.Theme_MindBin_HighContrast);
    }

    public void setDynamicColors() {
        if (preferences.isDynamicColorsEnabled()) DynamicColors.applyToActivityIfAvailable(activity);
    }
}