package com.android.kingwong.kingwongproject;

import android.app.Application;

import com.android.kingwong.appframework.AppFramework;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppFramework.init(this);
    }
}
