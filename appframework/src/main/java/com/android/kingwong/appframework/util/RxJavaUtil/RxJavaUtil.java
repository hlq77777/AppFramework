package com.android.kingwong.appframework.util.RxJavaUtil;

import android.support.v7.app.AppCompatActivity;

import com.android.kingwong.appframework.util.RxJavaUtil.Lifecycle.RxLifecycle;

import org.reactivestreams.Publisher;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.MaybeTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RxJavaUtil {

    public static ObservableTransformer observableToMain(){
        return new ObservableTransformer(){
            @Override
            public ObservableSource apply(Observable upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static FlowableTransformer flowableToMain(){
        return new FlowableTransformer() {
            @Override
            public Publisher apply(Flowable upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static SingleTransformer singleToMain(){
        return new SingleTransformer() {
            @Override
            public SingleSource apply(Single upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static MaybeTransformer completableToMain(){
        return new MaybeTransformer() {
            @Override
            public MaybeSource apply(Maybe upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 防止重复点击的Transformer
     */
    public static ObservableTransformer preventDuplicateClicksTransformer(){
        return new ObservableTransformer() {
            @Override
            public ObservableSource apply(Observable upstream) {
                return upstream.throttleFirst(1000, TimeUnit.MILLISECONDS);
            }
        };
    }

    /**
     * 防止重复点击的Transformer
     */
    public static ObservableTransformer preventDuplicateClicksTransformer(final long windowDuration, final TimeUnit unit){
        return new ObservableTransformer() {
            @Override
            public ObservableSource apply(Observable upstream) {
                return upstream.throttleFirst(windowDuration, unit);
            }
        };
    }

    /**
     * 对RxView绑定的事件
     * 封装了防止重复点击和RxLifecycle的生命周期
     */
    public static ObservableTransformer useRxViewTransformer(final AppCompatActivity targetActivity) {
        return new ObservableTransformer() {

            @Override
            public ObservableSource apply(Observable upstream) {
                return upstream.compose(preventDuplicateClicksTransformer())
                        .compose(RxLifecycle.bind(targetActivity).toLifecycleTransformer());
            }
        };
    }
}
