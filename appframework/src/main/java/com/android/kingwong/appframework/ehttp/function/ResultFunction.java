package com.android.kingwong.appframework.ehttp.function;


import com.android.kingwong.appframework.ehttp.callback.HttpCallback;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * Created by KingWong 2018/05/18
 */
public class ResultFunction<R> implements Function<ResponseBody, R> {
    private HttpCallback<R> callback;

    public ResultFunction(HttpCallback<R> callback) {
        this.callback = callback;
    }

    //如果解析数据有错，向上抛出异常，由后面ErrorFunction处理
    @Override
    public R apply(@NonNull ResponseBody body) throws Exception {
        Object result = callback.parseResponse(body);
        return (R) result;
    }
}
