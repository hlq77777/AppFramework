package com.android.kingwong.appframework.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    private static ToastUtil instance;
    private final Context context;

    public static ToastUtil getInstance(Context context) {
        if (instance == null) {
            instance = new ToastUtil(context);
        }
        return instance;
    }

    private ToastUtil(Context context) {
        this.context = context;
        // super(context);
    }

    public void shortToast(int StringId) {
        Toast.makeText(context, StringId, Toast.LENGTH_SHORT).show();
    }

    public void shortToast(String toastString) {
        Toast.makeText(context, toastString, Toast.LENGTH_SHORT).show();
    }

    public void longToast(String toastString) {
        Toast.makeText(context, toastString, Toast.LENGTH_LONG).show();
    }

    public void longToast(int StringId) {
        Toast.makeText(context, StringId, Toast.LENGTH_LONG).show();
    }
}
