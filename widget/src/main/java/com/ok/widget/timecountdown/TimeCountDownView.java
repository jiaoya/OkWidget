package com.ok.widget.timecountdown;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.xier.core.tools.CountDownUtils;
import com.xier.widget.R;

import java.util.concurrent.TimeUnit;

/**
 * @author Jane
 * @date 2020/7/29
 * @desc 商城用到的倒计时view
 */
public class TimeCountDownView extends FrameLayout {

    View inflate;

    private AppCompatTextView tvDay;
    private AppCompatTextView tvHour;
    private AppCompatTextView tvMinute;
    private AppCompatTextView tvSecond;
    private AppCompatTextView tvPonit1;
    private AppCompatTextView tvPonit2;

    /**
     * 时间（单位：秒
     */
    long timeLong = 0;
    long day;
    long hour;
    long minute;
    long second;

    /**
     * 时、分、秒的背景
     */
    int tvBgRes;
    /**
     * 时分秒字体颜色
     */
    int tvColor;
    /**
     * 天字体颜色
     */
    int tvDayColor;
    /**
     * 天字内边距
     */
    float tvPadding;
    /**
     * 天时分秒字体大小
     */
    float tvSize;
    /**
     * 时分秒中间分割点的颜色
     */
    int pointColor;

    /**
     * 天、时、分、秒对应的秒数单位
     */
    int[] unitLen = {86400000, 3600000, 60000, 1000};

    private int MODEL = 0;

    /**
     * 组件初始化，各属性设置默认值
     *
     * @param context
     */
    public TimeCountDownView(Context context) {
        super(context);
        timeLong = 0;
        tvBgRes = R.drawable.shape_rectangle_r2_ff2442;
        tvColor = ContextCompat.getColor(getContext(), R.color.wt_white);
        tvDayColor = ContextCompat.getColor(getContext(), R.color.wt_333333);
        tvPadding = getResources().getDimension(R.dimen.dp_4);
        tvSize = getResources().getDimension(R.dimen.dp_14);
        pointColor = ContextCompat.getColor(context, R.color.wt_FF2442);
        init();
    }

