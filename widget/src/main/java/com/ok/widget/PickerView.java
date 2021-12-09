package com.ok.widget;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.ContextCompat;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * <pre>
 *      Copyright    : Copyright (c) 2019.
 *      Author       : jiaoya.
 *      Created Time : 2019-11-15.
 *      Desc         : 标签/view 横条选择控件，滚动效果，中间放大，上下缩小
 * </pre>
 */
public class PickerView extends View {

    private Context mContext;

    private Paint mPaint;    // 画笔
    private int mLightColor; // 选中的颜色
    private int mDarkColor;  // 非选中的颜色
    private float mHalfWidth, mHalfHeight, mQuarterHeight;
    private float mMinTextSize; // 最小字体
    private float mTextSizeRange; // 最大最小字体差值/幅度
    private float mTextSpacing, mHalfTextSpacing;

    private float mScrollDistance;// 移动距离
    private float mLastTouchY; // 手指点击y坐标
    private List<String> mDataList = new ArrayList<>();
    private int mSelectedIndex;
    private boolean mCanScroll = true;
    private boolean mCanScrollLoop = true;
    private OnSelectListener mOnSelectListener;
    private ObjectAnimator mScrollAnim;
    private boolean mCanShowAnim = true;

    private Timer mTimer = new Timer();
    private TimerTask mTimerTask;
    private Handler mHandler = new ScrollHandler(this);

    /**
     * 自动回滚到中间的速度
     */
    private static final float AUTO_SCROLL_SPEED = 10;

    /**
     * 透明度：最小 120，最大 255，极差 135
     */
    private static final int TEXT_ALPHA_MIN = 120;
    private static final int TEXT_ALPHA_RANGE = 135;

    /**
     * 选择结果回调接口
     */
    public interface OnSelectListener {
        void onSelect(View view, String selected);
    }

    private static class ScrollTimerTask extends TimerTask {
        private WeakReference<Handler> mWeakHandler;

        private ScrollTimerTask(Handler handler) {
            mWeakHandler = new WeakReference<>(handler);
        }

        @Override
        public void run() {
            Handler handler = mWeakHandler.get();
            if (handler == null) return;

            handler.sendEmptyMessage(0);
        }
    }

    private static class ScrollHandler extends Handler {
        private WeakReference<PickerView> mWeakView;

        private ScrollHandler(com.xier.widget.PickerView view) {
            mWeakView = new WeakReference<>(view);
        }

        @Override
        public void handleMessage(Message msg) {
            com.xier.widget.PickerView view = mWeakView.get();
            if (view == null) {
                return;
            }
            view.keepScrolling();
        }
    }

