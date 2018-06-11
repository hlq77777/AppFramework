package com.android.kingwong.kingwongproject.activity;

 import android.os.Bundle;
import android.view.View;

import com.android.kingwong.appframework.Activity.BaseActivity;
import com.android.kingwong.appframework.ehttp.EHttp;
import com.android.kingwong.appframework.ehttp.callback.ApiCallback;
import com.android.kingwong.appframework.ehttp.callback.StringCallback;
import com.android.kingwong.appframework.ehttp.model.CommonResult;
import com.android.kingwong.appframework.ehttp.request.HttpRequest;
import com.android.kingwong.appframework.ehttp.request.RequestParams;
import com.android.kingwong.appframework.entity.FileEntity;
import com.android.kingwong.appframework.util.IntentUtil;
 import com.android.kingwong.appframework.util.LogUtil;
 import com.android.kingwong.appframework.util.OneClickUtil.AntiShake;
import com.android.kingwong.kingwongproject.R;
import com.android.kingwong.kingwongproject.bean.UserInfo;
import com.android.kingwong.kingwongproject.module.Updata;
 import com.android.kingwong.kingwongproject.novate.ExampleActivity;

 import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
 import io.reactivex.Completable;
 import io.reactivex.Maybe;
 import io.reactivex.MaybeEmitter;
 import io.reactivex.MaybeOnSubscribe;
 import io.reactivex.Notification;
 import io.reactivex.Observable;
 import io.reactivex.ObservableEmitter;
 import io.reactivex.ObservableOnSubscribe;
 import io.reactivex.Single;
 import io.reactivex.SingleEmitter;
 import io.reactivex.SingleOnSubscribe;
 import io.reactivex.annotations.NonNull;
 import io.reactivex.disposables.Disposable;
 import io.reactivex.functions.Action;
 import io.reactivex.functions.BiConsumer;
 import io.reactivex.functions.Consumer;

public class MainActivity extends BaseActivity {

    Disposable disposable;

    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void OnCreate(Bundle savedInstanceState) {
        newRxjavaHelloWorld();
        //newRxjavaDo();
        newSingle();
        newCompletable();
        newMaybe();
        newCreate();
    }

    @OnClick({R.id.button_updata, R.id.button_novate})
    public void onClick(View view) {
        if (AntiShake.check(view.getId())) {
            //判断是否多次点击
            return;
        }
        switch (view.getId()) {
            case R.id.button_updata:
                IntentUtil.startActivity(this, Updata.class);
                break;
            case R.id.button_novate:
                IntentUtil.startActivity(this, ExampleActivity.class);
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

        disposable = EHttp.postBody(this,"http://api.vd.cn/info/getbonusnotice/", request,
                new ApiCallback<UserInfo>(CommonResult.class) {
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

    private void newRxjavaHelloWorld(){
        Disposable mDisposable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("Hello World");
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                LogUtil.e("newRxjavaHelloWorld", s);
            }
        });
    }

    private void newRxjavaDo(){
        Disposable mDisposable = Observable.just("Hello World").doOnSubscribe(new Consumer<Disposable>() {
            //订阅之后回调的方法
            @Override
            public void accept(Disposable disposable) throws Exception {
                LogUtil.e("newRxjavaDo", "doOnSubscribe");
            }
        }).doOnLifecycle(new Consumer<Disposable>() {
            //在观察者订阅之后，设置是否取消订阅
            @Override
            public void accept(Disposable disposable) throws Exception {
                LogUtil.e("newRxjavaDo", "doOnLifecycle disposable.isDisposed(): " + disposable.isDisposed());
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                LogUtil.e("newRxjavaDo", "doOnLifecycle run");
            }
        }).doOnNext(new Consumer<String>() {
            //它产生的Observable每发射一项数据就会调用它一次，它的Consumer接收发射的数据项，一般用于在subscriber之前对数据进行处理
            @Override
            public void accept(String s) throws Exception {
                LogUtil.e("newRxjavaDo", "doOnNext:" + s);
            }
        }).doAfterNext(new Consumer<String>() {
            //在onNext之后执行，而doOnNext()是在onNext之前执行
            @Override
            public void accept(String s) throws Exception {
                LogUtil.e("newRxjavaDo", "doAfterNext:" + s);
            }
        }).doOnComplete(new Action() {
            //当它产生的Observable在正常终止调用onComplete时会被调用
            @Override
            public void run() throws Exception {
                LogUtil.e("newRxjavaDo", "doOnComplete");
            }
        }).doFinally(new Action() {
            //当它产生的Observable终止之后会被调用，无论是正常终止还是异常终止。doFinally优先于doAfterTerminate的调用
            @Override
            public void run() throws Exception {
                LogUtil.e("newRxjavaDo", "doFinally");
            }
        }).doAfterTerminate(new Action() {
            //注册一个Action，当Observable调用onComplete或Error时触发
            @Override
            public void run() throws Exception {
                LogUtil.e("newRxjavaDo", "doAfterTerminate");
            }
        }).doOnEach(new Consumer<Notification<String>>() {
            //它产生的Observable每发射一项数据就会调用它一次，不仅包括onNext，还包括onError和onCompleted
            @Override
            public void accept(Notification<String> stringNotification) throws Exception {
                LogUtil.e("newRxjavaDo", "doOnEach: " +
                        (stringNotification.isOnNext() ? "OnNext" : stringNotification.isOnComplete() ? "OnComplete" : "onError"));
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                LogUtil.e("newRxjavaDo", "subscribe: " + s);
            }
        });
    }

    private void newSingle(){
        Disposable mDisposable = Single.create(new SingleOnSubscribe<String>() {
            @Override
            public void subscribe(SingleEmitter<String> e) throws Exception {
                e.onSuccess("Hello Single");
            }
        }).subscribe(new BiConsumer<String, Throwable>() {
            @Override
            public void accept(String s, Throwable throwable) throws Exception {
                LogUtil.e("newSingle", "subscribe: " + s);
            }
        });
    }

    private void newCompletable(){
        Disposable mDisposable = Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                LogUtil.e("newCompletable", "Hello Completable");
            }
        }).subscribe();
    }

    private void newMaybe(){
        Disposable mDisposable = Maybe.create(new MaybeOnSubscribe<String>() {
            @Override
            public void subscribe(MaybeEmitter<String> e) throws Exception {
                e.onSuccess("Hello Maybe");
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                LogUtil.e("newMaybe", "subscribe: " + s);
            }
        });
    }

    private void newCreate(){
        Disposable mDisposable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                try {
                    if(!emitter.isDisposed()){
                        for (int i = 0; i < 3; i++){
                            emitter.onNext(i);
                        }
                        emitter.onComplete();
                    }
                }catch (Exception e){
                    emitter.onError(e);
                }
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                LogUtil.e("newCreate", "Next: " + integer);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                LogUtil.e("newCreate", "Error: " + throwable.getMessage());
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                LogUtil.e("newCreate", "Complete");
            }
        });
    }

}
