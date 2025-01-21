package com.eipna.mindbin.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import androidx.annotation.NonNull;


import java.util.Locale;

public class LocaleUtil {

    public static void setLocale(@NonNull Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();

        configuration.setLocale(locale);
        context.getApplicationContext().createConfigurationContext(configuration);
    }

    public static void resetLocale(@NonNull Context context) {
        Locale defaultLocale = Locale.getDefault();
        setLocale(context, defaultLocale.getLanguage());
    }
}