package com.ok.widget.errorview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.ok.widget.R;


/**
 * @author Jane
 * @date 2020/6/10
 * @desc 通用缺省页
 */
public class ErrorView extends FrameLayout {

    private FrameLayout flBack;
    private LinearLayout llErContent;
    private AppCompatImageView ivError;
    private AppCompatTextView tvErrorBtn;
    private AppCompatTextView tvErrorTitle;
    private AppCompatTextView tvErrorSubtitle;

    private int ivErrorSrc = -1;

    private String errorTitle = "";
    private int errorTitleColor;
    private float errorTitleSize;

    private String errorSubTitle = "";
    private int errorSubTitleColor;
    private float errorSubTitleSize;
    private int errorSubTitleVisibility = 2;

    private int marginTop = 0;
    private int background = 0;

    public ErrorView(Context context, int marginTop) {
        super(context);
        this.marginTop = marginTop;
        this.background = ContextCompat.getColor(getContext(), R.color.wt_white);
        errorTitleColor = ContextCompat.getColor(getContext(), R.color.wt_333333);
        errorTitleSize = getResources().getDimension(R.dimen.dp_14);
        errorSubTitleColor = ContextCompat.getColor(context, R.color.wt_999999);
        errorSubTitleSize = getResources().getDimension(R.dimen.dp_14);
        init();
    }

    public ErrorView(Context context) {
        super(context);
        background = ContextCompat.getColor(getContext(), R.color.wt_white);
        errorTitleColor = ContextCompat.getColor(getContext(), R.color.wt_333333);
        errorTitleSize = getResources().getDimension(R.dimen.dp_14);
        errorSubTitleColor = ContextCompat.getColor(context, R.color.wt_999999);
        errorSubTitleSize = getResources().getDimension(R.dimen.dp_14);
        init();
    }


