package com.ok.widget.recycleview;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.GridLayoutManager;

/**
 * <pre>
 *      Copyright    : Copyright (c) 2020.
 *      Author       : jiaoya.
 *      Created Time : 2020/4/21.
 *      Desc         : 自定义GridLayoutManager，滑动处理
 * </pre>
 */
public class ScrollGridLayoutManager extends GridLayoutManager {

    private boolean isScrollEnabled = true;

    public ScrollGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public ScrollGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public ScrollGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    /**
     * 设置是否滑动
     *
     * @param flag
     */
    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    public boolean isScrollEnabled() {
        return isScrollEnabled;
    }

    @Override
    public boolean canScrollVertically() {
        return isScrollEnabled && super.canScrollVertically();
    }

}
