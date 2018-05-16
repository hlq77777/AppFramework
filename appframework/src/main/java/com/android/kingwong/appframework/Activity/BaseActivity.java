package com.android.kingwong.appframework.Activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{
    private Unbinder mUnbinder;

    /**
     * 需要进行检测的权限数组
     */
    protected String[] mNeedPermissions = { //这里填你需要申请的权限

    };

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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
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
