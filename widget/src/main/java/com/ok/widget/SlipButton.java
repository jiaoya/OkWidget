package com.ok.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * <pre>
 *      Copyright    : Copyright (c) 2019.
 *      Author       : jiaoya.
 *      Created Time : 2019-11-21.
 *      Desc         : 开关按钮
 * </pre>
 */
public class SlipButton extends View implements View.OnTouchListener {

    private boolean nowChoose = false;// 记录当前按钮是否打开,true为打开,false为关闭
    private boolean onSlip = false;// 记录用户是否在滑动的变量
    private float downX, nowX;// 按下时的x,当前的x,
    private Rect btnOn, btnOff;// 打开和关闭状态下,游标的Rect

    private boolean isChgLsnOn = false;
    private SlipButtonChangeListener slipBtnChangeListener;

    private Bitmap bgOn, bgOff, slipBtn;

    public SlipButton(Context context) {
        super(context);
        init();
    }

    public SlipButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SlipButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // 载入图片资源
        bgOn = BitmapFactory
                .decodeResource(getResources(), R.mipmap.wt_ic_slip_on);
        bgOff = BitmapFactory.decodeResource(getResources(),
                R.mipmap.wt_ic_slip_off);
        slipBtn = BitmapFactory
                .decodeResource(getResources(), R.mipmap.wt_ic_slip_cover);
        // 获得需要的Rect数据
        btnOn = new Rect(0, 0, slipBtn.getWidth(), slipBtn.getHeight());
        btnOff = new Rect(bgOff.getWidth() - slipBtn.getWidth(), 0,
                bgOff.getWidth(), slipBtn.getHeight());
        setOnTouchListener(this);// 设置监听器,也可以直接复写OnTouchEvent
    }

    @Override
    protected void onDraw(Canvas canvas) {// 绘图函数
        super.onDraw(canvas);
        Matrix matrix = new Matrix();
        Paint paint = new Paint();
        float x;
        {
            if (onSlip)// 是否是在滑动状态,
            {
                if (nowX < (bgOn.getWidth() / 2))// 滑动到前半段与后半段的背景不同,在此做判断
                    canvas.drawBitmap(bgOff, matrix, paint);// 画出关闭时的背景
                else
                    canvas.drawBitmap(bgOn, matrix, paint);// 画出打开时的背景

                if (nowX >= bgOn.getWidth())// 是否划出指定范围,不能让游标跑到外头,必须做这个判断
                    x = bgOn.getWidth() - slipBtn.getWidth() / 2;// 减去游标1/2的长度...
                else
                    x = nowX - slipBtn.getWidth() / 2;
            } else {// 非滑动状态
                if (nowChoose)// 根据现在的开关状态设置画游标的位置
                {
                    x = btnOff.left;
                    canvas.drawBitmap(bgOn, matrix, paint);// 画出打开时的背景
                } else {
                    x = btnOn.left;
                    canvas.drawBitmap(bgOff, matrix, paint);// 画出关闭时的背景
                }
            }

            if (x < 0)// 对游标位置进行异常判断...
            {
                x = 0;
            } else if (x > bgOn.getWidth() - slipBtn.getWidth())
                x = bgOn.getWidth() - slipBtn.getWidth();
            canvas.drawBitmap(slipBtn, x, 0, paint);// 画出游标.
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (!isClickable()) {
            return true;
        }
        switch (event.getAction())// 根据动作来执行代码
        {
            case MotionEvent.ACTION_MOVE:// 滑动
                nowX = event.getX();
                break;
            case MotionEvent.ACTION_DOWN:// 按下
                if (event.getX() > bgOn.getWidth()
                        || event.getY() > bgOn.getHeight())
                    return false;
                onSlip = true;
                downX = event.getX();
                nowX = downX;
                break;
            case MotionEvent.ACTION_UP:// 松开
                onSlip = false;
                boolean lastChoose = nowChoose;
                SlipButton.this.measure(0, 0);
                int w = bgOn.getWidth();
                nowChoose = event.getX() >= (w / 2);
                if (isChgLsnOn && (lastChoose != nowChoose))// 如果设置了监听器,就调用其方法..
                    slipBtnChangeListener.onChanged(nowChoose);
                break;
            case MotionEvent.ACTION_CANCEL:
                int m = (btnOff.right + btnOn.left) / 2;
                onSlip = false;
                lastChoose = nowChoose;
                SlipButton.this.measure(0, 0);
                w = bgOn.getWidth();
                nowChoose = event.getX() >= (m);
                if (isChgLsnOn && (lastChoose != nowChoose))// 如果设置了监听器,就调用其方法..
                    slipBtnChangeListener.onChanged(nowChoose);
                break;
            default:
        }
        invalidate();// 重画控件
        return true;
    }

    public boolean isNowChoose() {
        return nowChoose;
    }

    public void setHistoryChosen(boolean historyChosen) {
        nowChoose = historyChosen;
        if (null != slipBtnChangeListener) {
            slipBtnChangeListener.onChanged(nowChoose);
        }
        invalidate();
    }

    /**
     * 设置监听器,当状态修改的时候
     *
     * @param listener
     */
    public void setOnChangedListener(SlipButtonChangeListener listener) {
        isChgLsnOn = true;
        slipBtnChangeListener = listener;
    }

    public interface SlipButtonChangeListener {
        /**
         * @param checkState
         */
        void onChanged(boolean checkState);
    }

}
