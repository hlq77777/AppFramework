package com.android.kingwong.kingwongproject.util;

import com.android.kingwong.appframework.util.ApkUtil;

import java.util.HashMap;
import java.util.Map;

public class HttpUtil {
    private static HttpUtil instance;
    //网络接口地址
    private static final String debug_url = "https://shuang11.as-exchange.com";
    private static final String release_url = "https://app.as-exchange.com";

    public static HttpUtil getInstance(){
        if (null == instance) {
            instance = new HttpUtil();
        }
        return instance;
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

    public Map<String, String> getMap(){
        Map<String, String> map = new HashMap<>();
        return map;
    }
}
