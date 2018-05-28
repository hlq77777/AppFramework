package com.android.kingwong.kingwongproject.module;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.kingwong.appframework.Activity.BaseActivity;
import com.android.kingwong.appframework.ehttp.EHttp;
import com.android.kingwong.appframework.ehttp.callback.FileCallBack;
import com.android.kingwong.appframework.util.ApkUtil;
import com.android.kingwong.appframework.util.FileUtil;
import com.android.kingwong.appframework.util.LogUtil;
import com.android.kingwong.appframework.util.OneClickUtil.AntiShake;
import com.android.kingwong.appframework.util.ToastUtil;
import com.android.kingwong.kingwongproject.BuildConfig;
import com.android.kingwong.kingwongproject.R;
import com.daimajia.numberprogressbar.NumberProgressBar;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

public class Updata extends BaseActivity{

    @BindView(R.id.progressbar_layout)
    LinearLayout progressbar_layout;
    @BindView(R.id.progressbar)
    NumberProgressBar progressBar;
    @BindView(R.id.ingore_checkBox)
    CheckBox ingore_checkBox;
    @BindView(R.id.tv_upadata_text)
    TextView tv_upadata_text;
    @BindView(R.id.tv_length)
    TextView tv_length;
    @BindView(R.id.btn_updata_ok)
    TextView btn_updata_ok;

    private boolean isstart = false;
    public static final String filedir = Environment.getExternalStorageDirectory() + "/" + ApkUtil.getAppName();
    private String filename = "";
    private String filepath = "";

    @Override
    public int getContentViewId() {
        return R.layout.activity_updata;
    }

    @Override
    public void OnCreate(Bundle savedInstanceState) {
        initView();
    }

    @OnClick({R.id.btn_updata_ok, R.id.btn_updata_cancel})
    public void onClick(View view) {
        if (AntiShake.check(view.getId())) {
            //判断是否多次点击
            return;
        }
        switch (view.getId()) {
            case R.id.btn_updata_ok:
                if(checkWritePermission()){
                    if(FileUtil.fileIsExists(filepath)){
                        startInstall(filepath);
                    }else{
                        if(!isstart){
                            showUpdata();
                        }else{
                            if(progressBar.getProgress() != 100){
                                ToastUtil.getInstance(this).shortToast("正在下载");
                            }
                        }
                    }
                }
                break;
            case R.id.btn_updata_cancel:
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    private void initView(){
        setWindow();
        progressBar.setMax(100);
        String updataStr = "检查到更新,请下载最新版本\n" +
                "当前版本:" + ApkUtil.getVersionName() +
                "\n最新版本:" + ApkUtil.getVersionName() +
                "\n更新内容:\n" + "测试更新";
        tv_upadata_text.setText(updataStr);
        filename = ApkUtil.getAppName() + ".apk";
        filepath = filedir + "/" + filename;
        if(FileUtil.fileIsExists(filepath)){
            tv_length.setText("下载完成");
            btn_updata_ok.setText("立即安装");
            progressBar.setProgress(100);
        }
    }

    //设置窗口的大小及透明度
    private void setWindow(){
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.alpha = 0.9f;
        window.setAttributes(layoutParams);
    }

    //开始-显示更新
    private void showUpdata(){
        isstart = true;
        progressbar_layout.setVisibility(View.VISIBLE);
        btn_updata_ok.setText("正在下载");
        startDownload();
    }

    //开始下载
    private void startDownload(){
        String url = "http://imtt.dd.qq.com/16891/14A4C02997E946B800D87348E5E0CBAA.apk?fsname=com.apicloud.A6977052155909_3.4.4_344.apk&csr=1bbd";
        EHttp.get(this, url, new FileCallBack(filedir, filename) {
            @Override
            public void onStart() {
                super.onStart();
                tv_length.setText("开始下载：");
                progressBar.setProgress(0);
            }

            @Override
            public void onDownProgress(long bytesWritten, long totalSize) {
                tv_length.setText("下载进度:");
                progressBar.setProgress((int) (bytesWritten*100/totalSize));
            }

            @Override
            public void onFailure(Throwable e) {
                LogUtil.e("download failed", e.getMessage());
                FileUtil.deleteFile(filepath);
                tv_length.setText("下载失败:");
                btn_updata_ok.setText("重新下载");
                progressBar.setProgress(0);
                isstart = false;
            }

            @Override
            public void onSuccess(File rusult) {
                tv_length.setText("下载完成");
                btn_updata_ok.setText("立即安装");
                progressBar.setProgress(100);
                isstart = false;
                startInstall(rusult.getAbsolutePath());
            }
        });
    }

    //开始安装
    private void startInstall(String filepath){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //版本在7.0以上是不能直接通过uri访问的
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            File file = (new File(filepath));
            // 由于没有在Activity环境下启动Activity,设置下面的标签
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            Uri apkUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileProvider", file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(new File(filepath)), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        }
        startActivity(intent);
    }
}
