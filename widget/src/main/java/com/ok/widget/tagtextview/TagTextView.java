package com.ok.widget.tagtextview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.ok.widget.R;
import com.xier.core.tools.ResourceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jane
 * @date 2020/11/21
 * @desc desc
 */
public class TagTextView extends AppCompatTextView {

    public static int TAGS_INDEX_AT_START = 0;
    public static int TAGS_INDEX_AT_END = 1;

    private int tagsBackgroundStyle = R.drawable.shape_textview_tags_bg;

    private int tagTextSize = 12;   //标签的字体大小
    private int tagTextColor = ResourceUtils.getColor(R.color.wt_white);    //标签的字体颜色

    private StringBuffer content_buffer;
    private Context mContext;
    private AppCompatTextView tvTag;
    private AppCompatImageView ivTag;

    private int tagsIndex = 0;      //默认标签在开始的位置

    public TagTextView(Context context) {
        super(context);
        mContext = context;
    }

    public TagTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public TagTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    /**
     * 设置标签的背景样式
     *
     * @param tagTextSize 你需要替换的tag文字字体大小
     */
    public void setTagTextSize(int tagTextSize) {
        this.tagTextSize = tagTextSize;
    }

    /**
     * 设置标签的背景样式
     *
     * @param tagTextColor 你需要替换的tag的文字颜色
     */
    public void setTagTextColor(int tagTextColor) {
        this.tagTextColor = tagTextColor;
    }

    /**
     * 设置标签的背景样式
     *
     * @param tagsBackgroundStyle 你需要替换的tag背景样式
     */
    public void setTagsBackgroundStyle(int tagsBackgroundStyle) {
        this.tagsBackgroundStyle = tagsBackgroundStyle;
    }

    /**
     * 设置标签是在头部还是尾部
     *
     * @param tagsIndex 头部还是尾部显示tag
     */
    public void setTagsIndex(int tagsIndex) {
        this.tagsIndex = tagsIndex;
    }

    /**
     * 设置标签和文字内容(单个)
     *
     * @param tag     标签内容
     * @param content 标签文字
     */
    public void setSingleTagAndContent(@NonNull String tag, String content) {
        List<String> tagList = new ArrayList<>();
        tagList.add(tag);
        setMultiTagAndContent(tagList, content);
    }

    /**
     * 设置标签和文字内容(单个)
     *
     * @param tag     标签内容
     * @param content 标签文字
     */
    public void setSingleTagAndContent(@NonNull String tag, String tagImg, String content) {
        List<String> tagList = new ArrayList<>();
        tagList.add(tag);
        List<String> tagImgList = new ArrayList<>();
        tagImgList.add(tagImg);
        setMultiTagAndContent(tagList, tagImgList, content);
    }

    /**
     * 设置标签和文字内容(多个)
     *
     * @param tags    标签内容
     * @param content 标签文字
     */
    public void setMultiTagAndContent(@NonNull List<String> tags, String content) {
        if (tagsIndex == TAGS_INDEX_AT_START) {
            setTagStart(tags, content);
        } else {
            setTagEnd(tags, content);
        }
    }

    /**
     * 设置标签和文字内容(多个)
     *
     * @param tags    标签内容
     * @param content 标签文字
     */
    public void setMultiTagAndContent(@NonNull List<String> tags, List<String> tagImgList, String content) {
        if (tagsIndex == TAGS_INDEX_AT_START) {
            setTagStart(tags, tagImgList, content);
        } else {
            setTagEnd(tags, content);
        }
    }

