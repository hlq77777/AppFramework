package com.android.kingwong.appframework.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.android.kingwong.appframework.R;
import com.android.kingwong.appframework.util.DimensionUtil;

/**
 * Created by KingWong on 2017/8/25.
 * 验证码倒数发送按钮
 */

public class SendCodeButton extends AppCompatTextView {

    private static final int PADDING_NUM = 10;
    private int padding;
    public SendCodeButton(Context context) {
        super(context);
        init(context);
    }

    public SendCodeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SendCodeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        padding = DimensionUtil.getSizeInPixels(PADDING_NUM, context);
        this.setBackground(null);
        if (isEnabled()) {
            this.setTextColor(getResources().getColor(R.color.custom_button_deep));
        } else {
            this.setTextColor(getResources().getColor(R.color.text_color_hint));
        }
        this.setFocusable(true);
        this.requestFocusFromTouch();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.text_color_hint));
        paint.setStrokeWidth(1);
        canvas.drawLine(0,padding,0,getHeight() - padding, paint);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        this.setClickable(enabled);
        this.setFocusable(enabled);
        if (!enabled) {
            this.setTextColor(getResources().getColor(R.color.text_color_hint));
        } else {
            this.setTextColor(getResources().getColor(R.color.custom_button_deep));
        }
    }
}

