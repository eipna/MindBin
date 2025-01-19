package com.eipna.mindbin.data;

public enum DatePattern {

    LONG_DAY_NAME("EEEE, MMMM dd yyyy", "Monday, January 20 2025"),
    SHORT_DAY_NAME("EEE, MMMM dd yyyy", "Mon, January 20 2025"),
    MM_DD_YYYY("MM/dd/yyyy", "01/20/2025"),
    DD_MM_YYYY("dd/MM/yyyy", "20/01/2025"),
    YYYY_DD_MM("yyyy/dd/MM", "2025/20/01"),
    YYYY_MM_DD("yyyy/mm/dd", "2025/01/20");

    public static final DatePattern[] datePatterns;
    public final String value;
    public final String entry;

    static {
        datePatterns = values();
    }

    DatePattern(String value, String entry) {
        this.value = value;
        this.entry = entry;
    }

    public static String[] toStringArray() {
        String[] stringArray = new String[values().length];
        for (int i = 0; i < stringArray.length; i++) {
            stringArray[i] = values()[i].value;
        }
        return stringArray;
    }

    public static String[] toStringArrayEntries() {
        String[] stringArray = new String[values().length];
        for (int i = 0; i < stringArray.length; i++) {
            stringArray[i] = values()[i].entry;
        }
        return stringArray;
    }
}