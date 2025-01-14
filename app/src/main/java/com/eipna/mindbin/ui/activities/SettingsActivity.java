package com.eipna.mindbin.ui.activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.eipna.mindbin.R;
import com.eipna.mindbin.data.enums.Theme;
import com.eipna.mindbin.databinding.ActivitySettingsBinding;
import com.eipna.mindbin.util.SharedPreferenceUtil;
import com.google.android.material.color.DynamicColors;
import com.google.android.material.shape.MaterialShapeDrawable;

public class SettingsActivity extends AppCompatActivity {

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
        private boolean switchDynamicColorsVal;

        private ListPreference listTheme;

        private SwitchPreferenceCompat switchDynamicColors;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences_main, rootKey);
            setPreferences();

            listTheme.setValue(listThemeVal);
            listTheme.setSummary(listThemeVal);
            listTheme.setOnPreferenceChangeListener((preference, newValue) -> {
                String selectedTheme = (String) newValue;
                if (selectedTheme.equals(Theme.get(Theme.SYSTEM))) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                    sharedPreferenceUtil.setString("theme", Theme.get(Theme.SYSTEM));
                } else if (selectedTheme.equals(Theme.get(Theme.LIGHT))) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    sharedPreferenceUtil.setString("theme", Theme.get(Theme.LIGHT));
                } else if (selectedTheme.equals(Theme.get(Theme.BATTERY_SAVING))) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
                    sharedPreferenceUtil.setString("theme", Theme.get(Theme.BATTERY_SAVING));
                } else if (selectedTheme.equals(Theme.get(Theme.DARK))) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    sharedPreferenceUtil.setString("theme", Theme.get(Theme.DARK));
                }
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

        private void setPreferences() {
            sharedPreferenceUtil = new SharedPreferenceUtil(requireContext());

            listThemeVal = sharedPreferenceUtil.getString("theme", Theme.get(Theme.SYSTEM));
            switchDynamicColorsVal = sharedPreferenceUtil.getBoolean("dynamic_colors", false);

            listTheme = findPreference("theme");
            switchDynamicColors = findPreference("dynamic_colors");
        }
    }
}