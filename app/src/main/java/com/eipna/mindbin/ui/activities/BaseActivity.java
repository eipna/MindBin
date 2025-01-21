package com.eipna.mindbin.ui.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.eipna.mindbin.util.SharedPreferenceUtil;
import com.google.android.material.color.DynamicColors;

public abstract class BaseActivity extends AppCompatActivity {

    protected SharedPreferenceUtil sharedPreferenceUtil;
    protected final int RESULT_DELETE = 404;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        sharedPreferenceUtil = new SharedPreferenceUtil(this);

        super.onCreate(savedInstanceState);

        boolean isDynamicColorEnabled = sharedPreferenceUtil.getBoolean("dynamic_colors", false);
        if (isDynamicColorEnabled) DynamicColors.applyToActivityIfAvailable(this);
    }
}