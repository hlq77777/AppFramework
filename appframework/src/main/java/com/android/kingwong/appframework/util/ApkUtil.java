package com.android.kingwong.appframework.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.android.kingwong.appframework.AppFramework;

public class ApkUtil {

    public static String getAppName() {
        return getAppName(AppFramework.appContext);
    }

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getVersionName() {
        return getVersionName(AppFramework.appContext);
    }

    /**
     * [获取应用程序版本名称信息]<BR>
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (NameNotFoundException e) {
            return "";
        }
    }

    public static int getVersionCode() {
        return getVersionCode(AppFramework.appContext);
    }

    /**
     * 获取应用程序版本号
     */
    public static int getVersionCode(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pinfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return pinfo.versionCode;
        } catch (NameNotFoundException e) {

        }
        return -1;
    }

    public static PackageInfo getPackageInfo() {
        return getPackageInfo(AppFramework.appContext);
    }

    public static PackageInfo getPackageInfo(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            return packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getAppProcessName() {
        return getAppProcessName(AppFramework.appContext);
    }

    /**
     * 获取当前进程名称
     */
    public static String getAppProcessName(Context appContext) {
        int pid = android.os.Process.myPid();
        android.app.ActivityManager activityManager = (android.app.ActivityManager) appContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (android.app.ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return "";
    }

    public static String getMetaData(String name) {
        return getMetaData(AppFramework.appContext, name);
    }

    /**
     * 获取AndroidManifest.xml里 的值
     *
     * @param context
     * @param name
     * @return
     */
    public static String getMetaData(Context context, String name) {
        String value = null;
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            value = appInfo.metaData.getString(name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static boolean isRelease() {
        return isRelease(AppFramework.appContext);
    }

    /** 判断是否生产环境
     *
     * 根据版本号格式判断，例如1.2.3.4为测试环境，1.2.3为生产环境
     * */
    public static boolean isRelease(Context context) {
        String versionName = getVersionName(context);
        String[] strArray = versionName.split("\\.");
        return !(strArray.length == 4);
    }

}
