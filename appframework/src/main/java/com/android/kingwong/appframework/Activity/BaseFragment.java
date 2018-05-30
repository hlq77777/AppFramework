package com.android.kingwong.appframework.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.kingwong.appframework.R;
import com.android.kingwong.appframework.R2;
import com.android.kingwong.appframework.widget.CustomDialog;
import com.android.kingwong.appframework.widget.MultiStateView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by KingWong on 2017/10/18.
 *基类Fragment
 */

public abstract class BaseFragment extends Fragment {

    @BindView(R2.id.multiStateView)
    public MultiStateView multiStateView;

    public static final String TAG = BaseFragment.class.getSimpleName();
    private View mRootView;
    private Unbinder mUnbinder;
    public static final int PERMISSION_PHONE_CODE = 12;
    private CustomDialog mRequestDialog;

    protected String[] mPhonePermissions = {
            Manifest.permission.CALL_PHONE
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(getContentViewId(), container, false);
        initViewAndData();
        OnCreate(savedInstanceState);
        return mRootView;
    }

    private void initViewAndData(){
        initRequestDialog();
        mUnbinder = ButterKnife.bind(this, mRootView);
        if(null != multiStateView){
            multiStateView.setOnMultiStateViewListener(new MultiStateView.onMultiStateViewListener() {
                @Override
                public void reLoad() {
                    loadData();
                }
            });
        }
    }

    public abstract void OnCreate(Bundle savedInstanceState);
    public abstract int getContentViewId();
    public abstract void loadData();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    /**
     * 初始化权限提示信息
     */
    public void initRequestDialog(){
        mRequestDialog = new CustomDialog(getContext());
        mRequestDialog.setTitleVisible(true);
        mRequestDialog.setTitle(getContext().getResources().getString(R.string.notifyTitle));
        mRequestDialog.setMessage(getContext().getResources().getString(R.string.notifyMsg));
        mRequestDialog.setBtnRight(getContext().getResources().getString(R.string.setting));
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
     *显示权限提示dialog
     */
    public void showRequestDialog(String str){
        if(!TextUtils.isEmpty(str)){
            mRequestDialog.setMessage(str);
        }
        mRequestDialog.show();
    }

    public boolean checkPermissions(String... permissions) {
        List<String> needRequestPermissionList = findDeniedPermissions(permissions);
        if (null != needRequestPermissionList && needRequestPermissionList.size() > 0) {
            requestPermissions(needRequestPermissionList.toArray(new String[needRequestPermissionList.size()]),
                    PERMISSION_PHONE_CODE);
            return false;
        }
        return true;
    }

    private List<String> findDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissionList = new ArrayList<String>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            for (String perm : permissions) {
                if (getContext().checkSelfPermission(perm) != PackageManager.PERMISSION_GRANTED
                        || shouldShowRequestPermissionRationale(perm)) {
                    needRequestPermissionList.add(perm);
                }
            }
        }
        return needRequestPermissionList;
    }

    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getContext().getPackageName()));
        startActivity(intent);
    }

    private boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_PHONE_CODE) {
            if(!verifyPermissions(grantResults)){
                if(!shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                    showRequestDialog(getContext().getResources().getString(R.string.notifyMsgPhone));
                }
            }
        }
    }
}
