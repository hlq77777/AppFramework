package com.android.kingwong.appframework.ehttp.callback;

import okhttp3.ResponseBody;

/**
 * Created by KingWong 2018/05/18
 */
public abstract class HttpCallback<T> {
    /**
     * UI Thread
     */
    public void onStart() {}

    public abstract void onFailure(Throwable e);

    public abstract void onSuccess(T rusult);

    public void onUpProgress(long bytesWritten, long totalSize) {}

    public void onComplete() {}

    /**
     * Thread Pool Thread
     */
    public abstract T parseResponse(ResponseBody body) throws Exception;

}