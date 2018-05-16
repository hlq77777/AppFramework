package com.android.kingwong.appframework.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.io.Serializable;
import java.util.List;

/**
 * Created by KingWong on 2017/8/18.
 */

public class IntentUtil {
    public static String BUNDLE_KEY = "bundle";

    public static void startActivity(Context context, Class cls) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        context.startActivity(intent);
    }

    public static void startActivityAndFinish(Context context, Class cls) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    public static void startActivity(Context context, Class cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.putExtra(BUNDLE_KEY, bundle);
        context.startActivity(intent);
    }
    public static void startActivityAndFinish(Context context, Class cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.putExtra(BUNDLE_KEY, bundle);
        context.startActivity(intent);
        ((Activity) context).finish();
    }
    public static void startActivity(Context context, Class cls, String key, Object value) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        Bundle bundle = new Bundle();
        if (value.getClass() == String.class) {
            bundle.putString(key, String.valueOf(value));
        } else if (value.getClass() == Integer.class) {
            bundle.putInt(key, (Integer) value);
        } else if (value.getClass() == Boolean.class) {
            bundle.putBoolean(key, (Boolean) value);
        }else {
            bundle.putSerializable(key, (Serializable) value);
        }
        intent.putExtra(BUNDLE_KEY, bundle);
        context.startActivity(intent);
    }

    public static void startActivityAndFinish(Context context, Class cls, String key, Object value) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        Bundle bundle = new Bundle();
        if (value.getClass() == String.class) {
            bundle.putString(key, String.valueOf(value));
        } else if (value.getClass() == Integer.class) {
            bundle.putInt(key, (Integer) value);
        } else if (value.getClass() == Boolean.class) {
            bundle.putBoolean(key, (Boolean) value);
        }else {
            bundle.putSerializable(key, (Serializable) value);
        }
        intent.putExtra(BUNDLE_KEY, bundle);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    public static Bundle getIntentBundle(Activity activity) {
        return activity.getIntent().getBundleExtra(BUNDLE_KEY);
    }

    public static boolean getIntentBoolean(Activity activity, String key) {
        if (activity.getIntent().getBundleExtra(BUNDLE_KEY) != null)
            return activity.getIntent().getBundleExtra(BUNDLE_KEY).getBoolean(key, false);
        return false;
    }
    public static Serializable getIntentSerializable(Activity activity, String key) {
        if (activity.getIntent().getBundleExtra(BUNDLE_KEY) == null)
            return null;
        return activity.getIntent().getBundleExtra(BUNDLE_KEY).getSerializable(key);
    }

    public static String getIntentString(Activity activity, String key) {
        if (activity.getIntent().getBundleExtra(BUNDLE_KEY) == null)
            return null;
        return activity.getIntent().getBundleExtra(BUNDLE_KEY).getString(key);
    }

    public static int getIntentInt(Activity activity, String key) {
        return activity.getIntent().getBundleExtra(BUNDLE_KEY).getInt(key);
    }
    public static long getIntentLong(Activity activity, String key) {
        return activity.getIntent().getBundleExtra(BUNDLE_KEY).getLong(key);
    }
    public static double getIntentDouble(Activity activity, String key) {
        return activity.getIntent().getBundleExtra(BUNDLE_KEY).getDouble(key);
    }
    public static void startActivityAndFinishLine(Context context, Class cls) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    public static void startActivityAndFinishLine(Context context, Class cls, String key, String value) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        Bundle bundle = new Bundle();
        bundle.putString(key, value);
        intent.putExtra(BUNDLE_KEY, bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    /**
     * 判断应用是否已经启动
     * @param context 一个context
     * @param packageName 要判断应用的包名
     * @return boolean
     */
    public static boolean isAppAlive(Context context, String packageName){
        ActivityManager activityManager =
                (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos
                = activityManager.getRunningAppProcesses();
        for(int i = 0; i < processInfos.size(); i++){
            if(processInfos.get(i).processName.equals(packageName)){
                LogUtil.e("NotificationLaunch", String.format("the %s is running, isAppAlive return true", packageName));
                return true;
            }
        }
        LogUtil.e("NotificationLaunch", String.format("the %s is not running, isAppAlive return false", packageName));
        return false;
    }
}
