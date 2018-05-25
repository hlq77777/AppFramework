package com.android.kingwong.kingwongproject.module;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.kingwong.appframework.Activity.BaseActivity;
import com.android.kingwong.appframework.util.OneClickUtil.AntiShake;
import com.android.kingwong.appframework.util.ToastUtil;
import com.android.kingwong.kingwongproject.R;
import com.android.kingwong.kingwongproject.activity.UpDataActivity;
import com.daimajia.numberprogressbar.NumberProgressBar;

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

    @Override
    public int getContentViewId() {
        return R.layout.activity_updata;
    }

    @Override
    public void OnCreate(Bundle savedInstanceState) {
        setWindow();
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
                    if(!isstart) {
                        showUpdata();
                    } else {
                        if(progressBar.getProgress() != 100){
                            ToastUtil.getInstance(this).shortToast("正在下载");
                        }
                    }
                }
                break;
            case R.id.btn_updata_cancel:
                finish();
                break;
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
    }
}
