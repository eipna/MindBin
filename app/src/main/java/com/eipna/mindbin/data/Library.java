package com.eipna.mindbin.data;

public enum Library {
    PRETTY_TIME("PrettyTime", "https://www.ocpsoft.org/prettytime/");

    public static final Library[] libraries;
    public final String NAME;
    public final String URL;

    static {
        libraries = values();
    }

    Library(String name, String url) {
        this.NAME = name;
        this.URL = url;
    }

    public static String[] toStringArrayName() {
        String[] stringArray = new String[values().length];
        for (int i = 0; i < values().length; i++) {
            stringArray[i] = values()[i].NAME;
        }
        return stringArray;
    }

    public static String[] toStringArrayURL() {
        String[] stringArray = new String[values().length];
        for (int i = 0; i < values().length; i++) {
            stringArray[i] = values()[i].URL;
        }
        return stringArray;
    }
}