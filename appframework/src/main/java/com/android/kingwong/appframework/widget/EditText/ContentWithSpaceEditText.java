package com.android.kingwong.appframework.widget.EditText;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

import com.android.kingwong.appframework.R;
import com.android.kingwong.appframework.util.ToastUtil;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

/**
 * Created by KingWong on 2017/9/18.
 * 1.实现分割输入内容
 * 2.该控件支持类型分别为电话号码(000 0000 0000)、银行卡号(0000 0000 0000 0000 000)和身份证号(000000 0000 0000 0000)。
 *
 * <com.android.kingwong.appframework.view.EditText.ContentWithSpaceEditText
 *      android:id="@+id/information_account_num_edt"
 *      android:layout_width="match_parent"
 *      android:layout_height="match_parent"
 *      android:layout_marginLeft="15dp"
 *      android:gravity="center_vertical"
 *      android:hint="@string/information_account_num_hint"
 *      android:inputType="number"
 *      android:textColor="@color/text_color_text"
 *      android:textColorHint="@color/text_color_hint"
 *      android:textSize="@dimen/custom_text_size"
 *      app:type="IDCard" />
 */

public class ContentWithSpaceEditText extends AppCompatEditText implements View.OnFocusChangeListener{

    public static final int TYPE_PHONE = 0;
    public static final int TYPE_CARD = 1;
    public static final int TYPE_IDCARD = 2;
    private int maxLength = 100;
    private int contentType;
    private int start, count, before;
    private String digits;
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

    public ContentWithSpaceEditText(Context context) {
        this(context, null);
        this.context = context;
    }

    public ContentWithSpaceEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        parseAttributeSet(context, attrs);
    }

    public ContentWithSpaceEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        parseAttributeSet(context, attrs);
    }

    private void parseAttributeSet(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ContentWithSpaceEditText, 0, 0);
        contentType = ta.getInt(R.styleable.ContentWithSpaceEditText_type, 0);
        ta.recycle();
        initType();
        setSingleLine();
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
        addTextChangedListener(watcher);
        // 设置焦点改变的监听
        setOnFocusChangeListener(this);
    }

    private void initType() {
        if (contentType == TYPE_PHONE) {
            maxLength = 13;
            digits = "0123456789 ";
            setInputType(InputType.TYPE_CLASS_NUMBER);
        } else if (contentType == TYPE_CARD) {
            maxLength = 23;
            digits = "0123456789 ";
            setInputType(InputType.TYPE_CLASS_NUMBER);
        } else if (contentType == TYPE_IDCARD) {
            maxLength = 21;
            digits = "0123456789xX ";
            setInputType(InputType.TYPE_CLASS_TEXT);
        }
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
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
     * 设置清除图标的显示与隐藏，调用setCompoundDrawables为ClearEditText绘制上去
     *
     * @param visible
     */
    public void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }


    @Override
    public void setInputType(int type) {
        super.setInputType(type);
        // setKeyListener要在setInputType后面调用，否则无效
        if (!TextUtils.isEmpty(digits)) {
            setKeyListener(DigitsKeyListener.getInstance(digits));
        }
    }

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            ContentWithSpaceEditText.this.start = start;
            ContentWithSpaceEditText.this.before = before;
            ContentWithSpaceEditText.this.count = count;
            if(!enabled){
                setClearIconVisible(false);
                return;
            }
            if (hasFoucs) {
                setClearIconVisible(s.length() > 0);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s == null) {
                return;
            }
            //判断是否是在中间输入，需要重新计算
            boolean isMiddle = (start + count) < (s.length());
            //在末尾输入时，是否需要加入空格
            boolean isNeedSpace = false;
            if (!isMiddle && isSpace(s.length())) {
                isNeedSpace = true;
            }
            if (isMiddle || isNeedSpace || count > 1) {
                String newStr = s.toString();
                newStr = newStr.replace(" ", "");
                StringBuilder sb = new StringBuilder();
                int spaceCount = 0;
                for (int i = 0; i < newStr.length(); i++) {
                    sb.append(newStr.substring(i, i + 1));
                    //如果当前输入的字符下一位为空格(i+1+1+spaceCount)，因为i是从0开始计算的，所以一开始的时候需要先加1
                    if (isSpace(i + 2 + spaceCount)) {
                        sb.append(" ");
                        spaceCount += 1;
                    }
                }
                removeTextChangedListener(watcher);
                s.replace(0, s.length(), sb);
                //如果是在末尾的话,或者加入的字符个数大于零的话（输入或者粘贴）
                if (!isMiddle || count > 1) {
                    setSelection(s.length() <= maxLength ? s.length() : maxLength);
                } else if (isMiddle) {
                    //如果是删除
                    if (count == 0) {
                        //如果删除时，光标停留在空格的前面，光标则要往前移一位
                        if (isSpace(start - before + 1)) {
                            setSelection((start - before) > 0 ? start - before : 0);
                        } else {
                            setSelection((start - before + 1) > s.length() ? s.length() : (start - before + 1));
                        }
                    }
                    //如果是增加
                    else {
                        if (isSpace(start - before + count)) {
                            setSelection((start + count - before + 1) < s.length() ? (start + count - before + 1) : s.length());
                        } else {
                            setSelection(start + count - before);
                        }
                    }
                }
                addTextChangedListener(watcher);
            }
        }
    };

    private boolean isSpace(int length) {
        if (contentType == TYPE_PHONE) {
            return isSpacePhone(length);
        } else if (contentType == TYPE_CARD) {
            return isSpaceCard(length);
        } else if (contentType == TYPE_IDCARD) {
            return isSpaceIDCard(length);
        }
        return false;
    }

    private boolean isSpacePhone(int length) {
        return length >= 4 && (length == 4 || (length + 1) % 5 == 0);
    }

    private boolean isSpaceCard(int length) {
        return length % 5 == 0;
    }

    private boolean isSpaceIDCard(int length) {
        return length > 6 && (length == 7 || (length - 2) % 5 == 0);
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
        return this.getText().toString().replace(" ", "");
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
