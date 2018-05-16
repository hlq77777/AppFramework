package com.android.kingwong.appframework.view.EditText;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

import com.android.kingwong.appframework.R;
import com.android.kingwong.appframework.util.StringUtil;
import com.android.kingwong.appframework.util.ToastUtil;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

/**
 * Created by KingWong on 2017/8/23.
 * 输入文本框 右边有自带的删除按钮 当有输入时，显示删除按钮，当无输入时，不显示删除按钮。
 *
 * <com.android.kingwong.appframework.view.EditText.ClearEditText
 *      android:id="@+id/information_account_name_edt"
 *      android:layout_width="match_parent"
 *      android:layout_height="match_parent"
 *      android:layout_marginLeft="15dp"
 *      android:gravity="center_vertical"
 *      android:hint="@string/information_account_name_hint"
 *      android:inputType="textPersonName"
 *      android:maxLines="1"
 *      android:textColor="@color/text_color_text"
 *      android:textColorHint="@color/text_color_hint"
 *      android:textSize="@dimen/custom_text_size" />
 *
 */

public class ClearEditText extends AppCompatEditText implements View.OnFocusChangeListener, TextWatcher {
    /**
     * 删除按钮的引用
     */
    private Drawable mClearDrawable;
    /**
     * 控件是否有焦点
     */
    private boolean hasFoucs;
    private Context context;
    private static final int drawblePadding = 5;
    private boolean enabled = true;

    public ClearEditText(Context context) {
        this(context, null);
        this.context = context;
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        // 这里构造方法也很重要，不加这个很多属性不能再XML里面定义
        super(context, attrs);
        this.context = context;
        init();
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }

    private void init() {
        if (Build.VERSION.SDK_INT == 21) {
            this.setPadding(10, 5, 15, 0);
        }else{
            this.setPadding(getPaddingLeft(),getPaddingTop(),15,getPaddingBottom());
        }
        // 获取ClearEditText的DrawableRight,假如没有设置我们就使用默认的图片
        mClearDrawable = getCompoundDrawables()[2];
        if (mClearDrawable == null) {
            // throw new
            // NullPointerException("You can add drawableRight attribute in XML");
            mClearDrawable = getResources().getDrawable(R.drawable.edittext_del);
        }
        mClearDrawable.setBounds(drawblePadding, 0, mClearDrawable.getIntrinsicWidth() + drawblePadding, mClearDrawable.getIntrinsicHeight());
//        if(getBackground()==null){
        setBackground(null);
//        }
        // 默认设置隐藏图标
        setClearIconVisible(false);
        // 设置焦点改变的监听
        setOnFocusChangeListener(this);
        // 设置输入框里面内容发生改变的监听
        addTextChangedListener(this);
    }

    /**
     * 因为我们不能直接给ClearEditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件 当我们按下的位置 在 ClearEditText的宽度 -
     * 图标到控件右边的间距 - 图标的宽度 和 ClearEditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向就没有考虑
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!enabled){
            setClearIconVisible(false);
            return false;
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {
                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight()) && (event.getX() < ((getWidth() - getPaddingRight())));
                if (touchable) {
                    this.setText("");
                }
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 当ClearClearEditText焦点发生变化的时候，判断里面字符串长度设置清除图标的显示与隐藏
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.hasFoucs = hasFocus;
        if(!enabled){
            setClearIconVisible(false);
            return;
        }
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
    }

    /**
     * 设置清除图标的显示与隐藏，调用setCompoundDrawables为ClearEditText绘制上去
     *
     * @param visible
     */
    public void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    /**
     * 当输入框里面内容发生变化的时候回调的方法
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int count, int after) {
        if(!enabled){
            setClearIconVisible(false);
            return;
        }
        if (hasFoucs) {
            setClearIconVisible(s.length() > 0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        this.enabled =enabled;
    }

    public void setError(String errorMsg){
        YoYo.with(Techniques.Shake).playOn(this);
        ToastUtil.getInstance(context).shortToast(errorMsg);
    }

    public boolean isTextNull(){
        if(this.getTextString().trim().equals("")){
            return true;
        }
        return false;
    }

    public String getTextString(){
        return StringUtil.replaceBlank(this.getText().toString());
    }

    public int getTextStringLenght(){
        return getTextString().length();
    }

    public void setHintSize(String str, int size){
        // 新建一个可以添加属性的文本对象
        SpannableString ss = new SpannableString(str);
        // 新建一个属性对象,设置文字的大小
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(size,true);
        // 附加属性到文本
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置hint
        this.setHint(new SpannedString(ss)); // 一定要进行转换,否则属性会消失
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        InputConnection inputConnection = super.onCreateInputConnection(outAttrs);
        if(inputConnection != null){
            outAttrs.imeOptions &= ~EditorInfo.IME_FLAG_NO_ENTER_ACTION;
        }
        return inputConnection;
    }

}
