package me.maximpestryakov.yamblzweather.util;

import android.content.Context;

import me.maximpestryakov.yamblzweather.App;
import me.maximpestryakov.yamblzweather.R;

public class StringUtil {

    public static String getErrorMessage(Throwable throwable) {
        Context context = App.getContext();
        if (throwable instanceof NoInternetException) {
            return context.getString(R.string.error_no_internet);
        }
        return context.getString(R.string.error_unknown);
    }
}
