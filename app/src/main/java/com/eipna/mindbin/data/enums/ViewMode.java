package com.eipna.mindbin.data.enums;

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
}