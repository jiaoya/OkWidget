package com.ok.widget.viewpage;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * @author Jane
 * @date 2020-02-11
 * @desc
 */
public class NoScrollWrapContentHeightViewPager extends NoScrollViewPager {

    private int currentIndex;
    private int height = 0;
    //保存view对应的索引
    private HashMap<Integer, View> mChildrenViews = new LinkedHashMap<Integer, View>();

    private int minHeight;

    public NoScrollWrapContentHeightViewPager(@NonNull Context context) {
        super(context);
    }

    public NoScrollWrapContentHeightViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置最小高度
     *
     * @param minHeight
     */
    public void setMinHeight(int minHeight) {
        this.minHeight = minHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mChildrenViews.size() > currentIndex) {
            View child = mChildrenViews.get(currentIndex);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            height = child.getMeasuredHeight();
        }
        if (minHeight > 0 && height < minHeight) {
            height = minHeight;
        }
        if (mChildrenViews.size() != 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 重新设置高度
     *
     * @param current
     */
    public void resetHeight(int current) {
        try {
            currentIndex = current;
            if (mChildrenViews.size() > current) {
                ViewGroup.LayoutParams layoutParams;
                if (getLayoutParams() instanceof LinearLayout.LayoutParams) {
                    layoutParams = (LinearLayout.LayoutParams) getLayoutParams();
                } else if (getLayoutParams() instanceof FrameLayout.LayoutParams) {
                    layoutParams = (FrameLayout.LayoutParams) getLayoutParams();
                } else {
                    layoutParams = getLayoutParams();
                }
                if (layoutParams == null) {
                    layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
                } else {
                    layoutParams.height = height;
                }
                setLayoutParams(layoutParams);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存View对应的索引,需要自适应高度的一定要设置这个
     */
    public void setViewForPosition(View view, int position) {
        mChildrenViews.put(position, view);
    }
}
