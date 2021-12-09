package com.ok.widget.recycleview;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

/**
 * <pre>
 *      Copyright    : Copyright (c) 2020.
 *      Author       : jiaoya.
 *      Created Time : 2020/9/18.
 *      Desc         : 通常、普通、常用的recycleview
 * </pre>
 */
public class ComRecyclerView extends RecyclerView {


    // 动画
    private boolean isDefaultAnimatorOpen = false;


    public ComRecyclerView(@NonNull Context context) {
        super(context);
    }

    public ComRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ComRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * 打开局部刷新动画
     */
    public void openItemAnimator() {
        isDefaultAnimatorOpen = true;
        this.getItemAnimator().setAddDuration(120);
        this.getItemAnimator().setChangeDuration(250);
        this.getItemAnimator().setMoveDuration(250);
        this.getItemAnimator().setRemoveDuration(120);
        ((SimpleItemAnimator) this.getItemAnimator()).setSupportsChangeAnimations(true);
    }

    /**
     * 关闭局部刷新动画
     */
    public void closeItemAnimator() {
        isDefaultAnimatorOpen = false;
        this.getItemAnimator().setAddDuration(0);
        this.getItemAnimator().setChangeDuration(0);
        this.getItemAnimator().setMoveDuration(0);
        this.getItemAnimator().setRemoveDuration(0);
        ((SimpleItemAnimator) this.getItemAnimator()).setSupportsChangeAnimations(false);
    }

}
