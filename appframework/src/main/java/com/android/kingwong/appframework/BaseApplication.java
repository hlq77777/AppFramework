package com.android.kingwong.appframework;

import android.app.Application;
import android.text.TextUtils;

import com.android.kingwong.appframework.util.ApkUtil;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //防止重复加载
        if(TextUtils.isEmpty(ApkUtil.getAppProcessName(this)) || !TextUtils.equals(getPackageName(), ApkUtil.getAppProcessName(this))){
            return;
        }
        /**
         * 使用AppFramework库时必须在Application的onCreate方法中显式调用AppFramework.init(this);
         */
        AppFramework.init(this);
    }
}
