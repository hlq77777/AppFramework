package com.android.kingwong.appframework.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import com.android.kingwong.appframework.R;

/**
 * Created by KingWong on 2017/8/25.
 * 带圆角的按钮
 */

public class CustomButtonShape extends AppCompatButton {
    public CustomButtonShape(Context context) {
        super(context);
        init();
    }

    public CustomButtonShape(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomButtonShape(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.setBackground(getResources().getDrawable(R.drawable.shape_custombutton_true));
        this.setTextColor(Color.WHITE);
        if (isEnabled()) {
            this.setBackground(getResources().getDrawable(R.drawable.shape_custombutton_true));
        } else {
            this.setBackground(getResources().getDrawable(R.drawable.shape_custombutton_false));
        }
        this.setFocusable(true);
        this.requestFocusFromTouch();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            this.setBackground(getResources().getDrawable(R.drawable.shape_custombutton_true));
        } else {
            this.setBackground(getResources().getDrawable(R.drawable.shape_custombutton_false));
        }
    }

}
