package com.android.kingwong.appframework.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import com.android.kingwong.appframework.R;

/**
 * Created by KingWong on 2017/8/22.
 */

public class CustomButton extends AppCompatButton {
    public CustomButton(Context context) {
        super(context);
        init();
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.setBackgroundColor(getResources().getColor(R.color.custom_button_deep));
        this.setTextColor(Color.WHITE);
        if (isEnabled()) {
            this.setBackgroundColor(getResources().getColor(R.color.custom_button_deep));
        } else {
            this.setBackgroundColor(getResources().getColor(R.color.custom_button_gray));
        }
        this.setFocusable(true);
        this.requestFocusFromTouch();
    }


    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            this.setTextColor(Color.WHITE);
            this.setBackgroundColor(getResources().getColor(R.color.custom_button_deep));
        } else {
            this.setTextColor(Color.WHITE);
            this.setBackgroundColor(getResources().getColor(R.color.custom_button_gray));
        }
    }

    public void setButtonColor(boolean enabled){
        if (!enabled) {
            this.setTextColor(Color.WHITE);
            this.setBackgroundColor(getResources().getColor(R.color.custom_button_deep));
        } else {
            this.setTextColor(Color.WHITE);
            this.setBackgroundColor(getResources().getColor(R.color.custom_button_gray));
        }
    }

    public void setButtonBackground(int id){
        this.setBackgroundColor(getResources().getColor(id));
    }

    public void setButtonDrawable(int id){
        this.setBackgroundDrawable(getResources().getDrawable(id));
    }

}

