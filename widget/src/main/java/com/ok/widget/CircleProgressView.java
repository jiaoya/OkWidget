package com.ok.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.xier.core.tools.ConvertUtils;
import com.xier.core.tools.LogUtil;

/**
 * <pre>
 *      Copyright    : Copyright (c) 2020.
 *      Author       : jiaoya.
 *      Created Time : 2020/4/28.
 *      Desc         : 圆形倒计时
 * </pre>
 */
public class CircleProgressView extends View {
    /**
     * 进度条最大值
     */
    private int maxValue = 200;

    /**
     * 当前进度值
     */
    private int currentValue;

    /**
     * 每次扫过的角度，用来设置进度条圆弧所对应的圆心角，alphaAngle=(currentValue/maxValue)*360
     */
    private float alphaAngle;

    /**
     * 底部圆弧的颜色，默认为Color.LTGRAY
     */
    private int bgColor;

    /**
     * 进度条圆弧块的颜色
     */
    private int borderColor;
    /**
     * 中间文字颜色(默认蓝色)
     */
    private int centerTextColor = Color.BLUE;
    /**
     * 中间文字的字体大小(默认40dp)
     */
    private int centerTextSize;

    /**
     * 圆环的宽度
     */
    private int circleWidth;

    /**
     * 画圆弧的画笔
     */
    private Paint circlePaint;

    /**
     * 画文字的画笔
     */
    private Paint textPaint;
    /**
     * 是否使用渐变色
     */
    private boolean isShowGradient = false;

    /**
     * 渐变圆周颜色数组
     */
    private int[] colorArray = new int[]{Color.parseColor("#ffffff"),
            Color.parseColor("#ffffff"), Color.parseColor("#ffffff")};
    private int duration;
    private OnFinishListener listener;
    private ValueAnimator animator;


    public CircleProgressView(Context context) {
        this(context, null);
    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleProgress,
                defStyleAttr, 0);
        int n = ta.getIndexCount();

        for (int i = 0; i < n; i++) {
            int attr = ta.getIndex(i);
            if (attr == R.styleable.CircleProgress_bgColor) {
                bgColor = ta.getColor(attr, Color.LTGRAY); // 默认底色为亮灰色
            } else if (attr == R.styleable.CircleProgress_borderColor) {
                borderColor = ta.getColor(attr, Color.BLUE); // 默认进度条颜色为蓝色
            } else if (attr == R.styleable.CircleProgress_centerTextSize) {
                centerTextSize = ta.getDimensionPixelSize(attr, ConvertUtils.dp2px(40)); // 默认中间文字字体大小为40dp
            } else if (attr == R.styleable.CircleProgress_borderWidth) {
                circleWidth = ta.getDimensionPixelSize(attr, ConvertUtils.dp2px(6)); // 默认圆弧宽度为6dp
            } else if (attr == R.styleable.CircleProgress_centerTextColor) {
                centerTextColor = ta.getColor(attr, Color.BLUE); // 默认中间文字颜色为蓝色
            } else if (attr == R.styleable.CircleProgress_isShowGradient) {
                isShowGradient = ta.getBoolean(attr, false); // 默认不适用渐变色
            }
        }
        ta.recycle();

        circlePaint = new Paint();
        circlePaint.setAntiAlias(true); // 抗锯齿
        circlePaint.setDither(true); // 防抖动
        circlePaint.setStrokeWidth(circleWidth);//画笔宽度

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setDither(true);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 分别获取期望的宽度和高度，并取其中较小的尺寸作为该控件的宽和高,并且不超过屏幕宽高
        int widthPixels = this.getResources().getDisplayMetrics().widthPixels;//获取屏幕宽
        int heightPixels = this.getResources().getDisplayMetrics().heightPixels;//获取屏幕高
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int hedight = MeasureSpec.getSize(heightMeasureSpec);
        int minWidth = Math.min(widthPixels, width);
        int minHedight = Math.min(heightPixels, hedight);
        setMeasuredDimension(Math.min(minWidth, minHedight), Math.min(minWidth, minHedight));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int center = this.getWidth() / 2;
        int radius = center - circleWidth / 2;

