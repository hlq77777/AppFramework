package com.android.kingwong.appframework.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.android.kingwong.appframework.R;

/**
 * Created by KingWong on 2017/10/9.
 * 分享界面的悬浮窗
 *
 * public void showShareDialog(){
 mSharePopupWindow = new SharePopupWindow(this, itemsShareOnClick);
 mSharePopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
 mSharePopupWindow.showAtLocation(getWindow().getDecorView().findViewById(android.R.id.content), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
 }
 *
 *
 * private View.OnClickListener itemsShareOnClick = new View.OnClickListener() {
 public void onClick(View v) {
 if(checkSharePermission()){
 mSharePopupWindow.dismiss();
 switch (v.getId()) {
 case R.id.btn_share_wx:
 break;
 case R.id.btn_share_pyq:
 break;
 case R.id.btn_share_qq:
 break;
 case R.id.btn_share_wb:
 break;
 }
 }
 }
 };
 *
 */

public class SharePopupWindow extends PopupWindow {

    private LinearLayout btn_share_pyq, btn_share_wx, btn_share_qq,btn_share_wb,btn_share_cancle;
    private View mMenuView;

    public SharePopupWindow(Activity context, View.OnClickListener itemsOnClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.popwin_share_dialog, null);
        btn_share_pyq = (LinearLayout) mMenuView.findViewById(R.id.btn_share_pyq);
        btn_share_wx = (LinearLayout) mMenuView.findViewById(R.id.btn_share_wx);
        btn_share_qq = (LinearLayout) mMenuView.findViewById(R.id.btn_share_qq);
        btn_share_wb = (LinearLayout) mMenuView.findViewById(R.id.btn_share_wb);
        btn_share_cancle = (LinearLayout) mMenuView.findViewById(R.id.btn_share_cancel);
        btn_share_cancle.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dismiss();
            }
        });
        btn_share_pyq.setOnClickListener(itemsOnClick);
        btn_share_wx.setOnClickListener(itemsOnClick);
        btn_share_qq.setOnClickListener(itemsOnClick);
        btn_share_wb.setOnClickListener(itemsOnClick);
        this.setContentView(mMenuView);
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.AnimBottom);
        ColorDrawable dw = new ColorDrawable(Color.TRANSPARENT);
        this.setBackgroundDrawable(dw);
        mMenuView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y=(int) event.getY();
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(y<height){
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

}
