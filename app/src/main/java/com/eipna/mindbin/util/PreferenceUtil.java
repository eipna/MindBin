package com.eipna.mindbin.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.eipna.mindbin.data.Contrast;
import com.eipna.mindbin.data.DatePattern;
import com.eipna.mindbin.data.Theme;
import com.eipna.mindbin.data.ViewMode;

public class PreferenceUtil {

    private final SharedPreferences sharedPreferences;
    private static final String PREFERENCE_NAME = "prefs";

    public PreferenceUtil(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public String getContrast() {
        return sharedPreferences.getString("contrast", Contrast.LOW.value);
    }

    public void setContrast(String value) {
        sharedPreferences.edit().putString("contrast", value).apply();
    }

    public String getTheme() {
        return sharedPreferences.getString("theme", Theme.SYSTEM.value);
    }

    public void setTheme(String value) {
        sharedPreferences.edit().putString("theme", value).apply();
    }

    public String getViewMode() {
        return sharedPreferences.getString("view_mode", ViewMode.LIST.value);
    }

    public void setViewMode(String value) {
        sharedPreferences.edit().putString("view_mode", value).apply();
    }

    public boolean isDynamicColorsEnabled() {
        return sharedPreferences.getBoolean("dynamic_colors", false);
    }

    public void setDynamicColors(boolean value) {
        sharedPreferences.edit().putBoolean("dynamic_colors", value).apply();
    }
    public boolean isNoteDateCreatedEnabled() {
        return sharedPreferences.getBoolean("note_date_created", false);
    }

    public void setNoteDateCreated(boolean value) {
        sharedPreferences.edit().putBoolean("note_date_created", value).apply();
    }

    public String getNoteDateCreatedFormat() {
        return sharedPreferences.getString("note_date_created_format", DatePattern.MM_DD_YYYY.value);
    }

    public void setNoteDateCreatedFormat(String value) {
        sharedPreferences.edit().putString("note_date_create_format", value).apply();
    }

    public boolean isNoteLastUpdatedEnabled() {
        return sharedPreferences.getBoolean("note_last_updated", false);
    }

    public void setNoteLastUpdated(boolean value) {
        sharedPreferences.edit().putBoolean("note_last_updated", value).apply();
    }

    public boolean isRoundedNotes() {
        return sharedPreferences.getBoolean("rounded_notes", true);
    }

    public void setRoundedNotes(boolean value) {
        sharedPreferences.edit().putBoolean("rounded_notes", value).apply();
    }

    public int getMaxNoteTitleLines() {
        return sharedPreferences.getInt("note_max_title_lines", 1);
    }

    public void setMaxNoteTitleLines(int value) {
        sharedPreferences.edit().putInt("note_max_title_lines", value).apply();
    }

    public int getMaxNoteContentLines() {
        return sharedPreferences.getInt("note_max_content_lines", 1);
    }

    public void setMaxNoteContentLines(int value) {
        sharedPreferences.edit().putInt("note_max_content_lines", value).apply();
    }
}