package com.ok.widget.titlebar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.ok.widget.R;
import com.xier.core.tools.NullUtil;
import com.xier.core.tools.ResourceUtils;


/**
 * <pre>
 *      Copyright    : Copyright (c) 2019.
 *      Author       : jiaoya.
 *      Created Time : 2019-11-20.
 *      Desc         : 统一标题栏2
 * </pre>
 */
public class TitleBar extends FrameLayout {

    private AppCompatImageView mIvNavLeft;
    private AppCompatTextView mTitleTextView;
    private AppCompatTextView mTvRight;
    private AppCompatImageView mIvRight;
    private AppCompatImageView mIvRight2;
    private View tbline;

    String title = "";
    int titleColor;
    float titleSize;
    int leftSrc = -1;
    int rightSrc = -1;
    int rightImg2Src = -1;
    int rightSrcVisibility = 2;
    String rightTitle = "";
    int rightTitleColor;
    float rightTitleSize;
    int rightTitleVisibility = 2;
    int rightImg2Visibility = 2;
    int lineVisibility = 0;

    public TitleBar(Context context) {
        super(context);
        titleColor = ContextCompat.getColor(getContext(), R.color.wt_333333);
        titleSize = getResources().getDimension(R.dimen.dp_17);
        rightTitleColor = ContextCompat.getColor(context, R.color.wt_213cd5);
        rightTitleSize = getResources().getDimension(R.dimen.dp_15);
        init();
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        getTypedArray(context, attrs, 0);
        init();
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getTypedArray(context, attrs, defStyleAttr);
        init();
    }

    private void getTypedArray(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleBar, defStyleAttr, 0);
        title = typedArray.getString(R.styleable.TitleBar_title);
        titleColor = typedArray.getColor(R.styleable.TitleBar_titleColor, ContextCompat.getColor(context, R.color.wt_333333));
        titleSize = typedArray.getDimension(R.styleable.TitleBar_titleSize, getResources().getDimension(R.dimen.dp_17));
        leftSrc = typedArray.getResourceId(R.styleable.TitleBar_leftSrc, -1);
        rightSrc = typedArray.getResourceId(R.styleable.TitleBar_rightSrc, -1);
        rightImg2Src = typedArray.getResourceId(R.styleable.TitleBar_rightImg2Src, -1);
        rightSrcVisibility = typedArray.getInteger(R.styleable.TitleBar_rightSrcVisibility, GONE);
        rightImg2Visibility = typedArray.getInteger(R.styleable.TitleBar_rightSrcVisibility, GONE);

