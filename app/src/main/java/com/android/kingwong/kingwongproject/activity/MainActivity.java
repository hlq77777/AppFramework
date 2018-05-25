package com.android.kingwong.kingwongproject.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.kingwong.appframework.Activity.BaseActivity;
import com.android.kingwong.appframework.ehttp.EHttp;
import com.android.kingwong.appframework.ehttp.callback.ApiCallback;
import com.android.kingwong.appframework.ehttp.callback.FileCallBack;
import com.android.kingwong.appframework.ehttp.callback.StringCallback;
import com.android.kingwong.appframework.ehttp.model.CommonResult;
import com.android.kingwong.appframework.ehttp.request.HttpRequest;
import com.android.kingwong.appframework.util.IntentUtil;
import com.android.kingwong.appframework.util.OneClickUtil.AntiShake;
import com.android.kingwong.kingwongproject.R;
import com.android.kingwong.kingwongproject.bean.UserInfo;
import com.android.kingwong.kingwongproject.module.Updata;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;

public class MainActivity extends BaseActivity {

    @BindView(R.id.button_updata)
    Button button_updata;

    Disposable disposable;

    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void OnCreate(Bundle savedInstanceState) {

    }

    @OnClick({R.id.button_updata})
    public void onClick(View view) {
        if (AntiShake.check(view.getId())) {
            //判断是否多次点击
            return;
        }
        switch (view.getId()) {
            case R.id.button_updata:
                IntentUtil.startActivity(this, Updata.class);
                break;
        }
    }

    public void get(){
        String url="http://www.baidu.com";
        EHttp.get(this, url, new StringCallback() {
            @Override
            public void onStart() {
                super.onStart();
                //progressBar.setProgress(0);
            }

            @Override
            public void onFailure(Throwable e) {
                //tvGetStr.setText("get failed:"+e.getMessage());
            }

            @Override
            public void onSuccess(String rusult) {
                //tvGetStr.setText("get sucess:"+ Html.fromHtml(rusult));
            }

        });

    }

    public void post(){
        String json="{\n" +
                "\t\"BaseAppType\": \"android\",\n" +
                "\t\"BaseAppVersion\": \"4.10.1\",\n" +
                "\t\"SystemVersion\": \"7.1.1\",\n" +
                "\t\"_sign_\": \"1D97B4164A6C961AA4B6DBAF4A44DFF9\",\n" +
                "\t\"_token_\": \"062fe4f6dd5148a58d168520bea372f9--00\",\n" +
                "\t\"_wid_\": \"404084422--0\",\n" +
                "\t\"appIdentifier\": \"com.hs.yjseller--0\",\n" +
                "\t\"shop_id\": \"125036171————00\"\n" +
                "}";

        HttpRequest request=new HttpRequest.Builder()
                .addBodyParams(json)
                .build();
        disposable= EHttp.post(this,"http://api.vd.cn/info/getbonusnotice/",request, new ApiCallback<UserInfo>(CommonResult.class) {
            @Override
            public void onFailure(Throwable e) {
                //tvGetStr.setText("post failed:"+e.getMessage());
            }

            @Override
            public void onSuccess(UserInfo response) {
                //tvGetStr.setText("response:\n"+new Gson().toJson(response));

            }
            @Override
            public void onUpProgress(long bytesWritten, long totalSize) {
                super.onUpProgress(bytesWritten, totalSize);
                //progressBar.setProgress((int) (bytesWritten*100/totalSize));

            }

            @Override
            public void onStart() {
                super.onStart();
                //progressBar.setProgress(0);
            }
        });



    }

    private void download(){
        String url="http://180.163.220.71/softdl.360tpcdn.com/auto/20180309/102615199_2f0a7c0426fa87ac8112aff10789ed08.exe";
        EHttp.get(this, url, new FileCallBack(this.getExternalCacheDir().getPath()+"/ehttp","ehttp.apk") {
            @Override
            public void onStart() {
                super.onStart();
                //tvGetStr.setText("开始下载...");
                //progressBar.setProgress(0);
            }

            @Override
            public void onDownProgress(long bytesWritten, long totalSize) {
                //tvGetStr.setText("开始下载"+(bytesWritten*100/totalSize)+"%");
                //progressBar.setProgress((int) (bytesWritten*100/totalSize));
            }

            @Override
            public void onFailure(Throwable e) {
                //tvGetStr.setText("download failed:"+e.getMessage());
            }

            @Override
            public void onSuccess(File rusult) {
                //tvGetStr.setText("download sucess:"+rusult.getAbsolutePath());
            }
        });
    }
}
