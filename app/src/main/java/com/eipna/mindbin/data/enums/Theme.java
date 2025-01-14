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

    public static String get(Theme theme) {
        for (int i = 0; i < values().length; i++) {
            if (values()[i].equals(theme)) {
                return values()[i].theme;
            }
        }
        return null;
    }
}