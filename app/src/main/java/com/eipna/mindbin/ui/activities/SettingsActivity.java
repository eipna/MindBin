package com.eipna.mindbin.ui.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SeekBarPreference;
import androidx.preference.SwitchPreferenceCompat;

import com.eipna.mindbin.R;
import com.eipna.mindbin.data.Contrast;
import com.eipna.mindbin.data.DatePattern;
import com.eipna.mindbin.data.Library;
import com.eipna.mindbin.data.Theme;
import com.eipna.mindbin.data.ViewMode;
import com.eipna.mindbin.databinding.ActivitySettingsBinding;
import com.eipna.mindbin.util.PreferenceUtil;
import com.eipna.mindbin.util.ThemeUtil;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        private PreferenceUtil preferences;
        private ThemeUtil themeUtil;

        private ListPreference listTheme;
        private ListPreference listViewMode;
        private ListPreference listDateFormat;
        private ListPreference listContrast;

        private SwitchPreferenceCompat switchDynamicColors;
        private SwitchPreferenceCompat switchRoundedCorners;
        private SwitchPreferenceCompat switchShowDateCreated;
        private SwitchPreferenceCompat switchShowLastUpdated;

        private Preference versionPrefs;
        private Preference licensePrefs;
        private Preference thirdPartyLibraries;

        private SeekBarPreference seekBarMaxNoteTitle;
        private SeekBarPreference seekBarMaxNoteContent;

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

            thirdPartyLibraries.setOnPreferenceClickListener(preference -> {
                showLibrariesDialog();
                return true;
            });

            switchShowLastUpdated.setChecked(preferences.isNoteLastUpdatedEnabled());
            switchShowLastUpdated.setOnPreferenceChangeListener((preference, newValue) -> {
                preferences.setNoteLastUpdated((boolean) newValue);
                return true;
            });

            switchShowDateCreated.setChecked(preferences.isNoteDateCreatedEnabled());
            switchShowDateCreated.setOnPreferenceChangeListener((preference, newValue) -> {
                listDateFormat.setVisible((boolean) newValue);
                preferences.setNoteDateCreated((boolean) newValue);
                return true;
            });

            listContrast.setEntries(Contrast.toNameArray());
            listContrast.setEntryValues(Contrast.toValueArray());
            listContrast.setValue(preferences.getContrast());
            listContrast.setSummary(Contrast.getNameFromValue(preferences.getContrast()));
            listContrast.setOnPreferenceChangeListener((preference, newValue) -> {
                preferences.setContrast((String) newValue);
                listContrast.setSummary(Contrast.getNameFromValue(preferences.getContrast()));
                themeUtil.setContrast();
                requireActivity().recreate();
                return true;
            });

            listDateFormat.setVisible(preferences.isNoteDateCreatedEnabled());
            listDateFormat.setEntries(DatePattern.toStringArrayEntries());
            listDateFormat.setEntryValues(DatePattern.toStringArray());
            listDateFormat.setValue(preferences.getNoteDateCreatedFormat());
            listDateFormat.setSummary(preferences.getNoteDateCreatedFormat());
            listDateFormat.setOnPreferenceChangeListener((preference, newValue) -> {
                preferences.setNoteDateCreatedFormat((String) newValue);
                listDateFormat.setSummary((String) newValue);
                return true;
            });

            seekBarMaxNoteTitle.setValue(preferences.getMaxNoteTitleLines());
            seekBarMaxNoteTitle.setOnPreferenceChangeListener((preference, newValue) -> {
                preferences.setMaxNoteTitleLines((int) newValue);
                return true;
            });

            seekBarMaxNoteContent.setValue(preferences.getMaxNoteContentLines());
            seekBarMaxNoteContent.setOnPreferenceChangeListener((preference, newValue) -> {
                preferences.setMaxNoteContentLines((int) newValue);
                return true;
            });

            listViewMode.setValue(preferences.getViewMode());
            listViewMode.setSummary(preferences.getViewMode());
            listViewMode.setEntries(ViewMode.toStringArray());
            listViewMode.setEntryValues(ViewMode.toStringArray());
            listViewMode.setOnPreferenceChangeListener((preference, newValue) -> {
                preferences.setViewMode((String) newValue);
                listViewMode.setSummary((String) newValue);
                return true;
            });

            listTheme.setValue(preferences.getTheme());
            listTheme.setSummary(preferences.getTheme());
            listTheme.setEntries(Theme.toStringArray());
            listTheme.setEntryValues(Theme.toStringArray());
            listTheme.setOnPreferenceChangeListener((preference, newValue) -> {
                preferences.setTheme((String) newValue);
                listTheme.setSummary((String) newValue);
                themeUtil.setTheme();
                return true;
            });

            switchDynamicColors.setEnabled(DynamicColors.isDynamicColorAvailable());
            switchDynamicColors.setChecked(preferences.isDynamicColorsEnabled());
            switchDynamicColors.setOnPreferenceChangeListener((preference, newValue) -> {
                preferences.setDynamicColors((boolean) newValue);
                requireActivity().recreate();
                return true;
            });

            switchRoundedCorners.setChecked(preferences.isRoundedNotes());
            switchRoundedCorners.setOnPreferenceChangeListener((preference, newValue) -> {
                preferences.setRoundedNotes((boolean) newValue);
                return true;
            });
        }

        private void showLibrariesDialog() {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.dialog_third_party_libraries_title)
                    .setPositiveButton(R.string.dialog_button_close, null)
                    .setItems(Library.toStringArrayName(), (dialogInterface, index) -> {
                        String selectedLibrary = Library.toStringArrayURL()[index];
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(selectedLibrary));
                        startActivity(browserIntent);
                    });

            Dialog librariesDialog = builder.create();
            librariesDialog.show();
        }

        private void showLicenseDialog() {
            @SuppressLint("UseCompatLoadingForDrawables")
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext())
                    .setTitle(requireContext().getResources().getString(R.string.preference_license))
                    .setIcon(R.drawable.ic_license)
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
            preferences = new PreferenceUtil(requireContext());
            themeUtil = new ThemeUtil(requireActivity());

            listTheme = findPreference("theme");
            listViewMode = findPreference("view_mode");
            listDateFormat = findPreference("date_format");
            listContrast = findPreference("contrast");
            seekBarMaxNoteTitle = findPreference("max_note_title");
            seekBarMaxNoteContent = findPreference("max_note_content");
            switchDynamicColors = findPreference("dynamic_colors");
            switchRoundedCorners = findPreference("rounded_corners");
            switchShowDateCreated = findPreference("show_date_created");
            switchShowLastUpdated = findPreference("show_last_updated");

            versionPrefs = findPreference("version");
            licensePrefs = findPreference("license");
            thirdPartyLibraries = findPreference("third_party_libraries");
        }
    }
}