        drawCircle(canvas, center, radius); // 绘制进度圆弧
        // drawText(canvas, center);
    }

    /**
     * 绘制进度圆弧
     *
     * @param canvas 画布对象
     * @param center 圆心的x和y坐标
     * @param radius 圆的半径
     */
    private void drawCircle(Canvas canvas, int center, int radius) {
        circlePaint.setShader(null); // 清除上一次的shader
        circlePaint.setColor(bgColor); // 设置底部圆环的颜色，这里使用第一种颜色
        circlePaint.setStyle(Paint.Style.STROKE); // 设置绘制的圆为空心
        circlePaint.setAntiAlias(true); // 设置paint抗锯齿
        canvas.drawCircle(center, center, radius, circlePaint); // 画底部的空心圆
        RectF oval = new RectF(center - radius, center - radius, center + radius, center + radius); // 圆的外接正方形
        if (isShowGradient) {
            // 绘制颜色渐变圆环
            // shader类是Android在图形变换中非常重要的一个类。Shader在三维软件中我们称之为着色器，其作用是来给图像着色。
            LinearGradient linearGradient = new LinearGradient(circleWidth, circleWidth, getMeasuredWidth()
                    - circleWidth, getMeasuredHeight() - circleWidth, colorArray, null, Shader.TileMode.MIRROR);
            circlePaint.setShader(linearGradient);
        }
        // circlePaint.setShadowLayer(10, 10, 10, Color.WHITE); // 阴影
        circlePaint.setColor(borderColor); // 设置圆弧的颜色
        circlePaint.setStrokeCap(Paint.Cap.ROUND); // 把每段圆弧改成圆角的

        alphaAngle = currentValue * 360.0f / maxValue * 1.0f; // 计算每次画圆弧时扫过的角度，这里计算要注意分母要转为float类型，否则alphaAngle永远为0
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));// 设置canvas抗锯齿
        canvas.drawArc(oval, -90, alphaAngle, false, circlePaint);
    }

    /**
     * 绘制文字
     *
     * @param canvas 画布对象
     * @param center 圆心的x和y坐标
     */
    private void drawText(Canvas canvas, int center) {
        int result = ((maxValue - currentValue) * (duration / 1000) / maxValue); // 计算进度
        String percent;
        if (maxValue == currentValue) {
            percent = "完成";
            textPaint.setTextSize(centerTextSize); // 设置要绘制的文字大小
        } else {
            percent = (result / 60 < 10 ? "0" + result / 60 : result / 60) + ":" + (result % 60 < 10 ? "0" + result % 60 : result % 60);
//      percent = result+"秒";
            textPaint.setTextSize(centerTextSize + centerTextSize / 3); // 设置要绘制的文字大小
        }
        textPaint.setTextAlign(Paint.Align.CENTER); // 设置文字居中，文字的x坐标要注意
        textPaint.setColor(centerTextColor); // 设置文字颜色

        textPaint.setStrokeWidth(0); // 注意此处一定要重新设置宽度为0,否则绘制的文字会重叠
        Rect bounds = new Rect(); // 文字边框
        textPaint.getTextBounds(percent, 0, percent.length(), bounds); // 获得绘制文字的边界矩形
        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt(); // 获取绘制Text时的四条线
        int baseline = center + (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom; // 计算文字的基线    canvas.drawText(percent, center, baseline, textPaint); // 绘制表示进度的文字
    }

    /**
     * 设置圆环的宽度
     *
     * @param width
     */
    public void setCircleWidth(int width) {
        this.circleWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, getResources()
                .getDisplayMetrics());
        circlePaint.setStrokeWidth(circleWidth);
        //一般只是希望在View发生改变时对UI进行重绘。invalidate()方法系统会自动调用 View的onDraw()方法。
        invalidate();
    }

    /**
     * 设置圆环的底色，默认为亮灰色LTGRAY
     *
     * @param color
     */
    public void setBgColor(int color) {
        this.bgColor = color;
        circlePaint.setColor(bgColor);
        //一般只是希望在View发生改变时对UI进行重绘。invalidate()方法系统会自动调用 View的onDraw()方法。
        invalidate();
    }

    /**
     * 设置进度条的颜色，默认为蓝色<br>
     *
     * @param color
     */
    public void setBorderColor(int color) {
        this.borderColor = color;
        circlePaint.setColor(borderColor);
        //一般只是希望在View发生改变时对UI进行重绘。invalidate()方法系统会自动调用 View的onDraw()方法。
        invalidate();
    }

    /**
     * 设置进度条渐变色颜色数组
     *
     * @param colors 颜色数组，类型为int[]
     */
    public void setColorArray(int[] colors) {
        this.colorArray = colors;
        //一般只是希望在View发生改变时对UI进行重绘。invalidate()方法系统会自动调用 View的onDraw()方法。
        invalidate();
    }


    /**
     * 按进度显示百分比，可选择是否启用数字动画
     *
     * @param duration 动画时长
     */
    public void setDuration(int duration, OnFinishListener listener) {
        this.listener = listener;
        this.duration = duration + 1000;
    }

    /**
     * 开始倒计时
     */
    public void start() {
        if (duration == 0) {
            Toast.makeText(getContext(), "请先设置时长", Toast.LENGTH_SHORT).show();
            return;
        }
        if (animator != null) {
            animator.cancel();
        } else {
            animator = ValueAnimator.ofInt(0, maxValue);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {

                    currentValue = (int) animation.getAnimatedValue();
                    LogUtil.e("onAnimationUpdate", animation.getRepeatCount() + "");
                    if (listener != null) {
                        listener.onProgressing(((maxValue - currentValue) * (duration / 1000) / maxValue));
                    }
                    //一般只是希望在View发生改变时对UI进行重绘。invalidate()方法系统会自动调用 View的onDraw()方法。
                    invalidate();
                    if (maxValue == currentValue && com.xier.widget.CircleProgressView.this.listener != null) {
                        CircleProgressView.this.listener.onFinish();
                    }
                }
            });
            animator.setInterpolator(new LinearInterpolator());
        }
        animator.setDuration(duration);
        animator.start();
    }

    /**
     * 结束倒计时
     */
    public void stop() {
        if (animator != null) {
            animator.cancel();
        }
    }

    public interface OnFinishListener {

        void onProgressing(int currentDuration);

        void onFinish();
    }

    public void setOnFinishListener(OnFinishListener listener) {
        this.listener = listener;
    }

}
