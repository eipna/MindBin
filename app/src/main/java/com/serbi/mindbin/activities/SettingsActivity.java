package com.serbi.mindbin.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.serbi.mindbin.R;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private boolean isNightMode;
    private boolean isGridMode;

    private MaterialToolbar toolbar;
    private MaterialSwitch switch_list, switch_theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeComponents();
        setupToolbar();

        if (isGridMode) {
            switch_list.setChecked(true);
        }

        if (isNightMode) {
            switch_theme.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        switch_theme.setOnClickListener(v -> {
            if (isNightMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                editor = preferences.edit();
                editor.putBoolean("isNightMode", false);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                editor = preferences.edit();
                editor.putBoolean("isNightMode", true);
            }
            editor.apply();
        });

        switch_list.setOnClickListener(v -> {
            if (isGridMode) {
                editor = preferences.edit();
                editor.putBoolean("isGridMode", false);
            } else {
                editor = preferences.edit();
                editor.putBoolean("isGridMode", true);
            }
            editor.apply();
        });

        ConstraintLayout openGithub = findViewById(R.id.settings_github);
        openGithub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/eipna/MindBin"));
                startActivity(browserIntent);
            }
        });
    }

    private void initializeComponents() {
        toolbar = findViewById(R.id.toolbar_settings);
        switch_list = findViewById(R.id.settings_list_switch);
        switch_theme = findViewById(R.id.settings_theme_switch);

        preferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        isNightMode = preferences.getBoolean("isNightMode", false);
        isGridMode = preferences.getBoolean("isGridMode", false);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.previous_arrow_backward_svgrepo_com);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }
}