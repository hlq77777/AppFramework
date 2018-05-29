package com.android.kingwong.appframework.ehttp.callback;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import okhttp3.ResponseBody;

/**
 * Created by KingWong 2018/05/18
 */
public abstract class BitmapCallback extends HttpCallback<Bitmap> {
    @Override
    public Bitmap parseResponse(ResponseBody body) throws Exception {
        return BitmapFactory.decodeStream(body.byteStream());
    }
}
