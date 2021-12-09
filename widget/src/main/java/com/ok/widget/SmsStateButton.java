package com.ok.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

import com.xier.core.tools.LogUtil;
import com.xier.core.tools.ResourceUtils;

/**
 * <pre>
 *      Copyright    : Copyright (c) 2019.
 *      Author       : jiaoya.
 *      Created Time : 2019-11-28.
 *      Desc         : 发送短信验证码状态按钮
 * </pre>
 */
public class SmsStateButton extends AppCompatButton {

    private TimeCount timeCount;
    private int mCountTime = 60; // 默认倒计时时间

    private String buttonText;  // 按钮文字
    private String enableText;  // 状态文字

    private int defaultTextcolor;     // 开始颜色
    private int mTextColorCountTime = ResourceUtils.getColor(R.color.wt_cccccc);// 倒计时颜色
    /**
     * 记录计数器
     */
    private long millisInFuture;

    public SmsStateButton(Context context) {
        super(context);
        initView();
    }

    public SmsStateButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SmsStateButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SmsStateBtn, defStyleAttr, 0);
        mTextColorCountTime = typedArray.getColor(R.styleable.SmsStateBtn_colorCountDown, Color.parseColor("#c9c9c9"));
        mCountTime = typedArray.getColor(R.styleable.SmsStateBtn_count, 60);
        typedArray.recycle();
        initView();
    }

    private void initView() {
        defaultTextcolor = getDefaultTextColor();
        if (null != getText() && !"".equals(getText())) {
            buttonText = getText().toString();
        } else {
            buttonText = getResources().getString(R.string.wt_get_sms);
            setText(buttonText);
        }
    }

    /**
     * 设置倒计时时间，默认60s
     *
     * @param countTime
     */
    public void setCountTime(int countTime) {
        this.mCountTime = mCountTime;
    }

    /**
     * 获取默认字体颜色
     *
     * @return
     */
    private int getDefaultTextColor() {
        ColorStateList colorStateList = getTextColors();
        return colorStateList.getDefaultColor();
    }

    /**
     * 发送
     */
    public void send() {
        setEnabled(false);
    }

    /**
     * 发送
     */
    public void send(int countTime) {
        this.mCountTime = countTime;
        setEnabled(false);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) { // 可以点击：发送、重新发送、无效验证码
            this.millisInFuture = 0;
            this.setTextColor(defaultTextcolor);
            this.setText(buttonText);
            if (null != timeCount) {
                timeCount.cancel();
                timeCount = null;
            }
        } else { // 不可以点击只有发送验证码中
            if (TextUtils.isEmpty(enableText)) {
                enableText = "已发送(%1$d)";
            }
            this.setTextColor(mTextColorCountTime);
            this.setText(String.format(enableText, mCountTime));

            // 创建计时器，总时间：60秒，间隔时间1秒
            if (0 != millisInFuture) {
                timeCount = new TimeCount(millisInFuture * 1000, 1000);
            } else {
                timeCount = new TimeCount(mCountTime * 1000, 1000);
            }
            timeCount.start();
        }
    }

    @Override
    public void onDetachedFromWindow() {
        // 当按钮销毁时，销毁计时器TimeCount
        LogUtil.d("UIButtonSmsCountDown onDetachedFromWindow");
        if (null != timeCount) {
            timeCount.cancel();
            timeCount = null;
        }
        super.onDetachedFromWindow();
    }

    /**
     * 发送失败
     */
    public void setSmsCodeFailed() {
        this.setEnabled(true);
        CharSequence btnText = this.getText();
        this.setText(btnText);
        if (null != timeCount) {
            timeCount.cancel();
            timeCount = null;
        }
    }


    /**
     * 倒数计时器
     *
     * @author Unknown
     */
    public class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {
            // 计时完毕时触发
            setEnabled(true);
            enableText = "";
            setText(" 重新发送 ");
            setTextColor(defaultTextcolor);
        }

        @Override
        public void onTick(long arg0) {
            // 计时过程显示
            setText(String.format(enableText, arg0 / 1000));
            com.xier.widget.SmsStateButton.this.millisInFuture = arg0 / 1000;
        }
    }

}
