package com.eipna.mindbin.data;

import java.util.ArrayList;
import java.util.Objects;

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

    public static String[] toStringArray() {
        String[] stringArray = new String[values().length];
        for (int i = 0; i < stringArray.length; i++) {
            stringArray[i] = values()[i].value;
        }
        return stringArray;
    }
}