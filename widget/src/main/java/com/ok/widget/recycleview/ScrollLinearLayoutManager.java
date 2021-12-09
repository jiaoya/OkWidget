package com.ok.widget.recycleview;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * <pre>
 *      Copyright    : Copyright (c) 2020.
 *      Author       : jiaoya.
 *      Created Time : 2020/4/21.
 *      Desc         : 自定义LinearLayoutManage，滑动处理
 * </pre>
 */
public class ScrollLinearLayoutManager extends LinearLayoutManager {

    private boolean isScrollEnabled = true;

    public ScrollLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public ScrollLinearLayoutManager(Context context) {
        super(context);
    }

//    public ScrollLinearLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
//        super(context, spanCount, orientation, reverseLayout);
//    }


    public ScrollLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
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
