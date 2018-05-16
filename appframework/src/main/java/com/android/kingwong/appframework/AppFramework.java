package com.android.kingwong.appframework;

import android.content.Context;

public class AppFramework {

    public static Context appContext;//application context,在Application的onCreate中调用AppFramework.init(this)传进来

    /**
     * 使用库时必须在Application的onCreate方法中显式调用AppFramework.init(this);
     */
    public static void init(Context context) {
        if (null != context) {
            appContext = context;
        }
    }

}
