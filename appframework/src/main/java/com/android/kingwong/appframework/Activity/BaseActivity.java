package com.android.kingwong.appframework.Activity;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.android.kingwong.appframework.BuildConfig;
import com.android.kingwong.appframework.R;
import com.android.kingwong.appframework.util.LogUtil;
import com.android.kingwong.appframework.util.StringUtil;
import com.android.kingwong.appframework.view.CustomDialog;
import com.android.kingwong.appframework.view.PicPopupWindow;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

    private Unbinder mUnbinder;
    private InputFilter filter;
    private CustomDialog mRequestDialog;

    //头像选择相关控件
    private PicPopupWindow mPicPopupWindow;
    private static final int PHOTO_GRAPH = 101;
    private static final int PHOTO_ZOOM = 102;
    public static final int PHOTO_RESOULT = 103;
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private static final String IMAGE_KEY = "avatar";
    public static final File CROP_PIC = new File(Environment.getExternalStorageDirectory(), "piccrop.jpg");
    public static final File CAMERA_PIC = new File(Environment.getExternalStorageDirectory(), "piccamera.jpg");

    /**
     * 需要进行检测的权限数组
     */
    protected String[] mNeedPermissions = { //这里填你需要申请的权限
            //位置
            //Manifest.permission.ACCESS_COARSE_LOCATION,
            //Manifest.permission.ACCESS_FINE_LOCATION,
            //存储卡
            //Manifest.permission.WRITE_EXTERNAL_STORAGE,
            //Manifest.permission.READ_EXTERNAL_STORAGE,
            //手机
            //Manifest.permission.CALL_PHONE,
            //Manifest.permission.READ_PHONE_STATE,
            //Manifest.permission.READ_CALL_LOG
            //相机
            //Manifest.permission.CAMERA,
            //联系人
            //Manifest.permission.READ_CONTACTS,
            //Manifest.permission.GET_ACCOUNTS,
            //短信
            //Manifest.permission.READ_SMS
    };
    public static final int PERMISSION_REQUEST_CODE = 0;
    public static final int PERMISSION_CAMERA_CODE = 1;
    public static final int PERMISSION_PHONE_CODE = 2;
    public static final int PERMISSION_SHARE_CODE = 3;
    public static final int PERMISSION_CONTACT_CODE = 4;
    public static final int PERMISSION_WRITE_CODE = 5;

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
    }

    public abstract int getContentViewId();
    public abstract void OnCreate(Bundle savedInstanceState);

    /**
     * 初始化权限提示信息
     */
    private void initRequestDialog(){
        mRequestDialog = new CustomDialog(this);
        mRequestDialog.setTitleVisible(true);
        mRequestDialog.setTitle(getResources().getString(R.string.notifyTitle));
        mRequestDialog.setMessage(getResources().getString(R.string.notifyMsg));
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
     *显示权限提示dialog
     */
    private void showRequestDialog(String str){
        if(!TextUtils.isEmpty(str)){
            mRequestDialog.setMessage(str);
        }
        mRequestDialog.show();
    }

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
     *设置edittext只能输中文
     */
    private void initFilter(){
        filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (!StringUtil.isChinese(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };
    }

    /**
     *收起软键盘
     */
    public void closeEdittext(){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(isSoftShowing()){
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     *判断软键盘是否显示
     */
    private boolean isSoftShowing() {
        //获取当前屏幕内容的高度
        int screenHeight = getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return screenHeight - rect.bottom - getSoftButtonsBarHeight() != 0;
    }

    /**
     * 底部虚拟按键栏的高度
     * @return
     */
    private int getSoftButtonsBarHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        //这个方法获取可能不是真实屏幕的高度
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        //获取当前屏幕的真实高度
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }

    /**
     *显示相机dialog
     */
    public void showPicDialog(){
        if(checkCameraPermission()){
            mPicPopupWindow = new PicPopupWindow(this, itemsPicOnClick);
            mPicPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            mPicPopupWindow.showAtLocation(getWindow().getDecorView().findViewById(android.R.id.content), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
    }

    /**
     *相机dialog点击事件
     */
    //因为switch里的case值必须是常数，而在library module的R文件里ID的值不是final类型的，
    //但是主module的R文件里的ID值是final类型的，所以主module里可以用资源ID作为case值而library module却不能。
    private View.OnClickListener itemsPicOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            mPicPopupWindow.dismiss();
            int id = v.getId();
            if (id == R.id.btn_take_photo) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
                intent.putExtra(MediaStore.EXTRA_OUTPUT, getUriForFile());
                startActivityForResult(intent, PHOTO_GRAPH);
            } else if (id == R.id.btn_pick_photo) {
                Intent i = new Intent(Intent.ACTION_PICK, null);
                i.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
                startActivityForResult(i, PHOTO_ZOOM);
            }
        }

    };

    /**
     *activity返回处理
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_GRAPH) {
            startPhotoZoom(getUriForFile());
        }else if (requestCode == PHOTO_ZOOM) {
            startPhotoZoom(data.getData());
        }else if (requestCode == PHOTO_RESOULT) {
            LogUtil.e("onActivityResult", "剪裁成功");
            try {
                uploadImage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *开启剪裁
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.putExtra("scale", true);
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(CROP_PIC));
        intent.putExtra("return-data", false);//设置为不返回数据
        startActivityForResult(intent, PHOTO_RESOULT);
    }

    /**
     *设置头像后更新到服务端
     */
    private void uploadImage() throws Exception {}

    /**
     *解决Android 7.0之后的Uri安全问题
     */
    private Uri getUriForFile() {
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileProvider", CAMERA_PIC);
        } else {
            uri = Uri.fromFile(CAMERA_PIC);
        }
        return uri;
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
    public boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            LogUtil.e(getRunningActivityName(), result+"");
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     *权限申请返回处理
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {

        }else if(requestCode == PERMISSION_CAMERA_CODE){
            if (!verifyPermissions(grantResults)) {
                if(!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) &&
                        !ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    showRequestDialog(getResources().getString(R.string.notifyMsgCamera));
                }
            }
        }else if(requestCode == PERMISSION_PHONE_CODE){
            if (!verifyPermissions(grantResults)) {
                if(!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
                    showRequestDialog(getResources().getString(R.string.notifyMsgPhone));
                }
            }
        }else if(requestCode == PERMISSION_SHARE_CODE){
            if (!verifyPermissions(grantResults)) {
                if(!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE) &&
                        !ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                        !ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    showRequestDialog(getResources().getString(R.string.notifyMsgShare));
                }
            }
        }else if(requestCode == PERMISSION_CONTACT_CODE){
            if (!verifyPermissions(grantResults)) {
                if(!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE) &&
                        !ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)){
                    showRequestDialog(getResources().getString(R.string.notifyMsgContact));
                }
            }
        }else if (requestCode == PERMISSION_WRITE_CODE) {
            if (!verifyPermissions(grantResults)) {
                if(!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    showRequestDialog(getResources().getString(R.string.notifyMsgUpdata));
                }
            }
        }
    }

    /**
     *集中轮询权限设置
     */
    public boolean checkPermissions(String... permissions) {
        LogUtil.e(getRunningActivityName(), "checkPermissions");
        List<String> needRequestPermissionList = findDeniedPermissions(permissions);
        if (null != needRequestPermissionList && needRequestPermissionList.size() > 0) {
            ActivityCompat.requestPermissions(this,
                    needRequestPermissionList.toArray(new String[needRequestPermissionList.size()]),
                    PERMISSION_REQUEST_CODE);
            return false;
        }
        return true;
    }

    /**
     *检查相机开启权限
     */
    public boolean checkCameraPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            String[] permissionsNeeded = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            List<String> needRequestPermissionList = findDeniedPermissions(permissionsNeeded);
            ActivityCompat.requestPermissions(this,
                    needRequestPermissionList.toArray(new String[needRequestPermissionList.size()]),
                    PERMISSION_CAMERA_CODE);
            return false;
        }
        return true;
    }

    /**
     *检查电话开启权限
     */
    public boolean checkPhonePermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            String[] permissionsNeeded = new String[]{Manifest.permission.CALL_PHONE};
            ActivityCompat.requestPermissions(this, permissionsNeeded, PERMISSION_PHONE_CODE);
            return false;
        }
        return true;
    }

    /**
     *检查分享开启权限
     */
    public boolean checkSharePermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            String[] permissionsNeeded = new String[]{Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_COARSE_LOCATION};
            List<String> needRequestPermissionList = findDeniedPermissions(permissionsNeeded);
            ActivityCompat.requestPermissions(this,
                    needRequestPermissionList.toArray(new String[needRequestPermissionList.size()]),
                    PERMISSION_SHARE_CODE);
            return false;
        }
        return true;
    }

    /**
     *检查通讯录开启权限
     */
    public boolean checkContactPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)) {
            String[] permissionsNeeded = new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CONTACTS};
            List<String> needRequestPermissionList = findDeniedPermissions(permissionsNeeded);
            ActivityCompat.requestPermissions(this,
                    needRequestPermissionList.toArray(new String[needRequestPermissionList.size()]),
                    PERMISSION_CONTACT_CODE);
            return false;
        }
        return true;
    }

    /**
     *检查下载文件开启权限（自动更新）
     */
    public boolean checkWritePermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            String[] permissionsNeeded = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this, permissionsNeeded, PERMISSION_WRITE_CODE);
            return false;
        }
        return true;
    }

    /**
     * 获取application中指定的meta-data
     * @return 如果没有获取成功(没有对应值，或者异常)，则返回值为空
     */
    public String getAppMetaData(String key) {
        if (TextUtils.isEmpty(key)) {
            return "";
        }
        String resultData = "";
        try {
            PackageManager packageManager = getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = String.valueOf(applicationInfo.metaData.get(key));
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return resultData;
    }

}
