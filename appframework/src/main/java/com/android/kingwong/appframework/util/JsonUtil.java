package com.android.kingwong.appframework.util;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

/**
 * Created by KingWong on 2017/9/8.
 * json转换
 */

public class JsonUtil {
    private static JsonUtil instance;
    private final Gson gson;

    public static JsonUtil getInstance() {
        if (instance == null) {
            instance = new JsonUtil();
        }
        return instance;
    }

    private JsonUtil() {
        this.gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    }

    public String getJson(Object object) {
        return this.gson.toJson(object);
    }

    public <T> String getJson(Object object, Class<T> classT) {
        return this.gson.toJson(object, classT);
    }

    public <T> String getJson(List<T> listT, Class<T> classT) {
        if(null != listT && listT.size() > 0){
            Type type = new TypeToken<List<T>>(){}.getType();
            return this.gson.toJson(listT, type);
        }else{
            return "";
        }
    }

    public <T> T getObject(String jsonString, Class<T> classT) {
        T returnObject = null;
        try {
            returnObject = this.gson.fromJson(jsonString, classT);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return returnObject;
    }

    public <T> T getObject(String jsonString, Type type) {
        T returnObject = null;
        try {
            returnObject = this.gson.fromJson(jsonString, type);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return returnObject;
    }
}