package com.android.kingwong.kingwongproject.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.kingwong.kingwongproject.constant.ConstantValue;

/**
 * Created by KingWong on 2017/9/8.
 *
 */

public class SharedPreferencesUtil {
    public static void saveLoginInfo(Context context, String loginName, String loginPwd, String userId, String api_token) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(ConstantValue.SP_LOGIN_NAME, loginName);
        editor.putString(ConstantValue.SP_LOGIN_PWD, loginPwd);
        editor.putString(ConstantValue.SP_USER_ID, userId);
        editor.putString(ConstantValue.SP_API_TOKEN, api_token);
        editor.apply();
    }

    public static void removeLoginInfo(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(ConstantValue.SP_LOGIN_NAME);
        editor.remove(ConstantValue.SP_LOGIN_PWD);
        editor.remove(ConstantValue.SP_USER_ID);
        editor.remove(ConstantValue.SP_API_TOKEN);
        editor.apply();
    }

    public static void saveFirstKey(Context context, boolean isFirstKey){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(ConstantValue.SP_ISFIRST_KEY, isFirstKey);
        editor.apply();
    }

    public static void saveServicePhone(Context context, String phone){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(ConstantValue.SP_SERVICE_PHONE, phone);
        editor.apply();
    }

    public static void saveUpData(Context context,String path){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(ConstantValue.SP_PATH, path);
        editor.apply();
    }
    public static void saveIngore(Context context,String path){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(ConstantValue.SP_INGORE, path);
        editor.apply();
    }
}
