package com.eipna.mindbin.ui.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.eipna.mindbin.R;
import com.eipna.mindbin.data.Contrast;
import com.eipna.mindbin.data.Theme;
import com.eipna.mindbin.util.PreferenceUtil;
import com.google.android.material.color.DynamicColors;

public abstract class BaseActivity extends AppCompatActivity {

    protected PreferenceUtil preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = new PreferenceUtil(this);

        String theme = preferences.getTheme();
        if (theme.equals(Theme.SYSTEM.value)) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        if (theme.equals(Theme.LIGHT.value)) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        if (theme.equals(Theme.DARK.value)) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        if (theme.equals(Theme.BATTERY_SAVING.value)) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);

        String contrast = preferences.getContrast();
        if (contrast.equals(Contrast.LOW.value)) setTheme(R.style.Theme_MindBin);
        if (contrast.equals(Contrast.MEDIUM.value)) setTheme(R.style.Theme_MindBin_MediumContrast);
        if (contrast.equals(Contrast.HIGH.value)) setTheme(R.style.Theme_MindBin_HighContrast);

        if (preferences.isDynamicColorsEnabled()) DynamicColors.applyToActivityIfAvailable(BaseActivity.this);
    }
}