    /**
     * 标签显示在头部位置
     *
     * @param tags    标签内容
     * @param content 标签文字
     */
    public void setTagStart(List<String> tags, String content) {
        int endIndex = 0;
        int startIndex = 1;
        content_buffer = new StringBuffer();
        for (String item : tags) {
            content_buffer.append(item);
        }
        content_buffer.append(content);
        SpannableString spannableString = new SpannableString(content_buffer);
        for (int i = 0; i < tags.size(); i++) {
            String item = tags.get(i);
            endIndex += item.length();
            // 设置标签的布局
            View view = LayoutInflater.from(mContext).inflate(R.layout.wt_view_textview_tags, null);
            tvTag = view.findViewById(R.id.tvTag);
            tvTag.setText(item);
//            tv_tag.setTextSize(tagTextSize);
            tvTag.setTextColor(tagTextColor);
            //  设置背景样式
            tvTag.setBackgroundResource(tagsBackgroundStyle);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tvTag.getLayoutParams();
            layoutParams.setMarginEnd(ResourceUtils.getDimension(R.dimen.dp_2));

            Bitmap bitmap = convertViewToBitmap(view);
            Drawable drawable = new BitmapDrawable(bitmap);
            drawable.setBounds(0, 0, tvTag.getWidth() + ResourceUtils.getDimension(R.dimen.dp_2), tvTag.getHeight());

            CenterImageSpan span = new CenterImageSpan(drawable);
            spannableString.setSpan(span, startIndex - 1, endIndex, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            startIndex += item.length();
        }
        setText(spannableString);
        setGravity(Gravity.CENTER_VERTICAL);
    }

    /**
     * 标签显示在头部位置
     *
     * @param tags    标签内容
     * @param content 标签文字
     */
    public void setTagStart(List<String> tags, List<String> tagImgs, String content) {
        content_buffer = new StringBuffer();
        for (String item : tags) {
            content_buffer.append(item);
        }
        content_buffer.append(content);
        SpannableString spannableString = new SpannableString(content_buffer);
        for (int i = 0; i < tags.size(); i++) {
            String item = tags.get(i);
            //  设置标签的布局
            View view = LayoutInflater.from(mContext).inflate(R.layout.wt_view_textview_tags_img, null);
            LinearLayout ll = view.findViewById(R.id.view);
            tvTag = view.findViewById(R.id.tvTag);
            ivTag = view.findViewById(R.id.ivTag);
            if (tagImgs.size() > i) {
                Glide.with(getContext()).load(tagImgs.get(i)).into(ivTag);
            }
            tvTag.setText(item);
            tvTag.setTextColor(tagTextColor);
            //  设置背景样式
            ll.setBackgroundResource(tagsBackgroundStyle);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ll.getLayoutParams();
            layoutParams.setMarginEnd(ResourceUtils.getDimension(R.dimen.dp_4));

            Bitmap bitmap = convertViewToBitmap(view);
            Drawable drawable = new BitmapDrawable(bitmap);
            drawable.setBounds(0, 0, ll.getWidth() + ResourceUtils.getDimension(R.dimen.dp_4), ll.getHeight());

            ImageSpan span = new ImageSpan(drawable);
            spannableString.setSpan(span, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        }
        setText(spannableString);
        setGravity(Gravity.CENTER_VERTICAL);
    }

    /**
     * 设置图片
     *
     * @param resID   资源ID
     * @param content 文字内容
     */
    public void setTagImageStart(Context context, int resID, String content, int width, int height) {
        content_buffer = new StringBuffer("**" + content);  //  两个字符占位
        SpannableString spannableString = new SpannableString(content_buffer);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resID);
        Drawable drawable = new BitmapDrawable(bitmap);
        drawable.setBounds(0, 0, dp2px(context, width), dp2px(context, height));
        CenterImageSpan span = new CenterImageSpan(drawable);
        spannableString.setSpan(span, 0, 2, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        setText(spannableString);
        setGravity(Gravity.CENTER_VERTICAL);
    }

    /**
     * 标签显示在头部位置
     *
     * @param tags    标签内容
     * @param content 标签文字
     */
    public void setTagEnd(List<String> tags, String content) {
        content_buffer = new StringBuffer(content);
        for (String item : tags) {
            content_buffer.append(item);
        }
        SpannableString spannableString = new SpannableString(content_buffer);
        for (int i = 0; i < tags.size(); i++) {
            String item = tags.get(i);
            //  设置标签的布局
            View view = LayoutInflater.from(mContext).inflate(R.layout.wt_view_textview_tags, null);
            tvTag = view.findViewById(R.id.tv_tags);
            tvTag.setText(item);
//            tv_tag.setTextSize(tagTextSize);
            tvTag.setTextColor(tagTextColor);
            //  设置背景样式
            tvTag.setBackgroundResource(tagsBackgroundStyle);

            Bitmap bitmap = convertViewToBitmap(view);
            Drawable drawable = new BitmapDrawable(bitmap);
            drawable.setBounds(0, 0, tvTag.getWidth(), tvTag.getHeight());

            CenterImageSpan span = new CenterImageSpan(drawable);
            spannableString.setSpan(span, content_buffer.length() - item.length(), content_buffer.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }
        setText(spannableString);
        setGravity(Gravity.CENTER_VERTICAL);
    }

    /**
     * 指定位置设置标签
     *
     * @param start   开始位置从0开始
     * @param end     结束位置长度-1
     * @param content 文字内容
     */
    public void setTagAnyway(int start, int end, String content) {
        SpannableString spannableString = new SpannableString(content);
        //  设置标签的布局
        View view = LayoutInflater.from(mContext).inflate(R.layout.wt_view_textview_tags, null);
        String item = content.substring(start, end);
        tvTag = view.findViewById(R.id.tv_tags);
        tvTag.setText(item);
//        tv_tag.setTextSize(tagTextSize);
        tvTag.setTextColor(tagTextColor);
        //  设置背景样式
        tvTag.setBackgroundResource(tagsBackgroundStyle);

        Bitmap bitmap = convertViewToBitmap(view);
        Drawable drawable = new BitmapDrawable(bitmap);
        drawable.setBounds(0, 0, tvTag.getWidth(), tvTag.getHeight());

        CenterImageSpan span = new CenterImageSpan(drawable);
        spannableString.setSpan(span, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        setText(spannableString);
        setGravity(Gravity.CENTER_VERTICAL);
    }

    /**
     * Android中dp和pix互相转化
     *
     * @param dpValue dp值
     * @return
     */
    public static int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private static Bitmap convertViewToBitmap(View view) {
        view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }
}
