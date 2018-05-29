package com.android.kingwong.appframework.ehttp.callback;

import okhttp3.ResponseBody;

/**
 * Created by KingWong 2018/05/18
 */
public abstract class StringCallback extends HttpCallback<String> {
    @Override
    public String parseResponse(ResponseBody body) throws Exception {
        return body.string();
    }
}
