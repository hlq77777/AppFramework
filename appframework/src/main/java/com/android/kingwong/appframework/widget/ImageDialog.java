package com.android.kingwong.appframework.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.kingwong.appframework.R;

/**
 * Created by KingWong on 2017/10/25.
 *
 */

public class ImageDialog extends Dialog {

    private ImageView image_advert;
    private LinearLayout layout_outside;

    public ImageDialog(Context context) {
        super(context, R.style.MyProgressDialog);
        this.setContentView(R.layout.dialog_image);
        image_advert = (ImageView) findViewById(R.id.image_advert);
        image_advert.setOnClickListener(onClickListener);
        layout_outside= (LinearLayout) findViewById(R.id.layout_outside);
        layout_outside.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if(id == R.id.image_advert) {
                dismiss();
            }else if(id == R.id.layout_outside) {
                dismiss();
            }
        }
    };

    public void setImage_advert(Drawable drawable){
        image_advert.setImageDrawable(drawable);
    }
}
