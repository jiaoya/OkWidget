package com.ok.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;

/**
 * <pre>
 *      Copyright    : Copyright (c) 2019.
 *      Author       : jiaoya.
 *      Created Time : 2019-11-28.
 *      Desc         : 带清除按钮的EditText
 * </pre>
 */
public class EditTextClose extends AppCompatEditText implements View.OnFocusChangeListener, TextWatcher {

    private AfterTextChangedListener afterTextChangedListener;
    //删除按钮的引用
    private Drawable mClearDrawable;
    //控件是否有焦点
    private boolean hasFoucs;
    private OnFocusChange l;
    private boolean isClearIcon = false;

    public EditTextClose(Context context) {
        super(context);
        init();
    }

    public EditTextClose(Context context, AttributeSet attrs) {
        super(context, attrs, android.R.attr.editTextStyle);
        init();
    }

    public EditTextClose(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        // 获取EditText的DrawableRight,假如没有设置我们就使用默认的图片
        mClearDrawable = getCompoundDrawables()[2];
        if (mClearDrawable == null) {
            // throw new
            // NullPointerException("You can add drawableRight attribute in XML");
            mClearDrawable = getResources().getDrawable(R.mipmap.wt_ic_input_del);
        }
        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(),
                mClearDrawable.getIntrinsicHeight());
        // setCompoundDrawablePadding(4);
        // 默认设置隐藏图标
        setClearIconVisible(false);
        // 设置焦点改变的监听
        setOnFocusChangeListener(this);
        // 设置输入框里面内容发生改变的监听
        addTextChangedListener(this);
    }

    /**
     * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件 当我们按下的位置 在 EditText的宽度 -
     * 图标到控件右边的间距 - 图标的宽度 和 EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向就没有考虑
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {

                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
                        && (event.getX() < ((getWidth() - getPaddingRight())));

                if (touchable) {
                    this.setText("");
                }
            }
        }

        return super.onTouchEvent(event);
    }


    /**
     * 当ClearEditText焦点发生变化的时候，判断里面字符串长度设置清除图标的显示与隐藏
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.hasFoucs = hasFocus;
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }

        if (l != null)
            l.onChange(hasFocus);
    }

    public void setFocusChangeListener(OnFocusChange l) {
        this.l = l;
    }

    public interface OnFocusChange {
        void onChange(boolean hasFocus);
    }

    public void setAfterTextChangedListener(AfterTextChangedListener listener) {
        this.afterTextChangedListener = listener;
    }

    /**
     * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
     *
     * @param visible
     */
    public void setClearIconVisible(boolean visible) {
        if (isClearIcon) {
            setCompoundDrawables(getCompoundDrawables()[0],
                    getCompoundDrawables()[1], null, getCompoundDrawables()[3]);
        } else {
            Drawable right = visible ? mClearDrawable : null;
            setCompoundDrawables(getCompoundDrawables()[0],
                    getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
        }
    }

    public void setClearIcon(boolean isClearIcon) {
        this.isClearIcon = isClearIcon;
    }

    /**
     * 当输入框里面内容发生变化的时候回调的方法
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int count, int after) {
        if (hasFoucs) {
            setClearIconVisible(s.length() > 0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (null != afterTextChangedListener) {
            afterTextChangedListener.handleEditAction(s.toString());
        }
    }

    public TextWatcher getTextWatcher() {
        return this;
    }

    public interface AfterTextChangedListener {
        /**
         * @param s s==null监控焦点事件/监控输入事件
         * @description
         */
        void handleEditAction(String s);
    }

}
