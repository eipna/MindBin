package com.eipna.mindbin.ui.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.eipna.mindbin.data.Theme;
import com.eipna.mindbin.util.PreferenceUtil;
import com.google.android.material.color.DynamicColors;

public abstract class BaseActivity extends AppCompatActivity {

    protected PreferenceUtil preferences;

    protected final int RESULT_DELETE = 404;
    protected final int RESULT_UPDATE_STATE = 101;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        preferences = new PreferenceUtil(this);
        super.onCreate(savedInstanceState);

        String theme = preferences.getTheme();
        if (theme.equals(Theme.SYSTEM.value)) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        if (theme.equals(Theme.LIGHT.value)) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        if (theme.equals(Theme.DARK.value)) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        if (theme.equals(Theme.BATTERY_SAVING.value)) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);

        if (preferences.isDynamicColorsEnabled()) DynamicColors.applyToActivityIfAvailable(this);
    }
}