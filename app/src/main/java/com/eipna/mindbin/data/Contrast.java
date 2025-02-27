package com.eipna.mindbin.data;

public enum Contrast {
    LOW("Low", "low_contrast_level"),
    MEDIUM("Medium", "medium_contrast_level"),
    HIGH("High", "high_contrast_level");

    private static final Contrast[] contrasts;
    public final String name;
    public final String value;

    static {
        contrasts = values();
    }

    Contrast(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static String[] toNameArray() {
        String[] strings = new String[contrasts.length];
        for (int i = 0; i < contrasts.length; i++) {
            strings[i] = values()[i].name;
        }
        return strings;
    }

    public static String[] toValueArray() {
        String[] strings = new String[contrasts.length];
        for (int i = 0; i < contrasts.length; i++) {
            strings[i] = values()[i].value;
        }
        return strings;
    }
}