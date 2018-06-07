package com.android.kingwong.kingwongproject.util;

import android.content.Context;

import com.android.kingwong.appframework.novate.Novate;
import com.android.kingwong.appframework.util.ApkUtil;
//import com.android.kingwong.novate.Novate;

import java.util.HashMap;
import java.util.Map;

public class HttpUtil {
    private static HttpUtil instance;
    private final Context mContext;
    //网络接口地址
    private static final String debug_url = "https://xxx.com";
    private static final String release_url = "https://xxx.com";

    private Novate novate;

    public static HttpUtil getInstance(Context context){
        if (null == instance) {
            instance = new HttpUtil(context);
        }
        return instance;
    }

    private HttpUtil(Context context){
        this.mContext = context;
    }

    public String getBase_url(){
        String url;
        if (ApkUtil.isRelease()) {
            url = release_url;
        }else{
            url = debug_url;
        }
        return url;
    }

    public String getBase_url(String moudle, String function){
        String url;
        if (ApkUtil.isRelease()) {
            url = release_url;
        }else{
            url = debug_url;
        }
        return url + "/" + moudle + "/" + function;
    }

    public Novate getNovate(){
        novate = new Novate
                .Builder(mContext)
                .baseUrl(getBase_url())
                .addLog(!ApkUtil.isRelease())
                .build();
        return novate;
    }

}
