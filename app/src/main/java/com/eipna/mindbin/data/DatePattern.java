package com.eipna.mindbin.data;

public enum DatePattern {

    LONG_DAY_NAME("EEEE, MMMM dd yyyy"),
    SHORT_DAY_NAME("EEE, MMMM dd yyyy"),
    MM_DD_YYYY("MM/dd/yyyy"),
    DD_MM_YYYY("dd/MM/yyyy"),
    YYYY_DD_MM("yyyy/dd/MM"),
    YYYY_MM_DD("yyyy/mm/dd");

    public static final DatePattern[] datePatterns;
    public final String value;

    static {
        datePatterns = values();
    }

    DatePattern(String value) {
        this.value = value;
    }
}