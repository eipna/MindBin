package com.eipna.mindbin.util;

import android.annotation.SuppressLint;

import com.eipna.mindbin.data.DatePattern;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static String getStringByPattern(DatePattern pattern, long timeStamp) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern.PATTERN);
        return simpleDateFormat.format(new Date(timeStamp));
    }

    public static String getString(String pattern, long timeStamp) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(new Date(timeStamp));
    }
}