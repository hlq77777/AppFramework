package com.android.kingwong.appframework.Activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by KingWong on 2017/10/18.
 * 基类FragmentActivity
 */

public abstract class BaseFragmentActivity extends FragmentActivity {

    private Unbinder mUnbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getContentViewId());
        initViewAndData();
        OnCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    private void initViewAndData(){
        mUnbinder = ButterKnife.bind(this);
    }

    public abstract int getContentViewId();
    public abstract void OnCreate(Bundle savedInstanceState);

    /**
     * 获取当前Activity名称
     */
    public String getRunningActivityName(){
        ActivityManager activityManager=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        String runningActivity=activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
        runningActivity = runningActivity.substring(runningActivity.lastIndexOf(".") + 1);
        return runningActivity;
    }
}
