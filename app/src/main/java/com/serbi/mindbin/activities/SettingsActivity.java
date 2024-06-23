package com.serbi.mindbin.activities;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.gson.Gson;
import com.serbi.mindbin.R;
import com.serbi.mindbin.helpers.DatabaseHelper;
import com.serbi.mindbin.models.NoteExportModel;
import com.serbi.mindbin.models.NoteModel;

import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    ConstraintLayout openGithub, openLibraries, backup_import, backup_export;

    private boolean isNightMode, isGridMode;

    private MaterialToolbar toolbar;
    private MaterialSwitch switch_list, switch_theme;

    private DatabaseHelper databaseHelper;

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

        openGithub.setOnClickListener(v -> {
            final String githubLink = "https://github.com/eipna/MindBin";
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(githubLink));
            startActivity(browserIntent);
        });

        openLibraries.setOnClickListener(v -> showLibrariesDialog());
        backup_import.setOnClickListener(v -> importNotesToJSON());
        backup_export.setOnClickListener(v -> exportNotesToJSON());
    }

    private void showLibrariesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
        builder.setTitle("Libraries").
                setItems(R.array.settings_libraries, (dialog, which) -> {
                    if (which == 0) {
                        final String githubLink = "https://google.github.io/gson";
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(githubLink));
                        startActivity(browserIntent);
                    }
                }).setNegativeButton("Cancel", (dialog, which) -> {
                });
        builder.create().show();
    }

    private void initializeComponents() {
        toolbar = findViewById(R.id.toolbar_settings);
        switch_list = findViewById(R.id.settings_list_switch);
        switch_theme = findViewById(R.id.settings_theme_switch);

        openGithub = findViewById(R.id.settings_github);
        backup_export = findViewById(R.id.settings_export);
        backup_import = findViewById(R.id.settings_import);
        openLibraries = findViewById(R.id.settings_libraries);

        preferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        isNightMode = preferences.getBoolean("isNightMode", false);
        isGridMode = preferences.getBoolean("isGridMode", false);

        databaseHelper = new DatabaseHelper(SettingsActivity.this);
    }

    private void exportNotesToJSON() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/json");
        intent.putExtra(Intent.EXTRA_TITLE, "exported_notes.json"); // Suggest filename
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        startActivityForResult(intent, 101);
    }

    private void importNotesToJSON() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("application/json");
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            Uri uri = data.getData();
            readJsonFile(uri);
        }

        if (requestCode == 101) {
            Uri uri = data.getData();
            try {
                ArrayList<NoteExportModel> noteExportModels = databaseHelper.exportNotesToJSON();
                Gson gson = new Gson();
                String jsonString = gson.toJson(noteExportModels);

                writeJsonToFile(uri, jsonString);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeJsonToFile(Uri uri, String jsonString) throws IOException {
        ContentResolver contentResolver = getContentResolver();
        OutputStream outputStream = null;
        try {
            outputStream = contentResolver.openOutputStream(uri);
            outputStream.write(jsonString.getBytes());
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

    private void readJsonFile(Uri uri) {
        try {
            ContentResolver contentResolver = getContentResolver();
            InputStream inputStream = contentResolver.openInputStream(uri);
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();
            String jsonString = stringBuilder.toString();

            Gson gson = new Gson();
            NoteExportModel[] noteData = gson.fromJson(jsonString, NoteExportModel[].class);
            databaseHelper.importFromJSON(noteData);
        } catch (IOException e) {
            e.printStackTrace();
        }
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