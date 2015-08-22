package com.srb.zoomthegrid.utils;

import com.srb.zoomthegrid.global.AppConstants;

public class Logger {

    public static void i(String tag, String string) {
        if (AppConstants.LOG && string != null) {
            android.util.Log.i(tag, string);
        }
    }

    public static void e(String tag, String string) {
        if (AppConstants.LOG && string != null) {
            android.util.Log.e(tag, string);
        }
    }

    public static void e(String tag, String string, Exception e) {
        if (AppConstants.LOG && string != null) {
            android.util.Log.e(tag, string, e);
        }
    }

    public static void d(String tag, String string) {
        if (AppConstants.LOG && string != null) {
            android.util.Log.d(tag, string);
        }
    }

    public static void v(String tag, String string) {
        if (AppConstants.LOG && string != null) {
            android.util.Log.v(tag, string);
        }
    }

    public static void w(String tag, String string) {
        if (AppConstants.LOG && string != null) {
            android.util.Log.w(tag, string);
        }
    }
}