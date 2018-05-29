package com.android.kingwong.kingwongproject.activity;

 import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.kingwong.appframework.Activity.BaseActivity;
import com.android.kingwong.appframework.ehttp.EHttp;
import com.android.kingwong.appframework.ehttp.callback.ApiCallback;
import com.android.kingwong.appframework.ehttp.callback.StringCallback;
import com.android.kingwong.appframework.ehttp.model.CommonResult;
import com.android.kingwong.appframework.ehttp.request.HttpRequest;
import com.android.kingwong.appframework.ehttp.request.RequestParams;
import com.android.kingwong.appframework.entity.FileEntity;
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

        disposable = EHttp.postBody(this,"http://api.vd.cn/info/getbonusnotice/",request, new ApiCallback<UserInfo>(CommonResult.class) {
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

    private void postFile(FileEntity mfile){
        File file = new File(mfile.getFile_path());
        HttpRequest request = new HttpRequest.Builder()
                .addUrlParams("", "")
                .addFileParams(mfile.getFile_key(), file.getName(), RequestParams.APPLICATION_OCTET_STREAM, file)
                .build();
        disposable = EHttp.postBody(this, "http://api.vd.cn/info/getbonusnotice/", request, new ApiCallback<FileEntity>(){
            @Override
            public void onStart() {
                super.onStart();

            }
            @Override
            public void onUpProgress(long bytesWritten, long totalSize) {
                super.onUpProgress(bytesWritten, totalSize);

            }
            @Override
            public void onSuccess(FileEntity response) {

            }
            @Override
            public void onFailure(Throwable e) {

            }
        });
    }

}
