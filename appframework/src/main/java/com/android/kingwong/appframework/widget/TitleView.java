package com.android.kingwong.appframework.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.kingwong.appframework.R;
import com.android.kingwong.appframework.util.OneClickUtil.OnMultiClickListener;

/**
 * Created by KingWong on 2017/8/19.
 * 标题栏
 */

public class TitleView extends LinearLayout {
    private ImageButton leftButton;
    private ImageButton rightButton;
    private TextView titleTextView;
    private TextView rightTextView;
    private LinearLayout titleview;
    private OnLeftAndRightClickListener listener;//监听点击事件
    private boolean flag=true;

    //设置监听器
    public void setOnLeftAndRightClickListener(OnLeftAndRightClickListener listener) {
        this.listener = listener;
    }

    public ImageButton getLeftButton(){
        return leftButton;
    }

    public ImageButton getRightButton(){
        return rightButton;
    }

    public TextView getRightTextView(){
        return rightTextView;
    }

    //设置左边按钮的可见性
    public void setLeftButtonVisibility(boolean flag) {
        this.flag = flag;
        if(flag){
            leftButton.setVisibility(VISIBLE);
        }else{
            leftButton.setVisibility(GONE);
        }
    }

    //设置右边按钮的可见性
    public void setRightButtonVisibility(boolean flag) {
        if (flag){
            rightButton.setVisibility(VISIBLE);
            rightTextView.setVisibility(GONE);
        }else{
            rightButton.setVisibility(GONE);
        }
    }

    //更换右侧按钮背景图片
    public void setRightButtonSrc(Drawable drawable){
        rightButton.setBackgroundDrawable(drawable);
    }

    //设置标题名称
    public void setMiddleText(String middleText){
        titleTextView.setText(middleText);
    }

    //获取标题名称
    public String getMiddleText(){
        return titleTextView.getText().toString();
    }

    //设置右边文本的可见性
    public void setRightText(String rightText){
        rightTextView.setText(rightText);
        rightTextView.setTextSize(14);
        if(rightText != null&&!rightText.trim().equals("")){
            rightTextView.setVisibility(VISIBLE);
            rightButton.setVisibility(GONE);
        }else {
            rightTextView.setVisibility(GONE);
        }
    }

    //设置右边文本的可点击性
    public void setRightTextClick(boolean a){
        if(a){
            rightTextView.setClickable(true);
            rightTextView.setTextColor(getResources().getColor(R.color.black));
        }else{
            rightTextView.setClickable(false);
            rightTextView.setTextColor(getResources().getColor(R.color.gray));
        }
    }

    //按钮点击接口
    public interface OnLeftAndRightClickListener {
        public void onLeftButtonClick(ImageButton leftButton);
        public void onRightButtonClick(ImageButton rightButton);
        public void onRightTextViewClick(TextView rightTextView);
    }

    public TitleView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_title, this);
        leftButton = (ImageButton) findViewById(R.id.titleview_back);
        rightButton = (ImageButton) findViewById(R.id.titleview_share);
        titleTextView = (TextView) findViewById(R.id.title);
        rightTextView = (TextView) findViewById(R.id.right_textview);
        titleview = (LinearLayout) findViewById(R.id.titleview);
        leftButton.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (listener != null) {
                    listener.onLeftButtonClick(leftButton);//点击回调
                    return;
                }
                if(!flag)return;
                ((Activity) context).finish();

            }
        });
        rightButton.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (listener != null)
                    listener.onRightButtonClick(rightButton);//点击回调
            }
        });
        rightTextView.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (listener != null)
                    listener.onRightTextViewClick(rightTextView);//点击回调
            }
        });
        //获得自定义属性并赋值
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleView);
        String rightText = typedArray.getString(R.styleable.TitleView_rightText);
        float rightTextSize = typedArray.getDimensionPixelOffset(R.styleable.TitleView_rightTextSize, 14);
        int rightTextColor = typedArray.getColor(R.styleable.TitleView_rightTextColor, 0);
        if(rightTextColor==0){
            rightTextColor = Color.BLACK;
        }
        String titleText = typedArray.getString(R.styleable.TitleView_middleText);
        float titleTextSize = typedArray.getDimension(R.styleable.TitleView_middleTextSize, 20);
        int titleTextColor = typedArray.getColor(R.styleable.TitleView_middleTextColor, 0);
        if(titleTextColor==0){
            titleTextColor = Color.BLACK;
        }
        if (!typedArray.getBoolean(R.styleable.TitleView_leftButtonInVisible, true)) {
            leftButton.setVisibility(GONE);
            leftButton.setEnabled(false);
            leftButton.setFocusable(false);
            leftButton.setFocusableInTouchMode(false);
            leftButton.setClickable(false);
        }
        typedArray.recycle();//释放资源
        rightTextView.setText(rightText);
        if(rightText!=null&&!rightText.trim().equals("")){
            rightTextView.setTextSize(rightTextSize);
            rightTextView.setVisibility(VISIBLE);
            rightButton.setVisibility(GONE);
        }else {
            rightTextView.setVisibility(GONE);
        }
        rightTextView.setTextColor(rightTextColor);
        rightTextView.setTextSize(14);
        titleTextView.setText(titleText);
        if (titleTextSize != 0) {
            titleTextView.setTextSize(titleTextSize);
        }
        if(titleTextColor<=0){
            titleTextColor = Color.BLACK;
        }
        titleTextView.setTextColor(titleTextColor);
//        if (getId() == -1) {
//            setId(R.id.titleView);
//        }
        titleview.setBackgroundColor(getResources().getColor(R.color.white));
    }

}
