package com.eipna.mindbin.ui.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.eipna.mindbin.util.PreferenceUtil;
import com.eipna.mindbin.util.ThemeUtil;

public abstract class BaseActivity extends AppCompatActivity {

    protected PreferenceUtil preferences;
    protected ThemeUtil themeUtil;

    protected final int RESULT_DELETE = 404;
    protected final int RESULT_UPDATE_STATE = 101;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        preferences = new PreferenceUtil(this);
        themeUtil = new ThemeUtil(this);
        super.onCreate(savedInstanceState);

        themeUtil.setTheme();
        themeUtil.setContrast();
        themeUtil.setDynamicColors();
    }
}