    /**
     * 组件初始化，各属性设置默认值
     *
     * @param context
     * @param attrs
     */
    public TimeCountDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getTypedArray(context, attrs, 0);
        init();
    }

    /**
     * 组件初始化，各属性设置默认值
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public TimeCountDownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getTypedArray(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 属性初始化
     * 初始化属性取值（styleable）
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    private void getTypedArray(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TimeCountDownView, defStyleAttr, 0);
        timeLong = typedArray.getInt(R.styleable.TimeCountDownView_second, 0);
        tvBgRes = typedArray.getResourceId(R.styleable.TimeCountDownView_textBackground, R.drawable.shape_rectangle_r2_ff2442);
        tvColor = typedArray.getColor(R.styleable.TimeCountDownView_textColor, ContextCompat.getColor(context, R.color.wt_white));
        tvDayColor = typedArray.getColor(R.styleable.TimeCountDownView_textDayColor, ContextCompat.getColor(context, R.color.wt_333333));
        tvPadding = typedArray.getDimension(R.styleable.TimeCountDownView_textPaddiing, getResources().getDimension(R.dimen.dp_4));
        tvSize = typedArray.getDimension(R.styleable.TimeCountDownView_textSize, getResources().getDimension(R.dimen.dp_14));
        pointColor = typedArray.getColor(R.styleable.TimeCountDownView_pointColor, ContextCompat.getColor(context, R.color.wt_FF2442));
    }

    /**
     * 组件初始化，并赋值
     */
    private void init() {
        mCountDownTask = new CountDownTask(this);

        inflate = inflate(getContext(), R.layout.wt_view_time_count_down, this);

        tvDay = inflate.findViewById(R.id.tvTimeDay);
        tvHour = inflate.findViewById(R.id.tvTimeHour);
        tvMinute = inflate.findViewById(R.id.tvTimeMinute);
        tvSecond = inflate.findViewById(R.id.tvTimeSecond);
        tvPonit1 = inflate.findViewById(R.id.tvPoint1);
        tvPonit2 = inflate.findViewById(R.id.tvPoint2);

        tvHour.setBackgroundResource(tvBgRes);
        tvMinute.setBackgroundResource(tvBgRes);
        tvSecond.setBackgroundResource(tvBgRes);

        tvDay.setTextColor(tvDayColor);

        tvHour.setTextColor(tvColor);
        tvMinute.setTextColor(tvColor);
        tvSecond.setTextColor(tvColor);

        tvPonit1.setTextColor(pointColor);
        tvPonit2.setTextColor(pointColor);

        tvHour.setPadding((int) tvPadding, 0, (int) tvPadding, 0);
        tvMinute.setPadding((int) tvPadding, 0, (int) tvPadding, 0);
        tvSecond.setPadding((int) tvPadding, 0, (int) tvPadding, 0);

        tvDay.setTextSize(TypedValue.COMPLEX_UNIT_PX, tvSize);
        tvHour.setTextSize(TypedValue.COMPLEX_UNIT_PX, tvSize);
        tvMinute.setTextSize(TypedValue.COMPLEX_UNIT_PX, tvSize);
        tvSecond.setTextSize(TypedValue.COMPLEX_UNIT_PX, tvSize);
        tvPonit1.setTextSize(TypedValue.COMPLEX_UNIT_PX, tvSize);
        tvPonit2.setTextSize(TypedValue.COMPLEX_UNIT_PX, tvSize);

        initDataToViews();
    }

    @Override
    protected void onDetachedFromWindow() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        super.onDetachedFromWindow();
        // view销毁时监听
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // 设置默认高度
//        ViewGroup.LayoutParams params = getLayoutParams();
//        if (params.height == LayoutParams.WRAP_CONTENT || params.height < ConvertUtils.dp2px(45)) {
//            LayoutParams layoutParams = (LayoutParams) inflate.getLayoutParams();
//            layoutParams.height = ConvertUtils.dp2px(45);
//            inflate.setLayoutParams(layoutParams);
//        }
    }

    /**
     * 设置时分秒中间分割点的颜色
     *
     * @param pointColor 颜色（color）
     */
    public void setPointColor(int pointColor) {
//        this.pointColor = pointColor;
        if (tvPonit1 != null) {
            tvPonit1.setTextColor(pointColor);
        }
        if (tvPonit2 != null) {
            tvPonit2.setTextColor(pointColor);
        }
    }

    /**
     * 设置时、分、秒的背景
     *
     * @param tvBgRes 背景（drawable）
     */
    public void setTextBackgroundResource(int tvBgRes) {
//        this.tvBgRes = tvBgRes;
        if (tvHour != null) {
            tvHour.setBackgroundResource(tvBgRes);
        }
        if (tvMinute != null) {
            tvMinute.setBackgroundResource(tvBgRes);
        }
        if (tvSecond != null) {
            tvSecond.setBackgroundResource(tvBgRes);
        }
    }

    /**
     * 设置时分秒字体颜色
     *
     * @param tvColor 颜色（color）
     */
    public void setTextColor(int tvColor) {
        this.tvColor = getResources().getColor(tvColor);
        if (tvHour != null) {
            tvHour.setTextColor(getResources().getColor(tvColor));
        }
        if (tvMinute != null) {
            tvMinute.setTextColor(getResources().getColor(tvColor));
        }
        if (tvSecond != null) {
            tvSecond.setTextColor(getResources().getColor(tvColor));
        }
    }

    /**
     * 设置天字体颜色
     *
     * @param tvDayColor 颜色（color）
     */
    public void setDayTextColor(int tvDayColor) {
        this.tvDayColor = tvDayColor;
        if (tvDay != null) {
            tvDay.setTextColor(getResources().getColor(tvDayColor));
        }
    }

    /**
     * 设置天时分秒字体大小
     *
     * @param tvPadding 字体内边距（dimen）
     */
    public void setTextPadding(float tvPadding) {
        this.tvPadding = tvPadding;
        if (tvHour != null) {
            tvHour.setPadding((int) tvPadding, 0, (int) tvPadding, 0);
        }
        if (tvMinute != null) {
            tvMinute.setPadding((int) tvPadding, 0, (int) tvPadding, 0);
        }
        if (tvSecond != null) {
            tvSecond.setPadding((int) tvPadding, 0, (int) tvPadding, 0);
        }
    }

    /**
     * 设置天时分秒字体大小
     *
     * @param tvSize 字体大小（dimen）
     */
    public void setTextSize(float tvSize) {
        this.tvSize = tvSize;
        if (tvDay != null) {
            tvDay.setTextSize(TypedValue.COMPLEX_UNIT_PX, tvSize);
        }
        if (tvHour != null) {
            tvHour.setTextSize(TypedValue.COMPLEX_UNIT_PX, tvSize);
        }
        if (tvMinute != null) {
            tvMinute.setTextSize(TypedValue.COMPLEX_UNIT_PX, tvSize);
        }
        if (tvSecond != null) {
            tvSecond.setTextSize(TypedValue.COMPLEX_UNIT_PX, tvSize);
        }
    }

    /**
     * 数据填充
     */
    private void initDataToViews() {
        if (timeLong > 0) {
            setTextBackgroundResource(tvBgRes);
            setPointColor(pointColor);
            day = TimeUnit.MILLISECONDS.toDays(timeLong);
            hour = TimeUnit.MILLISECONDS.toHours(timeLong - day * unitLen[0]);
            minute = TimeUnit.MILLISECONDS.toMinutes(timeLong - day * unitLen[0] - hour * unitLen[1]);
            second = TimeUnit.MILLISECONDS.toSeconds(timeLong - day * unitLen[0] - hour * unitLen[1] - minute * unitLen[2]);
        } else {
            setTextBackgroundResource(R.drawable.shape_rectangle_r2_cccccc);
            setPointColor(R.color.wt_cccccc);
            day = 0;
            hour = 0;
            minute = 0;
            second = 0;
        }
        if (day > 0) {
            tvDay.setVisibility(View.VISIBLE);
            tvDay.setText(day + "天");
        } else {
            tvDay.setVisibility(View.GONE);
        }
        tvHour.setText((hour < 10 ? "0" : "") + hour);
        tvMinute.setText((minute < 10 ? "0" : "") + minute);
        tvSecond.setText((second < 10 ? "0" : "") + second);

        // 开始倒计时
        if (MODEL == 0) {
            startCountDown();
        } else {
            startCountDown1();
        }
    }

    /**
     * 填充时间
     *
     * @param timeLong 时间（单位：毫秒）
     */
    public void setTimeLong(long timeLong) {
        this.timeLong = timeLong;
        MODEL = 0;
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        initDataToViews();
    }

    public void setTimeLong1(long timeLong) {
        this.timeLong = timeLong;
        MODEL = 1;
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        initDataToViews();
    }

    public long getTimeLong() {
        return timeLong;
    }

    @Override
    public void setOnClickListener(OnClickListener listener) {
        inflate.setOnClickListener(listener);
    }

    CountDownTimer countDownTimer;

    public void startCountDown() {
        if (countDownTimer == null && timeLong > 1000) {
            countDownTimer = CountDownUtils.cDownTimer(timeLong, 1000, new CountDownUtils.ICountDownCallback() {
                @Override
                public void onStart(long currentTotleTime, long day, long hour, long minute, long second) {
                    if (countDownListener != null) {
                        countDownListener.countDown(currentTotleTime);
                    }
                    if (day < 1) {
                        tvDay.setVisibility(GONE);
                    } else {
                        tvDay.setText(day + "天");
                        tvDay.setVisibility(VISIBLE);
                    }
                    tvHour.setText((hour < 10 ? "0" : "") + hour);
                    tvMinute.setText((minute < 10 ? "0" : "") + minute);
                    tvSecond.setText((second < 10 ? "0" : "") + second);

//                    Log.e("timeLong", timeLong + "");
                }

                @Override
                public void onFinish() {
                    if (countDownListener != null) {
                        countDownListener.complete();
                    }
                    setTextBackgroundResource(R.drawable.shape_rectangle_r2_cccccc);
                    setPointColor(R.color.wt_cccccc);
                    tvDay.setVisibility(GONE);
                    tvHour.setText("00");
                    tvMinute.setText("00");
                    tvSecond.setText("00");
                }
            });
        }
    }

    CountDownTask mCountDownTask;

    public void startCountDown1() {
        if (timeLong > 0) {
            stopCountDown();
            postDelayed(mCountDownTask, 1000);
        }
    }

    public void stopCountDown() {
        if (mCountDownTask != null) {
            removeCallbacks(mCountDownTask);
        }
    }

    private class CountDownTask implements Runnable {
        TimeCountDownView countDownView;

        private CountDownTask(TimeCountDownView countDownView) {
            this.countDownView = countDownView;
        }

        @Override
        public void run() {
            if (countDownView != null) {
                long countTime = countDownView.getTimeLong();
                if (countTime >= 0) {
                    countTime--;
                    countDownView.setTimeLong(countTime);
                    if (countDownListener != null) {
                        countDownListener.countDown(countTime);

                        if (countTime <= 0) {
                            countDownListener.complete();
                        }
                    }
//                    if (countTime >= 0) {
                    countDownView.startCountDown();
//                    }
                } else {
//                    if (countDownListener!=null) {
//                        countDownListener.complete();
//                    }
                }
            }
        }
    }

    TimeCountDownListener countDownListener;

    public void setCountDownListener(TimeCountDownListener countDownListener) {
        this.countDownListener = countDownListener;
    }

    public interface TimeCountDownListener {
        void countDown(long newTime);

        void complete();
    }

}
