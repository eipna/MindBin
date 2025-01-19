package com.eipna.mindbin.data;

public enum ViewMode {
    LIST("List"),
    TILES("Tiles");

    public static final ViewMode[] viewModes;
    public final String value;

    static {
        viewModes = values();
    }

    ViewMode(String value) {
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