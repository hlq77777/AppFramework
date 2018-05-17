package com.android.kingwong.kingwongproject.activity;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.kingwong.appframework.util.ApkUtil;
import com.android.kingwong.appframework.util.LogUtil;
import com.android.kingwong.appframework.util.StringUtil;
import com.android.kingwong.appframework.util.ToastUtil;
import com.android.kingwong.appframework.view.CustomDialog;
import com.android.kingwong.kingwongproject.MyApplication;
import com.android.kingwong.kingwongproject.R;
import com.android.kingwong.kingwongproject.constant.ConstantValue;
import com.android.kingwong.kingwongproject.response.UpDataResponse;
import com.android.kingwong.kingwongproject.service.UpDataService;
import com.android.kingwong.kingwongproject.util.SharedPreferencesUtil;
import com.daimajia.numberprogressbar.NumberProgressBar;

/**
 * Created by KingWong on 2017/9/19.
 * 自动更新界面
 * //更新-----Action
 * //state
 * 1:开始下载
 * 2:正在下载
 * 3:下载取消
 * 4:下载失败
 * 5:下载成功
 * //进度:length
 * 路径:path
 * errow
 */

public class UpDataActivity extends Activity {
    public static String UpData = "com.android.cwtz.shikedai.receiver.updata";
    //=================更新内容
    private TextView tv_upadata_text;
    private UpDataResponse upData;
    private String path;
    private TextView btn_updata_ok;
    //=========更新中=================
    private NumberProgressBar progressBar;
    private TextView tv_length;
    private boolean isstart = false;
    private CheckBox ingore_checkBox;
    private String ingore = "";
    private LinearLayout progressbar_layout;
    private CustomDialog mRequestDialog;
    public static final int PERMISSION_WRITE_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_updata);
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        //设置窗口的大小及透明度
        layoutParams.width = ViewGroup.LayoutParams.FILL_PARENT;
        layoutParams.height = layoutParams.FILL_PARENT;
        layoutParams.alpha = 0.9f;
        window.setAttributes(layoutParams);
        initView();
        initData();
        MyApplication.getInstance().addUpdataLineActivity(this);
    }

    private void initRequestDialog(){
        mRequestDialog = new CustomDialog(this);
        mRequestDialog.setTitleVisible(true);
        mRequestDialog.setTitle(getResources().getString(R.string.notifyTitle));
        mRequestDialog.setMessage(getResources().getString(R.string.notifyMsgUpdata));
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

    private boolean verifyPermissions(int[] grantResults, String[] permissions) {
        for(int i = 0; i < grantResults.length; i++){
            LogUtil.e("UpDataActivity", "verifyPermissions:" + permissions[i] + "=" + grantResults[i]);
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_updata_cancel:
                    if(ingore.equals("true")){
                        if(ingore_checkBox.isChecked()){
                            if(!StringUtil.getNotNullString(upData.getVersion_name(), "").equals(PreferenceManager.getDefaultSharedPreferences(UpDataActivity.this).getString(ConstantValue.SP_INGORE,""))){
                                SharedPreferencesUtil.saveIngore(UpDataActivity.this, StringUtil.getNotNullString(upData.getVersion_name(), ""));
                            }
                        }
                    }
                    if(upData.getMandatory_status() == 0){
                        finish();
                    }else {
                        MyApplication.getInstance().finishUpdataLineActivities();
                    }
                    break;
                case R.id.btn_updata_ok:
                    if(checkWritePermission()){
                        if(!isstart) {
                            showUpdata();
                        } else {
                            if(progressBar.getProgress() != 100){
                                ToastUtil.getInstance(UpDataActivity.this).shortToast("正在下载");
                            }
                        }
                    }
                    break;
            }
        }
    };

    //开始-显示更新
    private void showUpdata(){
        isstart = true;
        progressbar_layout.setVisibility(View.VISIBLE);
        btn_updata_ok.setText("正在下载");
        Intent intent = new Intent();
        intent.setAction(UpDataService.UPDATA_ACTION);
        intent.setPackage("com.android.cwtz.shikedai");
        intent.putExtra("data", upData);
        startService(intent);
    }
    //更新广播
    private BroadcastReceiver appRegister = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent != null){
                if(intent.getAction() != null){
                    if(intent.getAction().equals(UpData)){
                        checkState(intent);
                    }
                }
            }
        }
    };
    //检查状态
    private void checkState(Intent intent){
        int state=intent.getIntExtra("state", -1);
        switch (state){
            case 1:
                tv_length.setText("开始下载:");
                break;
            case 2:
                float length = intent.getFloatExtra("length", 0);
                int lg = getInit(length);
                tv_length.setText("下载进度:");
                progressBar.setProgress(lg);
                break;
            case 3:
                tv_length.setText("下载取消:");
                break;
            case 4:
                //Not Found  "maybe the file has downloaded completely"
                String errow=intent.getStringExtra("errow");
                LogUtil.e("UpDataActivity", errow);
                if(errow.equals("maybe the file has downloaded completely")){
                    tv_length.setText("下载完成:");
                    progressBar.setProgress(100);
                    btn_updata_ok.setText("立即安装");
                }else if(errow.equals("Not Found")){
                    tv_length.setText("找不到下载地址:");
                    btn_updata_ok.setText("重新下载");
                }else if(errow.equals("安装包下载出错")){
                    tv_length.setText("安装包下载出错:");
                    btn_updata_ok.setText("重新下载");
                }else {
                    tv_length.setText("下载失败:");
                    btn_updata_ok.setText("重新下载");
                }
                isstart=false;
                break;
            case 5:
                tv_length.setText("下载完成");
                btn_updata_ok.setText("立即安装");
                progressBar.setProgress(100);
                break;
        }
    }
    //浮点返回整数
    private int getInit(float length){
        if(length == 0.0){
            return 0;
        }else if(length == 1.0){
            return 100;
        }else{
            String mlength = "" + length;
            int xsd = mlength.indexOf(".");
            String ml = mlength.substring(xsd+1, xsd+3);
            return Integer.parseInt(ml);
        }
    }
    private void initView() {
        upData = (UpDataResponse)getIntent().getSerializableExtra("data");
        path = getIntent().getStringExtra("path");
        ingore = getIntent().getStringExtra("ingore");
        findViewById(R.id.btn_updata_cancel).setOnClickListener(onClickListener);
        findViewById(R.id.btn_updata_ok).setOnClickListener(onClickListener);
        tv_upadata_text = (TextView)findViewById(R.id.tv_upadata_text);
        tv_upadata_text.setText("检查到更新,请下载最新版本\n" +
                "当前版本:" + ApkUtil.getVersionName(UpDataActivity.this) +
                "\n最新版本:" + StringUtil.getNotNullString(upData.getVersion_name(), "") +
                "\n更新内容:\n" + StringUtil.getNotNullString(upData.getVersion_content(), ""));
        progressBar = (NumberProgressBar)findViewById(R.id.progressbar);
        progressBar.setMax(100);
        btn_updata_ok = (TextView)findViewById(R.id.btn_updata_ok);
        ingore_checkBox = (CheckBox)findViewById(R.id.ingore_checkBox);
        tv_length = (TextView)findViewById(R.id.tv_length);
        progressbar_layout = (LinearLayout)findViewById(R.id.progressbar_layout);
        if(upData.getMandatory_status() > 0){
            findViewById(R.id.btn_updata_cancel).setVisibility(View.GONE);
            findViewById(R.id.text_btn_line).setVisibility(View.GONE);
        }else{
            if(ingore.equals("true")){
                ingore_checkBox.setVisibility(View.VISIBLE);
            }
        }
        initRequestDialog();
    }

    private void initData(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UpData);
        registerReceiver(appRegister, intentFilter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(appRegister);
    }

    public boolean checkWritePermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            String[] permissionsNeeded = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this, permissionsNeeded, PERMISSION_WRITE_CODE);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_WRITE_CODE) {
            if (!verifyPermissions(grantResults, permissions)) {
                if(!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    mRequestDialog.show();
                }
            }
        }
    }

}
