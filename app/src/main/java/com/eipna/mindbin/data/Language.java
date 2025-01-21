package com.eipna.mindbin.data;

public enum Language {
    DEFAULT("Default", "default"),
    FILIPINO("Filipino", "fil");

    public static final Language[] LANGUAGES;
    public final String name;
    public final String code;

    static {
        LANGUAGES = values();
    }

    Language(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public static String[] toStringArrayName() {
        String[] stringArray = new String[values().length];
        for (int i = 0; i < values().length; i++) {
            stringArray[i] = values()[i].name;
        }
        return stringArray;
    }

    public static String[] toStringArrayCode() {
        String[] stringArray = new String[values().length];
        for (int i = 0; i < values().length; i++) {
            stringArray[i] = values()[i].code;
        }
        return stringArray;
    }
}