package com.android.kingwong.kingwongproject;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.pm.PackageManager;

import com.android.kingwong.appframework.AppFramework;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MyApplication extends Application {

    private List<Activity> updataLineList = new LinkedList<Activity>();

    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        //防止重复加载
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        if (processAppName == null || processAppName.equals("")) {
            return;
        }
        AppFramework.init(this);
    }

    public String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {

            }
        }
        return processName;
    }

    // 单例模式中获取唯一的ShiKeDaiApplication实例
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
