package com.eipna.mindbin.data.enums;

public enum Theme {

    LIGHT("Light"),
    DARK("Dark"),
    SYSTEM("System"),
    BATTERY_SAVING("Battery Saving");

    private final String theme;

    Theme(String theme) {
        this.theme = theme;
    }
}