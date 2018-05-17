package com.android.kingwong.appframework.Activity;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

import com.android.kingwong.appframework.R;
import com.android.kingwong.appframework.R2;
import com.android.kingwong.appframework.util.LogUtil;
import com.android.kingwong.appframework.view.CustomDialog;
import com.android.kingwong.appframework.view.MultiStateView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by KingWong on 2017/9/12.
 * 四种状态基类Activity
 */

public abstract class BaseStatusActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

    @BindView(R2.id.multiStateView)
    public MultiStateView multiStateView;

    private Unbinder mUnbinder;
    public CustomDialog mRequestDialog;
    private boolean checkPermission = false;

    /**
     * 需要进行检测的权限数组
     */
    protected String[] mNeedPermissions = { //这里填你需要申请的权限
            //位置
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            //存储卡
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            //手机
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CALL_LOG,
            //相机
            //Manifest.permission.CAMERA,
            //联系人
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.GET_ACCOUNTS,
            //短信
            Manifest.permission.READ_SMS
    };
    private static final int PERMISSION_REQUEST_CODE = 0;

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
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private void initViewAndData(){
        mUnbinder = ButterKnife.bind(this);
        initRequestDialog();
        if(null != multiStateView){
            multiStateView.setOnMultiStateViewListener(new MultiStateView.onMultiStateViewListener() {
                @Override
                public void reLoad() {
                    loadData();
                }
            });
        }
    }

    public abstract int getContentViewId();
    public abstract void OnCreate(Bundle savedInstanceState);
    public abstract void loadData();

    /**
     * 获取当前Activity名称
     */
    public String getRunningActivityName(){
        ActivityManager activityManager=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        String runningActivity=activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
        runningActivity = runningActivity.substring(runningActivity.lastIndexOf(".") + 1);
        return runningActivity;
    }

    /**
     * @param permissions
     */
    public boolean checkPermissions(String... permissions) {
        List<String> needRequestPermissionList = findDeniedPermissions(permissions);
        if (null != needRequestPermissionList && needRequestPermissionList.size() > 0) {
            LogUtil.e(getRunningActivityName(), "checkPermissions");
            ActivityCompat.requestPermissions(this,
                    needRequestPermissionList.toArray(new String[needRequestPermissionList.size()]),
                    PERMISSION_REQUEST_CODE);
            return false;
        }else{
            setCheckPermission(false);
            return true;
        }
    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     */
    private List<String> findDeniedPermissions(String[] permissions) {
        LogUtil.e(getRunningActivityName(), "findDeniedPermissions");
        List<String> needRequestPermissionList = new ArrayList<String>();
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, perm)) {
                needRequestPermissionList.add(perm);
            }
        }
        return needRequestPermissionList;
    }

    /**
     * 检测是否说有的权限都已经授权
     *
     * @param grantResults
     * @return
     */
    private boolean verifyPermissions(int[] grantResults, String[] permissions) {
        for(int i = 0; i < grantResults.length; i++){
            LogUtil.e(getRunningActivityName(), "verifyPermissions:" + permissions[i] + "=" + grantResults[i]);
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 显示提示信息
     *
     */
    private void initRequestDialog(){
        mRequestDialog = new CustomDialog(this);
        mRequestDialog.setTitleVisible(true);
        mRequestDialog.setTitle(getResources().getString(R.string.notifyTitle));
        mRequestDialog.setMessage(getResources().getString(R.string.notifyMsgAll));
        mRequestDialog.setBtnRight(getResources().getString(R.string.setting));
        mRequestDialog.dialogBtnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRequestDialog.dismiss();
            }
        });
        mRequestDialog.dialogBtnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRequestDialog.dismiss();
                startAppSettings();
            }
        });
        mRequestDialog.setCancelable(false);
    }

    /**
     * 启动应用的设置
     *
     */
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (!verifyPermissions(grantResults, permissions)) {
                mRequestDialog.show();
                setCheckPermission(true);
            }else {
                setCheckPermission(false);
            }
        }
    }

    public void setCheckPermission(boolean checkPermission){
        this.checkPermission = checkPermission;
    }

    public boolean getCheckPermission(){
        return checkPermission;
    }

}
