package com.android.kingwong.kingwongproject;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.android.kingwong.appframework.AppFramework;
import com.android.kingwong.appframework.BaseApplication;
import com.android.kingwong.appframework.util.ApkUtil;
import com.android.kingwong.appframework.util.LogUtil;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MyApplication extends BaseApplication {

    private List<Activity> updataLineList = new LinkedList<Activity>();

    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    // 单例模式中获取唯一的Application实例
    public static MyApplication getInstance() {
        if (null == instance) {
            instance = new MyApplication();
        }
        return instance;
    }

    //添加自动更新Activity到容器中
    public void addUpdataLineActivity(Activity activity) {
        updataLineList.add(activity);
    }

    // 遍历自动更新Activity并finish
    public void finishUpdataLineActivities() {
        for (Activity activity : updataLineList) {
            activity.finish();
        }
    }
}
