package com.eipna.mindbin.util;

import android.annotation.SuppressLint;

import com.eipna.mindbin.data.DatePattern;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static String getStringByPattern(DatePattern pattern, long timeStamp) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern.value);
        return simpleDateFormat.format(new Date(timeStamp));
    }
}