        rightTitle = typedArray.getString(R.styleable.TitleBar_rightTitle);
        rightTitleColor = typedArray.getColor(R.styleable.TitleBar_rightTitleColor, ContextCompat.getColor(context, R.color.wt_213cd5));
        rightTitleSize = typedArray.getDimension(R.styleable.TitleBar_rightTitleSize, getResources().getDimension(R.dimen.dp_15));
        rightTitleVisibility = typedArray.getInteger(R.styleable.TitleBar_rightTitleVisibility, 2);
        lineVisibility = typedArray.getInteger(R.styleable.TitleBar_lineVisibility, 0);
    }

    View inflate;

    private void init() {
        inflate = inflate(getContext(), R.layout.wt_layout_title_bar, this);
        mTitleTextView = inflate.findViewById(R.id.tvTitle);
        mIvNavLeft = inflate.findViewById(R.id.ivLeftBack);
        mTvRight = inflate.findViewById(R.id.tvRight);
        mIvRight = inflate.findViewById(R.id.ivRight);
        mIvRight2 = inflate.findViewById(R.id.ivRight2);
        tbline = inflate.findViewById(R.id.tbLine);

        if (NullUtil.notEmpty(title)) {
            mTitleTextView.setText(title);
        }
        mTitleTextView.setTextColor(titleColor);
        mTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize);
        if (leftSrc != -1) {
            mIvNavLeft.setImageResource(leftSrc);
        }

        if (rightSrcVisibility == 0) {
            mIvRight.setVisibility(VISIBLE);
        } else if (rightSrcVisibility == 1) {
            mIvRight.setVisibility(INVISIBLE);
        } else {
            mIvRight.setVisibility(GONE);
        }

        if (rightImg2Visibility == 0) {
            mIvRight2.setVisibility(VISIBLE);
        } else if (rightImg2Visibility == 1) {
            mIvRight2.setVisibility(INVISIBLE);
        } else {
            mIvRight2.setVisibility(GONE);
        }

        if (rightSrc != -1) {
            mIvRight.setImageResource(rightSrc);
            mIvRight.setVisibility(VISIBLE);
        }
        if (rightImg2Src != -1) {
            mIvRight2.setImageResource(rightImg2Src);
            mIvRight2.setVisibility(VISIBLE);
        }
        if (rightTitleVisibility == 0) {
            mTvRight.setVisibility(VISIBLE);
        } else if (rightTitleVisibility == 1) {
            mTvRight.setVisibility(INVISIBLE);
        } else {
            mTvRight.setVisibility(GONE);
        }
        if (NullUtil.notEmpty(rightTitle)) {
            mTvRight.setText(rightTitle);
            mTvRight.setVisibility(VISIBLE);
        }
        mTvRight.setTextColor(rightTitleColor);
        mTvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, rightTitleSize);

        if (lineVisibility == 0) {
            tbline.setVisibility(VISIBLE);
        } else if (lineVisibility == 1) {
            tbline.setVisibility(INVISIBLE);
        } else {
            tbline.setVisibility(GONE);
        }
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

    public void setTitle(String title) {
        if (mTitleTextView != null) {
            mTitleTextView.setText(title);
        }
    }

    public void setTitle(@StringRes int resid) {
        if (mTitleTextView != null) {
            mTitleTextView.setText(resid);
        }
    }

    public void setTitleTextColor(@ColorRes int colorId) {
        if (mTitleTextView != null) {
            mTitleTextView.setTextColor(ResourceUtils.getColor(colorId));
        }
    }

    public TextView getTitleTextView() {
        return mTitleTextView;
    }

    public AppCompatTextView getTvRightView() {
        return mTvRight;
    }

    public void setRightSrc(int rightSrc) {
        if (mTvRight != null) {
            mIvRight.setImageResource(rightSrc);
            mIvRight.setVisibility(VISIBLE);
        }
    }

    public void setIvRight2Src(int rightSrc) {
        if (mIvRight2 != null) {
            mIvRight2.setImageResource(rightSrc);
            mIvRight2.setVisibility(VISIBLE);
        }
    }

    public void setRightTitle(String title) {
        if (mTvRight != null) {
            mTvRight.setText(title);
            mTvRight.setVisibility(VISIBLE);
        }
    }

    public void setRightTitle(String title, OnClickListener listener) {
        if (mTvRight != null) {
            mTvRight.setText(title);
            mTvRight.setVisibility(VISIBLE);
            mTvRight.setOnClickListener(listener);
        }
    }

    public void setRightTitle(int strId, OnClickListener listener) {
        if (mTvRight != null) {
            mTvRight.setText(strId);
            mTvRight.setVisibility(VISIBLE);
            mTvRight.setOnClickListener(listener);
        }
    }

    public void setRightTitle(@StringRes int resid) {
        if (mTvRight != null) {
            mTvRight.setText(resid);
            mTvRight.setVisibility(VISIBLE);
        }
    }

    public void setRightTitleColor(@ColorInt int color) {
        if (mTvRight != null) {
            mTvRight.setTextColor(color);
            mTvRight.setVisibility(VISIBLE);
        }
    }

    public void setRightTitleColor(ColorStateList colors) {
        if (mTvRight != null) {
            mTvRight.setTextColor(colors);
            mTvRight.setVisibility(VISIBLE);
        }
    }

    public void setRightClickListener(OnClickListener listener) {
        if (mTvRight != null) {
            mTvRight.setOnClickListener(listener);
        }
    }

    public void setRightIvClickListener(OnClickListener listener) {
        if (mIvRight != null) {
            mIvRight.setOnClickListener(listener);
        }
    }

    public void setRightIv2ClickListener(OnClickListener listener) {
        if (mIvRight2 != null) {
            mIvRight2.setOnClickListener(listener);
        }
    }

    public AppCompatImageView getLeftImg() {
        return mIvNavLeft;
    }

    public void setLeftImage(@DrawableRes int resId) {
        if (mIvNavLeft != null) {
            mIvNavLeft.setImageResource(resId);
        }
    }

    @Override
    public void setAlpha(float alpha) {
        super.setAlpha(alpha);
    }

    /**
     * 获取下划线
     *
     * @return
     */
    public View getLine() {
        return tbline;
    }

    public void setNavLeftOnClickListener(OnClickListener listener) {
        mIvNavLeft.setOnClickListener(listener);
    }

    public void setNavTvRightOnClickListener(OnClickListener listener) {
        mTvRight.setOnClickListener(listener);
    }

    public TextView getTvRight() {
        return mTvRight;
    }

    public AppCompatImageView getImgRight2() {
        return mIvRight2;
    }

    public AppCompatImageView getIvRight() {
        return mIvRight;
    }
}
