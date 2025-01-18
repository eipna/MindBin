package com.eipna.mindbin.ui.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.eipna.mindbin.R;
import com.eipna.mindbin.data.enums.Theme;
import com.eipna.mindbin.data.enums.ViewMode;
import com.eipna.mindbin.databinding.ActivitySettingsBinding;
import com.eipna.mindbin.util.SharedPreferenceUtil;
import com.google.android.material.color.DynamicColors;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.shape.MaterialShapeDrawable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SettingsActivity extends BaseActivity {

    private ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Drawable drawable = MaterialShapeDrawable.createWithElevationOverlay(this);
        binding.appBar.setStatusBarForeground(drawable);

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        private SharedPreferenceUtil sharedPreferenceUtil;

        private String listThemeVal;
        private String listViewModeVal;
        private boolean switchDynamicColorsVal;

        private ListPreference listTheme;
        private ListPreference listViewMode;

        private SwitchPreferenceCompat switchDynamicColors;

        private Preference versionPrefs;
        private Preference licensePrefs;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences_main, rootKey);
            setPreferences();

            try {
                PackageManager packageManager = requireActivity().getPackageManager();
                PackageInfo packageInfo = packageManager.getPackageInfo(requireActivity().getPackageName(), 0);
                versionPrefs.setSummary(packageInfo.versionName);
            } catch (PackageManager.NameNotFoundException e) {
                Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            licensePrefs.setOnPreferenceClickListener(preference -> {
                showLicenseDialog();
                return true;
            });

            listViewMode.setValue(listViewModeVal);
            listViewMode.setSummary(listViewModeVal);
            listViewMode.setOnPreferenceChangeListener((preference, newValue) -> {
                String selectedViewMode = (String) newValue;
                if (selectedViewMode.equals(ViewMode.LIST.value)) {
                    sharedPreferenceUtil.setString("view_mode", ViewMode.LIST.value);
                } else if (selectedViewMode.equals(ViewMode.TILES.value)) {
                    sharedPreferenceUtil.setString("view_mode", ViewMode.TILES.value);
                }
                listViewMode.setSummary(selectedViewMode);
                return true;
            });

            listTheme.setValue(listThemeVal);
            listTheme.setSummary(listThemeVal);
            listTheme.setOnPreferenceChangeListener((preference, newValue) -> {
                String selectedTheme = (String) newValue;
                if (selectedTheme.equals(Theme.SYSTEM.value)) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                    sharedPreferenceUtil.setString("theme", Theme.SYSTEM.value);
                } else if (selectedTheme.equals(Theme.LIGHT.value)) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    sharedPreferenceUtil.setString("theme", Theme.LIGHT.value);
                } else if (selectedTheme.equals(Theme.DARK.value)) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    sharedPreferenceUtil.setString("theme", Theme.DARK.value);
                } else if (selectedTheme.equals(Theme.BATTERY_SAVING.value)) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
                    sharedPreferenceUtil.setString("theme", Theme.BATTERY_SAVING.value);
                }
                listTheme.setSummary(selectedTheme);
                return true;
            });

            switchDynamicColors.setEnabled(DynamicColors.isDynamicColorAvailable());
            switchDynamicColors.setChecked(switchDynamicColorsVal);
            switchDynamicColors.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean isChecked = (boolean) newValue;
                sharedPreferenceUtil.setBoolean("dynamic_colors", isChecked);
                requireActivity().recreate();
                return true;
            });
        }

        private void showLicenseDialog() {
            @SuppressLint("UseCompatLoadingForDrawables")
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext())
                    .setTitle(requireContext().getResources().getString(R.string.preference_license))
                    .setMessage(readLicenseFromFile())
                    .setIcon(requireContext().getResources().getDrawable(R.drawable.ic_key, requireContext().getTheme()))
                    .setPositiveButton("Close", null);

            Dialog dialog = builder.create();
            dialog.show();
        }

        private String readLicenseFromFile() {
            StringBuilder stringBuilder = new StringBuilder();
            AssetManager assetManager = requireContext().getAssets();
            try (InputStream inputStream = assetManager.open("license.txt")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
            } catch (IOException e) {
                Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            return stringBuilder.toString();
        }

        private void setPreferences() {
            sharedPreferenceUtil = new SharedPreferenceUtil(requireContext());

            listThemeVal = sharedPreferenceUtil.getString("theme", Theme.SYSTEM.value);
            listViewModeVal = sharedPreferenceUtil.getString("view_mode", ViewMode.LIST.value);
            switchDynamicColorsVal = sharedPreferenceUtil.getBoolean("dynamic_colors", false);

            listTheme = findPreference("theme");
            listViewMode = findPreference("view_mode");
            switchDynamicColors = findPreference("dynamic_colors");

            versionPrefs = findPreference("version");
            licensePrefs = findPreference("license");
        }
    }
}