    public PickerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Style.FILL);
        mPaint.setTextAlign(Align.CENTER);
        mLightColor = ContextCompat.getColor(mContext, R.color.wt_date_picker_text_light);
        mDarkColor = ContextCompat.getColor(mContext, R.color.wt_date_picker_text_dark);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mHalfWidth = getMeasuredWidth() / 2f;
        int height = getMeasuredHeight();
        mHalfHeight = height / 2f;
        mQuarterHeight = height / 4f;

        // 按照View的高度计算字体大小
        float maxTextSize = height / 7f;             // 整个高度的七分之一
        mMinTextSize = maxTextSize / 2.2f;           // 最小字体大小
        mTextSizeRange = (float) (maxTextSize - mMinTextSize * 1.5); // 计算字体幅度
        mTextSpacing = mMinTextSize * 3f;          // 字体行距
        mHalfTextSpacing = mTextSpacing / 2f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mSelectedIndex >= mDataList.size()) {
            return;
        }

        // 绘制选中上方的 text
        for (int i = 1; i <= mSelectedIndex; i++) {
            drawText(canvas, mDarkColor, mScrollDistance - i * mTextSpacing,
                    mDataList.get(mSelectedIndex - i));
        }

        // 绘制选中的 text
        drawText(canvas, mLightColor, mScrollDistance, mDataList.get(mSelectedIndex));

        // 绘制选中下方的 text
        int size = mDataList.size() - mSelectedIndex;
        for (int i = 1; i < size; i++) {
            drawText(canvas, mDarkColor, mScrollDistance + i * mTextSpacing,
                    mDataList.get(mSelectedIndex + i));
        }
    }

    private void drawText(Canvas canvas, int textColor, float offsetY, String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }

        // 比例
        float scale = parabola(offsetY / mQuarterHeight, 2);

        // 设置字体大小
        mPaint.setTextSize(mMinTextSize + mTextSizeRange * scale);
        // 设置颜色
        mPaint.setColor(textColor);
        // 设置透明度1-255的某个值
        mPaint.setAlpha(TEXT_ALPHA_MIN + (int) (TEXT_ALPHA_RANGE * scale));

        // text 居中绘制，mHalfHeight + offsetY = y 是 text 的中心坐标
        // text居中绘制，注意baseline的计算才能达到居中
        Paint.FontMetrics fm = mPaint.getFontMetrics();
        float baseline = mHalfHeight + offsetY - (fm.top + fm.bottom) / 2f;
        canvas.drawText(text, mHalfWidth, baseline, mPaint);
    }

    /**
     * 抛物线
     *
     * @param offset 偏移量
     * @param zero   零点坐标
     */
    private float parabola(float offset, float zero) {
        // Math.pow(x,y)这个函数是求x的y次方
        float f = (float) (1 - Math.pow(offset / zero, 2));
        return f < 0 ? 0 : f;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return mCanScroll && super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                cancelTimerTask();
                mLastTouchY = event.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                float offsetY = event.getY();
                mScrollDistance += offsetY - mLastTouchY;
                if (mScrollDistance > mHalfTextSpacing) {
                    if (!mCanScrollLoop) {
                        if (mSelectedIndex == 0) {
                            mLastTouchY = offsetY;
                            invalidate();
                            break;
                        } else {
                            mSelectedIndex--;
                        }
                    } else {
                        // 往下滑超过离开距离，将末尾元素移到首位
                        moveTailToHead();
                    }
                    mScrollDistance -= mTextSpacing;
                } else if (mScrollDistance < -mHalfTextSpacing) {
                    if (!mCanScrollLoop) {
                        if (mSelectedIndex == mDataList.size() - 1) {
                            mLastTouchY = offsetY;
                            invalidate();
                            break;
                        } else {
                            mSelectedIndex++;
                        }
                    } else {
                        // 往上滑超过离开距离，将首位元素移到末尾
                        moveHeadToTail();
                    }
                    mScrollDistance += mTextSpacing;
                }
                mLastTouchY = offsetY;
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                // 抬起手后 mSelectedIndex 由当前位置滚动到中间选中位置
                if (Math.abs(mScrollDistance) < 0.01) {
                    mScrollDistance = 0;
                    break;
                }
                cancelTimerTask();
                mTimerTask = new ScrollTimerTask(mHandler);
                mTimer.schedule(mTimerTask, 0, 10);
                break;
        }
        return true;
    }

    private void cancelTimerTask() {
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
        if (mTimer != null) {
            mTimer.purge();
        }
    }

    private void moveTailToHead() {
        if (!mCanScrollLoop || mDataList.isEmpty()) return;

        String tail = mDataList.get(mDataList.size() - 1);
        mDataList.remove(mDataList.size() - 1);
        mDataList.add(0, tail);
    }

    private void moveHeadToTail() {
        if (!mCanScrollLoop || mDataList.isEmpty()) return;

        String head = mDataList.get(0);
        mDataList.remove(0);
        mDataList.add(head);
    }

    /**
     * 持续滑动
     */
    private void keepScrolling() {
        // 绝对值
        if (Math.abs(mScrollDistance) < AUTO_SCROLL_SPEED) {
            mScrollDistance = 0;
            if (mTimerTask != null) {
                cancelTimerTask();

                if (mOnSelectListener != null && mSelectedIndex < mDataList.size()) {
                    mOnSelectListener.onSelect(this, mDataList.get(mSelectedIndex));
                }
            }
        } else if (mScrollDistance > 0) {
            // 向下滚动
            mScrollDistance -= AUTO_SCROLL_SPEED;
        } else {
            // 向上滚动
            mScrollDistance += AUTO_SCROLL_SPEED;
        }
        invalidate();
    }

    /**
     * 设置数据
     */
    public void setDataList(List<String> list) {
        if (list == null || list.isEmpty()) return;

        mDataList = list;
        // 重置 mSelectedIndex，防止溢出
        mSelectedIndex = 0;
        invalidate();
    }

    /**
     * 选择选中间位置的第一个选中项
     */
    public void setSelected(int index) {
        if (index >= mDataList.size()) return;

        mSelectedIndex = index;
        if (mCanScrollLoop) {
            // 可循环滚动时，mSelectedIndex 值固定为 mDataList / 2
            int position = mDataList.size() / 2 - mSelectedIndex;
            if (position < 0) {
                for (int i = 0; i < -position; i++) {
                    moveHeadToTail();
                    mSelectedIndex--;
                }
            } else if (position > 0) {
                for (int i = 0; i < position; i++) {
                    moveTailToHead();
                    mSelectedIndex++;
                }
            }
        }
        invalidate();
    }

    /**
     * 设置选择结果监听
     */
    public void setOnSelectListener(OnSelectListener listener) {
        mOnSelectListener = listener;
    }

    /**
     * 是否允许滚动
     */
    public void setCanScroll(boolean canScroll) {
        mCanScroll = canScroll;
    }

    /**
     * 是否允许循环滚动
     */
    public void setCanScrollLoop(boolean canLoop) {
        mCanScrollLoop = canLoop;
    }

    /**
     * 执行滚动动画
     */
    public void startAnim() {
        if (!mCanShowAnim) return;

        if (mScrollAnim == null) {
            PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 1f, 0f, 1f);
            PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 1f, 1.3f, 1f);
            PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 1f, 1.3f, 1f);
            mScrollAnim = ObjectAnimator.ofPropertyValuesHolder(this, alpha, scaleX, scaleY).setDuration(200);
        }

        if (!mScrollAnim.isRunning()) {
            mScrollAnim.start();
        }
    }

    /**
     * 是否允许滚动动画
     */
    public void setCanShowAnim(boolean canShowAnim) {
        mCanShowAnim = canShowAnim;
    }

    /**
     * 销毁资源
     */
    public void onDestroy() {
        mOnSelectListener = null;
        mHandler.removeCallbacksAndMessages(null);
        if (mScrollAnim != null && mScrollAnim.isRunning()) {
            mScrollAnim.cancel();
        }
        cancelTimerTask();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

}
