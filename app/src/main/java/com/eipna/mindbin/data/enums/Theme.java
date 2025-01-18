package com.eipna.mindbin.data.enums;

public enum Theme {
    LIGHT("Light"),
    DARK("Dark"),
    SYSTEM("System"),
    BATTERY_SAVING("Battery Saving");

    public static final Theme[] themes;
    public final String value;

    static {
        themes = values();
    }

    Theme(String value) {
        this.value = value;
    }
}