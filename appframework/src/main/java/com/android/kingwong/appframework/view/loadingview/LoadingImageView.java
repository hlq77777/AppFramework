package com.android.kingwong.appframework.view.loadingview;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Created by KingWong on 2016/12/7.
 */
public class LoadingImageView extends AppCompatImageView {
    public LoadingImageView(Context context) {
        super(context);
        init(context);
    }

    public LoadingImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoadingImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        AnimationDrawable an=(AnimationDrawable)getBackground();
        an.start();
    }

}