    public ErrorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getTypedArray(context, attrs, 0);
        init();
    }

    public ErrorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getTypedArray(context, attrs, defStyleAttr);
        init();
    }

    private void getTypedArray(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ErrorView, defStyleAttr, 0);
        errorTitle = typedArray.getString(R.styleable.ErrorView_errorTitle);
        errorTitleColor = typedArray.getColor(R.styleable.ErrorView_errorTitleColor, ContextCompat.getColor(context, R.color.wt_333333));
        errorTitleSize = typedArray.getDimension(R.styleable.ErrorView_errorTitleSize, getResources().getDimension(R.dimen.dp_17));
        ivErrorSrc = typedArray.getResourceId(R.styleable.ErrorView_errorSrc, -1);
        errorSubTitle = typedArray.getString(R.styleable.ErrorView_errorSubTitle);
        errorSubTitleColor = typedArray.getColor(R.styleable.ErrorView_errorSubTitleColor, ContextCompat.getColor(context, R.color.wt_999999));
        errorSubTitleSize = typedArray.getDimension(R.styleable.ErrorView_errorSubTitleSize, getResources().getDimension(R.dimen.dp_15));
        errorSubTitleVisibility = typedArray.getInteger(R.styleable.ErrorView_errorSubTitleVisibility, 2);
        background = typedArray.getColor(R.styleable.ErrorView_errorBackground, ContextCompat.getColor(context, R.color.wt_white));
    }

    private void init() {
        this.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        inflate(getContext(), R.layout.wt_view_error, this);
        flBack = findViewById(R.id.flBack);
        tvErrorBtn = findViewById(R.id.tvErrorBtn);
        llErContent = findViewById(R.id.llErContent);

        if (marginTop > 0) {
            LayoutParams params = (LayoutParams) llErContent.getLayoutParams();
            params.topMargin = marginTop;
            llErContent.setLayoutParams(params);
        }
        if (background > 0) {
            llErContent.setBackgroundResource(background);
        } else {
            llErContent.setBackgroundResource(0);
        }

        ivError = findViewById(R.id.ivError);
        tvErrorTitle = findViewById(R.id.tvErrorTitle);
        tvErrorSubtitle = findViewById(R.id.tvErrorSubtitle);

        if (NullUtil.notEmpty(errorTitle)) {
            tvErrorTitle.setText(errorTitle);
            tvErrorTitle.setVisibility(VISIBLE);
        } else {
            tvErrorTitle.setVisibility(GONE);
        }

        tvErrorTitle.setTextColor(errorTitleColor);
        tvErrorTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, errorTitleSize);
        if (ivErrorSrc != -1) {
            ivError.setImageResource(ivErrorSrc);
        }

        if (errorSubTitleVisibility == 0) {
            tvErrorSubtitle.setVisibility(GONE);
        } else if (errorSubTitleVisibility == 1) {
            tvErrorSubtitle.setVisibility(INVISIBLE);
        } else {
            tvErrorSubtitle.setVisibility(VISIBLE);
        }
        if (NullUtil.notEmpty(errorSubTitle)) {
            tvErrorSubtitle.setText(errorSubTitle);
            tvErrorSubtitle.setVisibility(VISIBLE);
        }
        tvErrorSubtitle.setTextColor(errorSubTitleColor);
        tvErrorSubtitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, errorSubTitleSize);

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

    public void setErrorTitle(String title) {
        if (tvErrorTitle != null) {
            tvErrorTitle.setText(title);
            tvErrorTitle.setVisibility(VISIBLE);
        }
    }

    public void setTitle(@StringRes int resid) {
        if (tvErrorTitle != null) {
            tvErrorTitle.setText(resid);
        }
    }

    public void setTitleTextColor(int colorId) {
        if (tvErrorTitle != null) {
            tvErrorTitle.setTextColor(getResources().getColor(colorId));
        }
    }

    public TextView getErrorTitleTextView() {
        return tvErrorTitle;
    }

    public void setErrorSubTitle(String title) {
        if (tvErrorSubtitle != null) {
            tvErrorSubtitle.setText(title);
            tvErrorSubtitle.setVisibility(VISIBLE);
        }
    }

    public void setErrorSubTitle(@StringRes int resid) {
        if (tvErrorSubtitle != null) {
            tvErrorSubtitle.setText(resid);
            tvErrorSubtitle.setVisibility(VISIBLE);
        }
    }

    public void setErrorSubTitleColor(@ColorInt int color) {
        if (tvErrorSubtitle != null) {
            tvErrorSubtitle.setTextColor(color);
            tvErrorSubtitle.setVisibility(VISIBLE);
        }
    }

    public void setErrorSubTitleColor(ColorStateList colors) {
        if (tvErrorSubtitle != null) {
            tvErrorSubtitle.setTextColor(colors);
            tvErrorSubtitle.setVisibility(VISIBLE);
        }
    }

    public AppCompatTextView getTvErrorSubtitle() {
        return tvErrorSubtitle;
    }

    public ImageView getIvError() {
        return ivError;
    }

    public void setErrorImage(@DrawableRes int resId) {
        if (ivError != null) {
            ivError.setImageResource(resId);
        }
    }

    /**
     * 设置按钮显示隐藏
     *
     * @param visibility
     */
    public void setBtnVisible(int visibility) {
        if (tvErrorBtn != null) {
            tvErrorBtn.setVisibility(visibility);
        }
    }

    /**
     * 设置按钮的文本
     *
     * @param str
     */
    public void setBtnText(String str) {
        if (tvErrorBtn != null && NullUtil.notEmpty(str)) {
            tvErrorBtn.setText(str);
            tvErrorBtn.setVisibility(VISIBLE);
        }
    }

    /**
     * 图片点击事件
     *
     * @param listener
     */
    public void setErrorImgOnClickListener(OnClickListener listener) {
        if (ivError != null) {
            ivError.setOnClickListener(listener);
        }
    }

    /**
     * 设置按钮点击事件
     *
     * @param listener
     */
    public void setErrorBtnOnClickListener(OnClickListener listener) {
        if (tvErrorBtn != null) {
            tvErrorBtn.setOnClickListener(listener);
        }
    }

    /**
     * 设置返回键是否可见，默认不可见
     *
     * @param visibility
     */
    public void setBackVisible(int visibility) {
        if (flBack != null) {
            flBack.setVisibility(visibility);
        }
    }

    /**
     * 设置返回
     *
     * @param listener
     */
    public void setBackOnClickListener(OnClickListener listener) {
        if (flBack != null) {
            flBack.setOnClickListener(listener);
        }
    }
}
