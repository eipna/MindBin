package com.serbi.mindbin.helpers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateHelper {

    public static String getCurrentDetailedDate() {
        String date = new SimpleDateFormat("EEEE, MMMM dd yyyy", Locale.getDefault()).format(new Date());
        return date;
    }

    public static String getCurrentSimpleDate() {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        return date;
    }
}