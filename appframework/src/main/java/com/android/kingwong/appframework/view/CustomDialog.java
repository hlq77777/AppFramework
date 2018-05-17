package com.android.kingwong.appframework.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.kingwong.appframework.R;
import com.android.kingwong.appframework.view.EditText.ClearEditText;

/**
 * Created by KingWong on 2017/9/4.
 * 统一样式弹窗
 */

public class CustomDialog extends Dialog {

    public TextView dialogBtnLeft;
    public TextView dialogBtnRight;
    private ImageView iconDialogTop;
    private TextView textMessage;
    private TextView textTitle;
    private ClearEditText dialog_code_edt;
    private LinearLayout title_layout;
    private LinearLayout dialog_code_edt_layout;
    private Context context;

    public CustomDialog(Context context) {
        super(context, R.style.MyProgressDialog);
        this.setContentView(R.layout.dialog_custom);
        this.context = context;
        dialogBtnLeft = (TextView) findViewById(R.id.text_btn_off);
        dialogBtnRight = (TextView) findViewById(R.id.text_btn_on);
        iconDialogTop = (ImageView) findViewById(R.id.icon_dialog_top);
        textMessage = (TextView) findViewById(R.id.text_dialog_center);
        textTitle = (TextView) findViewById(R.id.title);
        title_layout = (LinearLayout) findViewById(R.id.title_layout);
        dialog_code_edt = (ClearEditText) findViewById(R.id.dialog_code_edt);
        dialog_code_edt_layout = (LinearLayout) findViewById(R.id.dialog_code_edt_layout);
    }

    public void setIcon(Drawable drawable) {
        iconDialogTop.setImageDrawable(drawable);
    }

    public void setTitle(String message) {
        textTitle.setText(message);
    }

    public void setMessage(String message) {
        textMessage.setText(message);
    }

    public void setBtnLeft(String left) {
        dialogBtnLeft.setText(left);
    }

    public void setBtnRight(String right) {
        dialogBtnRight.setText(right);
    }

    public void show() {
        super.show();
    }

    public void setTitleVisible(boolean b) {
        if (b) {
            title_layout.setVisibility(View.VISIBLE);
        } else {
            title_layout.setVisibility(View.GONE);
        }
    }

    public void setEditVisible(boolean b){
        if (b) {
            textMessage.setVisibility(View.GONE);
            dialog_code_edt_layout.setVisibility(View.VISIBLE);
        } else {
            textMessage.setVisibility(View.VISIBLE);
            dialog_code_edt_layout.setVisibility(View.GONE);
        }
    }

    public void setEditHint(String str){
        dialog_code_edt.setHint(str);
    }

    public void setEdittext(String str){
        dialog_code_edt.setText(str);
    }

    public int getEditLength(){
        return dialog_code_edt.getTextString().trim().length();
    }

    public void setEditError(String error){
        dialog_code_edt.setError(error);
    }

    public String getEdittextString(){
        return dialog_code_edt.getTextString();
    }

    public void setEditType(int type){
        dialog_code_edt.setInputType(type);
    }

    public void setRightVisible(boolean b){
        if (b) {
            dialogBtnLeft.setVisibility(View.VISIBLE);
            dialogBtnRight.setBackground(context.getResources().getDrawable(R.drawable.dialog_button_right));
        }else{
            dialogBtnLeft.setVisibility(View.GONE);
            dialogBtnRight.setBackground(context.getResources().getDrawable(R.drawable.dialog_button_center));
        }
    }

    public void setIconVisible(boolean b) {
        if (b) {
            title_layout.setVisibility(View.VISIBLE);
            iconDialogTop.setVisibility(View.VISIBLE);
            textTitle.setVisibility(View.GONE);
        } else {
            title_layout.setVisibility(View.GONE);
            iconDialogTop.setVisibility(View.GONE);
            textTitle.setVisibility(View.VISIBLE);
        }
    }

}
