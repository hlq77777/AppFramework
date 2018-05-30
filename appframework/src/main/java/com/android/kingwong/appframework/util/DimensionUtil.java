package com.android.kingwong.appframework.util;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by KingWong on 2017/8/25.
 */

public class DimensionUtil {
    private DimensionUtil() {
        //No instances allowed
    }

    public static int getSizeInPixels(float dp, Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float pixels = metrics.density * dp;
        return (int) (pixels + 0.5f);
    }

    public static float pixelsToSp(Context context, float sp) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return sp * scaledDensity;
    }
    public static int pixels2Dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }
}

