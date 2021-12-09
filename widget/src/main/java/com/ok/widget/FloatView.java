package com.ok.widget;


import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xier.core.tools.LogUtil;

/**
 * <pre>
 *      Copyright    : Copyright (c) 2020.
 *      Author       : jiaoya.
 *      Created Time : 2020/3/19.
 *      Desc         : 悬浮窗口
 * </pre>
 */

public abstract class FloatView {

    private final WindowManager mWindowManager;
    private Context mContext;
    private WindowManager.LayoutParams mParams;
    private boolean isShowing = false;
    private boolean mCreate = false;
    private View mDecor;
    private boolean isCanMove = false;

    public FloatView(@NonNull Context context) {
        mContext = context;
        //窗口管理器
        mWindowManager = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE));
        //布局参数
        mParams = new WindowManager.LayoutParams();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            mParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        //        mParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        //        mParams.type = WindowManager.LayoutParams.TYPE_SEARCH_BAR;
        //        mParams.type = WindowManager.LayoutParams.TYPE_INPUT_METHOD;
        mParams.format = PixelFormat.RGBA_8888;

        //设置之后window永远不会获取焦点,所以用户不能给此window发送点击事件焦点会传递给在其下面的可获取焦点的windo
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL; //设置之后window永远不会获取焦点,所以用户不能给此window发送点击事件焦点会传递给在其下面的可获取焦点的windo
        //WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        //WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |//当这个window对用户是可见状态,则保持设备屏幕不关闭且不变暗
        // WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS |//允许window扩展值屏幕之外
        //WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON;//当window被添加或者显示,系统会点亮屏幕,就好像用户唤醒屏幕一样*/
        mParams.gravity = Gravity.LEFT | Gravity.CENTER;
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        if (setCreateAnimator() != 0) {
            mParams.windowAnimations = setCreateAnimator();
        }
    }

    /**
     * 设置布局
     *
     * @return
     */
    protected abstract Integer setContentView();

    private int setCreateAnimator() {
        return -1;
    }

    public void show() {
        if (isShowing) {
            if (mDecor != null) {
                mDecor.setVisibility(View.VISIBLE);
            }
            return;
        }
        if (!mCreate) {
            dispathOnCreate();
        }
        mWindowManager.addView(mDecor, mParams);
        isShowing = true;
        onStart();
    }

    private void dispathOnCreate() {
        if (!mCreate) {
            create();
            mCreate = true;
        }
    }

    private void create() {
        mDecor = LayoutInflater.from(getContext()).inflate(setContentView(), null);
        onCreate(mDecor, mParams);
        if (isCanMove) {
            mDecor.setOnTouchListener(new FloatOnTouchListener());
        }
    }

    protected abstract void onCreate(View decor, WindowManager.LayoutParams layoutParams);

    protected void onStart() {

    }

    public void dismiss() {
        if (mDecor == null || !isShowing) {
            return;
        }
        try {
            mWindowManager.removeViewImmediate(mDecor);
            //这个地方可以注销ButterKnife
        } finally {
            mDecor = null;
            isShowing = false;
            mCreate = false;
            //这里可以还原参数
        }
    }

    //获取当前悬浮窗是否展示
    public final boolean isShowing() {
        return isShowing;
    }

    public final void hide() {
        if (mDecor != null) {
            mDecor.setVisibility(View.GONE);
        }
    }

    public final WindowManager.LayoutParams getParams() {
        return mParams;
    }

    public final WindowManager getWindowManager() {
        return mWindowManager;
    }

    @Nullable
    protected final <T extends View> T findViewById(@IdRes int id) {
        return mDecor.findViewById(id);
    }

    @NonNull
    protected final Context getContext() {
        return mContext;
    }

    public com.xier.widget.FloatView setCanMove(boolean isCan) {
        if (isCan) {
            isCanMove = true;
        } else {
            isCanMove = false;
        }
        return this;
    }

    private class FloatOnTouchListener implements View.OnTouchListener {
        private int x;
        private int y;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            LogUtil.e("FloatView", "onTouch: " + event.getRawX() + "-----------" + event.getRawY());
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = (int) event.getRawX();
                    y = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - x;
                    int movedY = nowY - y;
                    x = nowX;
                    y = nowY;
                    mParams.x = mParams.x + movedX;
                    mParams.y = mParams.y + movedY;

                    // 更新悬浮窗控件布局
                    mWindowManager.updateViewLayout(v, mParams);
                    break;
                default:
                    break;
            }
            return false;
        }
    }

}
