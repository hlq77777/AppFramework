package com.android.kingwong.appframework.util;

/**
 * Created by KingWong on 2017/8/18.
 * Log信息管理
 */

public class LogUtil {

    public static void v(String tag, String msg) {
        if (!ApkUtil.isRelease())
            android.util.Log.v(tag, msg);
    }

    public static void v(String tag, String msg, Throwable t) {
        if (!ApkUtil.isRelease())
            android.util.Log.v(tag, msg, t);
    }

    public static void d(String tag, String msg) {
        if (!ApkUtil.isRelease())
            android.util.Log.d(tag, msg);
    }

    public static void d(String tag, String msg, Throwable t) {
        if (!ApkUtil.isRelease())
            android.util.Log.d(tag, msg, t);
    }

    public static void i(String tag, String msg) {
        if (!ApkUtil.isRelease())
            android.util.Log.i(tag, msg);
    }

    public static void i(String tag, String msg, Throwable t) {
        if (!ApkUtil.isRelease())
            android.util.Log.i(tag, msg, t);
    }

    public static void w(String tag, String msg) {
        if (!ApkUtil.isRelease())
            android.util.Log.w(tag, msg);
    }

    public static void w(String tag, String msg, Throwable t) {
        if (!ApkUtil.isRelease())
            android.util.Log.w(tag, msg, t);
    }

    public static void e(String tag, String msg) {
        if (!ApkUtil.isRelease())
            android.util.Log.e(tag, msg);
    }

    public static void e(String tag, String msg, Throwable t) {
        if (!ApkUtil.isRelease())
            android.util.Log.e(tag, msg, t);
    }
}

