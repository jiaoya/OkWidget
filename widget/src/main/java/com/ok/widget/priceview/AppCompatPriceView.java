package com.ok.widget.priceview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.ok.widget.R;
import com.xier.core.tools.NullUtil;
import com.xier.core.tools.NumberUtils;

import java.math.BigDecimal;

/**
 * @author Jane
 * @date 2020/7/29
 * @desc 商城用的价格View
 */
public class AppCompatPriceView extends FrameLayout {
    View inflate;

    private LinearLayout view;
    private LinearLayout viewLeft;
    private AppCompatImageView imgIntegral;
    private AppCompatTextView tvNegative; //负号，字体颜色和大小与tvPrice一致
    private AppCompatTextView tvUnit;
    private AppCompatTextView tvPrice;
    private AppCompatTextView tvOldPrice;

    // 积分+钱
    private AppCompatTextView tvIntegralUnit;       // 积分
    private AppCompatTextView tvIntegralPlus;       // + 号
    private AppCompatTextView tvIntegralPrice;      // 金额

    /**
     * 价格单位（目前只有人民币）
     */
    String unit;
    /**
     * 显示价格
     */
    double price = 0;
    /**
     * 划线价格
     */
    double oldPrice;
    /**
     * 显示价格的字体颜色
     */
    int priceTvColor;
    /**
     * 划线价的字体颜色
     */
    int oldPriceTvColor;
    /**
     * 单位字体颜色
     */
    float unitTvSize;
    /**
     * 显示价格的字体大小
     */
    float priceTvSize;
    /**
     * 显示价格的小数位字体大小
     */
    float pricePointTvSize;
    /**
     * 划线价的字体大小
     */
    float oldPriceTvSize;
    /**
     * 两个价格之间的间距
     */
    float unitTvMargin;
    /**
     * 两个价格之间的间距（仅横向布局生效）
     */
    float priceTvMargin;
    /**
     * 显示价格，整数与小数字体大小的倍数关系
     */
    float priceTransition;
    /**
     * 显示金额和划线金额的显示方向（0横向、1竖向）
     */
    int orientation = 0;
    /**
     * 价格是否加粗
     */
    boolean priceTextBold = false;

    /**
     * 显示价格（左边的价格）
     */
    boolean showLeftPrice = true;
    /**
     * 显示负号（在羊角符之前）
     * 字体颜色和大小与tvPrice一致
     */
    boolean showLeftNegative = false;

    // ---------------------------------------积分相关属性---------------------------------------
    /**
     * 积分
     */
    int integral;
    /**
     * 积分
     */
    boolean isIntegral;
    /**
     * 积分单位
     */
    String integralUnit;
    /**
     * 积分图标
     */
    int integralSrc;
    /**
     * 积分单位字体大小
     */
    float integralUnitTextSize;
    /**
     * 积分单位字体颜色
     */
    int integralUnitTextColor;
    /**
     * 积分+钱，+号字体大小
     */
    float plusTvSize;
    /**
     * 积分+钱，+号字体颜色
     */
    int plusTextColor;
    /**
     * 显示积分和附加金额的字体大小（加分+钱）
     */
    float integralPriceTextSize;
    /**
     * 显示积分和附加金额的字体颜色（加分+钱），默认与主积分字体颜色一致
     */
    int integralPriceTextColor;
    /**
     * 积分单位左间距
     */
    float integralUnitLeftMargin;
    /**
     * 积分+钱，+号左间距
     */
    float plusLeftMargin;
    /**
     * 积分+钱，金额左间距
     */
    float integralPriceLeftMargin;

    /**
     * 组件初始化，各属性设置默认值
     *
     * @param context
     */
    public AppCompatPriceView(Context context) {
        super(context);
        unit = "¥";
        integralUnit = "积分";
        price = 0;
        oldPrice = 0;
        integral = 0;
        isIntegral = false;
        integralSrc = 0;
        priceTvColor = ContextCompat.getColor(getContext(), R.color.wt_FF2442);
        oldPriceTvColor = ContextCompat.getColor(getContext(), R.color.wt_999999);
        unitTvSize = getResources().getDimension(R.dimen.dp_15);
        priceTvSize = getResources().getDimension(R.dimen.dp_15);
        pricePointTvSize = 0;
        integralPriceTextSize = 0;
        integralPriceTextColor = ContextCompat.getColor(getContext(), R.color.wt_FF2442);
        plusTvSize = 0;
        plusTextColor = ContextCompat.getColor(getContext(), R.color.wt_FF2442);
        oldPriceTvSize = getResources().getDimension(R.dimen.dp_12);
        unitTvMargin = getResources().getDimension(R.dimen.dp_2);
        priceTvMargin = getResources().getDimension(R.dimen.dp_6);
        priceTransition = 0;
        priceTextBold = false;
        showLeftPrice = true;
        showLeftNegative = false;
        integralUnitLeftMargin = getResources().getDimension(R.dimen.dp_2);
        plusLeftMargin = getResources().getDimension(R.dimen.dp_2);
        integralPriceLeftMargin = getResources().getDimension(R.dimen.dp_2);
        integralUnitTextSize = 0;
        integralUnitTextColor = ContextCompat.getColor(getContext(), R.color.wt_FF2442);
        init();
    }

    /**
     * 组件初始化，各属性设置默认值
     *
     * @param context
     * @param attrs
     */
    public AppCompatPriceView(Context context, AttributeSet attrs) {
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
    public AppCompatPriceView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AppCompatPriceView, defStyleAttr, 0);
        unit = typedArray.getString(R.styleable.AppCompatPriceView_unit);
        integralUnit = typedArray.getString(R.styleable.AppCompatPriceView_integralUnit);
        price = typedArray.getFloat(R.styleable.AppCompatPriceView_price, 0);
        oldPrice = typedArray.getFloat(R.styleable.AppCompatPriceView_oldPrice, 0);
        integral = typedArray.getInt(R.styleable.AppCompatPriceView_integral, 0);
        isIntegral = typedArray.getBoolean(R.styleable.AppCompatPriceView_isIntegral, false);
        integralSrc = typedArray.getInt(R.styleable.AppCompatPriceView_integralSrc, 0);
        priceTvColor = typedArray.getColor(R.styleable.AppCompatPriceView_priceTextColor, ContextCompat.getColor(context, R.color.wt_FF2442));
        oldPriceTvColor = typedArray.getColor(R.styleable.AppCompatPriceView_oldPriceTextColor, ContextCompat.getColor(context, R.color.wt_999999));
        priceTvSize = typedArray.getDimension(R.styleable.AppCompatPriceView_priceTextSize, getResources().getDimension(R.dimen.dp_15));
        pricePointTvSize = typedArray.getDimension(R.styleable.AppCompatPriceView_pricePointTextSize, 0);
        unitTvSize = typedArray.getDimension(R.styleable.AppCompatPriceView_unitTextSize, getResources().getDimension(R.dimen.dp_15));
        integralPriceTextSize = typedArray.getDimension(R.styleable.AppCompatPriceView_integralPriceTextSize, 0);
        integralPriceTextColor = typedArray.getColor(R.styleable.AppCompatPriceView_integralPriceTextColor, ContextCompat.getColor(context, R.color.wt_FF2442));
        plusTextColor = typedArray.getColor(R.styleable.AppCompatPriceView_integralPriceTextColor, ContextCompat.getColor(context, R.color.wt_FF2442));
        plusTvSize = typedArray.getDimension(R.styleable.AppCompatPriceView_plusTextSize, 0);
        oldPriceTvSize = typedArray.getDimension(R.styleable.AppCompatPriceView_oldPriceTextSize, getResources().getDimension(R.dimen.dp_12));
        unitTvMargin = typedArray.getDimension(R.styleable.AppCompatPriceView_unitMargin, getResources().getDimension(R.dimen.dp_2));
        priceTvMargin = typedArray.getDimension(R.styleable.AppCompatPriceView_priceTextMargin, getResources().getDimension(R.dimen.dp_6));
        integralUnitLeftMargin = typedArray.getDimension(R.styleable.AppCompatPriceView_integralUnitLeftMargin, getResources().getDimension(R.dimen.dp_2));
        plusLeftMargin = typedArray.getDimension(R.styleable.AppCompatPriceView_plusLeftMargin, getResources().getDimension(R.dimen.dp_2));
        integralPriceLeftMargin = typedArray.getDimension(R.styleable.AppCompatPriceView_integralPriceLeftMargin, getResources().getDimension(R.dimen.dp_2));
        priceTransition = typedArray.getFloat(R.styleable.AppCompatPriceView_priceTransition, 0);
        orientation = typedArray.getInt(R.styleable.AppCompatPriceView_orientation, 0);
        priceTextBold = typedArray.getBoolean(R.styleable.AppCompatPriceView_priceTextBold, false);
        showLeftPrice = typedArray.getBoolean(R.styleable.AppCompatPriceView_showLeftPrice, true);
        showLeftNegative = typedArray.getBoolean(R.styleable.AppCompatPriceView_showLeftNegative, false);
        integralUnitTextSize = typedArray.getDimension(R.styleable.AppCompatPriceView_integralUnitTextSize, 0);
        integralUnitTextColor = typedArray.getColor(R.styleable.AppCompatPriceView_integralUnitTextColor, ContextCompat.getColor(getContext(), R.color.wt_FF2442));
    }

    /**
     * 组件初始化，并赋值
     */
    private void init() {

        inflate = inflate(getContext(), R.layout.wt_view_price, this);

        view = inflate.findViewById(R.id.view);
        viewLeft = inflate.findViewById(R.id.viewLeft);
        imgIntegral = inflate.findViewById(R.id.imgIntegral);
        tvNegative = inflate.findViewById(R.id.tvNegative);
        tvUnit = inflate.findViewById(R.id.tvUnit);
        tvPrice = inflate.findViewById(R.id.tvPrice);
        tvOldPrice = inflate.findViewById(R.id.tvOldPrice);

        tvIntegralUnit = inflate.findViewById(R.id.tvIntegralUnit);
        tvIntegralPlus = inflate.findViewById(R.id.tvIntegralPlus);
        tvIntegralPrice = inflate.findViewById(R.id.tvIntegralPrice);

        if (!NullUtil.notEmpty(unit)) {
            unit = "¥";
        }

        if (integral > 0) {
            isIntegral = true;
        } else {
            isIntegral = false;
        }

        if (integralSrc != 0) {
            imgIntegral.setImageResource(integralSrc);
            imgIntegral.setVisibility(VISIBLE);
        } else {
            imgIntegral.setVisibility(GONE);
        }

        if (integralUnit != null && integralUnit.length() > 0) {
            tvIntegralUnit.setText(integralUnit);
        }

        if (isIntegral) {
            tvPrice.setText(integral + "");
            tvIntegralUnit.setVisibility(integralSrc > 0 ? GONE : VISIBLE);
            if (price > 0) {
                tvIntegralPrice.setText(unit + removeZero(price));
                tvIntegralPrice.setVisibility(VISIBLE);
                tvIntegralPlus.setVisibility(VISIBLE);
//                tvPrice.setText(integral + (integralSrc > 0? "+" : "积分+") + unit + removeZero(price) + "");
            } else {
//                tvPrice.setText(integral + (integralSrc > 0? "" : "积分"));
                tvIntegralPrice.setVisibility(GONE);
                tvIntegralPlus.setVisibility(GONE);
            }
            tvUnit.setVisibility(GONE);
        } else {
            tvPrice.setText(removeZero(price) + "");
            tvIntegralUnit.setVisibility(GONE);
            tvIntegralPrice.setVisibility(GONE);
            tvIntegralPlus.setVisibility(GONE);
            tvUnit.setVisibility(VISIBLE);
        }

        if (oldPrice > 0) {
            tvOldPrice.setText(unit + removeZero(oldPrice) + "");
            tvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            tvOldPrice.setVisibility(VISIBLE);
        } else {
            tvOldPrice.setVisibility(GONE);
        }

        if (priceTvColor != 0) {
            tvPrice.setTextColor(priceTvColor);
            tvNegative.setTextColor(priceTvColor);
            tvUnit.setTextColor(priceTvColor);
        }

        if (oldPriceTvColor != 0) {
            tvOldPrice.setTextColor(oldPriceTvColor);
        }

        if (unitTvSize != 0) {
            if (unitTvSize > priceTvSize) {
                tvUnit.setTextSize(TypedValue.COMPLEX_UNIT_PX, priceTvSize);
            } else {
                tvUnit.setTextSize(TypedValue.COMPLEX_UNIT_PX, unitTvSize);
            }

            if (unitTvSize < priceTvSize) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tvUnit.getLayoutParams();
                layoutParams.setMargins(0, (int) unitTvMargin, 0, (int) getResources().getDimension(R.dimen.dp_0));
            }
        } else {
            tvUnit.setTextSize(TypedValue.COMPLEX_UNIT_PX, priceTvSize);
        }

        if (priceTvSize != 0) {
            tvPrice.setTextSize(TypedValue.COMPLEX_UNIT_PX, priceTvSize);
            tvNegative.setTextSize(TypedValue.COMPLEX_UNIT_PX, priceTvSize);
        }

        if (priceTvSize > getResources().getDimension(R.dimen.dp_20)) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tvPrice.getLayoutParams();
            layoutParams.setMargins(0, 0, 0, (int) getResources().getDimension(R.dimen.dp_m_3));
        } else {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tvPrice.getLayoutParams();
            layoutParams.setMargins(0, 0, 0, 0);
        }

        if (pricePointTvSize != 0 || integralPriceTextSize != 0) {
            setTextSize();
        }

        if (oldPriceTvSize != 0) {
            tvOldPrice.setTextSize(TypedValue.COMPLEX_UNIT_PX, oldPriceTvSize);
        }

        if (unitTvMargin != 0) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tvUnit.getLayoutParams();
            layoutParams.setMarginEnd((int) unitTvMargin);
            tvUnit.setLayoutParams(layoutParams);
        }

        if (priceTvMargin != 0 && orientation == 0) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tvOldPrice.getLayoutParams();
            layoutParams.setMarginStart((int) priceTvMargin);
            tvOldPrice.setLayoutParams(layoutParams);
        }

        if (priceTransition != 0) {
            float ps = tvPrice.getTextSize();
            tvOldPrice.setTextSize(ps * priceTransition);
        }

        if (orientation == 0) {
            view.setOrientation(LinearLayout.HORIZONTAL);
        } else {
            view.setOrientation(LinearLayout.VERTICAL);
        }

        tvPrice.getPaint().setFakeBoldText(priceTextBold);
        tvNegative.getPaint().setFakeBoldText(priceTextBold);
        tvUnit.getPaint().setFakeBoldText(priceTextBold);

        if (showLeftNegative) {
            tvNegative.setVisibility(VISIBLE);
        } else {
            tvNegative.setVisibility(GONE);
        }

        if (showLeftPrice) {
            viewLeft.setVisibility(VISIBLE);
        } else {
            viewLeft.setVisibility(GONE);
        }

        // ---------------------------------------积分相关属性---------------------------------------------------

        if (integralUnitTextColor != 0) {
            tvIntegralUnit.setTextColor(integralUnitTextColor);
        }

        if (integralUnitTextSize != 0) {
            tvIntegralUnit.setTextSize(TypedValue.COMPLEX_UNIT_PX, integralUnitTextSize);
        }

        if (plusTextColor != 0) {
            tvIntegralPlus.setTextColor(plusTextColor);
        }

        if (plusTvSize != 0) {
            tvIntegralPlus.setTextSize(TypedValue.COMPLEX_UNIT_PX, plusTvSize);
        }

        if (integralPriceTextColor != 0) {
            tvIntegralPrice.setTextColor(integralPriceTextColor);
        }

        if (integralPriceTextSize != 0) {
            tvIntegralPrice.setTextSize(TypedValue.COMPLEX_UNIT_PX, integralPriceTextSize);
        }

        if (integralUnitLeftMargin != 0) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tvIntegralUnit.getLayoutParams();
            layoutParams.setMarginStart((int) integralUnitLeftMargin);
            tvIntegralUnit.setLayoutParams(layoutParams);

            LinearLayout.LayoutParams imgLayoutParams = (LinearLayout.LayoutParams) imgIntegral.getLayoutParams();
            imgLayoutParams.setMarginStart((int) integralUnitLeftMargin);
            imgIntegral.setLayoutParams(imgLayoutParams);
        }

        if (plusLeftMargin != 0) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tvIntegralPlus.getLayoutParams();
            layoutParams.setMarginStart((int) plusLeftMargin);
            tvIntegralPlus.setLayoutParams(layoutParams);
        }

        if (integralPriceLeftMargin != 0) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tvIntegralPrice.getLayoutParams();
            layoutParams.setMarginStart((int) integralPriceLeftMargin);
            tvIntegralPrice.setLayoutParams(layoutParams);
        }

        tvIntegralUnit.getPaint().setFakeBoldText(priceTextBold);
        tvIntegralPrice.getPaint().setFakeBoldText(priceTextBold);
        tvIntegralPlus.getPaint().setFakeBoldText(priceTextBold);
    }

    /**
     * 设置单位
     */
    public void setUnit(String unit) {
        if (!NullUtil.notEmpty(unit)) {
            unit = "¥";
        }
        this.unit = unit;
        tvUnit.setText(unit);
        tvOldPrice.setText(unit + removeZero(oldPrice) + "");
    }

    /**
     * 设置显示金额
     */
    public void setPrice(double price) {
        this.price = price;
        this.isIntegral = false;
        tvIntegralUnit.setVisibility(GONE);
        tvIntegralPrice.setVisibility(GONE);
        tvIntegralPlus.setVisibility(GONE);
        tvUnit.setVisibility(VISIBLE);
        tvPrice.setText(removeZero(price) + "");
        setTextSize();
    }

    /**
     * 设置显示划线金额
     */
    public void setOldPrice(double oldPrice) {
        this.oldPrice = oldPrice;
        if (oldPrice > 0) {
            tvOldPrice.setText(unit + removeZero(oldPrice) + "");
            tvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            tvOldPrice.setVisibility(VISIBLE);
        } else {
            tvOldPrice.setVisibility(GONE);
        }
    }

    /**
     * 设置显示金额
     *
     * @param price    当前价格
     * @param oldPrice 划线价
     */
    public void setPrice(double price, double oldPrice) {
        this.price = price;
        this.oldPrice = oldPrice;
        tvPrice.setText(removeZero(price) + "");
        if (oldPrice > 0) {
            tvOldPrice.setText(unit + removeZero(oldPrice) + "");
            tvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            tvOldPrice.setVisibility(VISIBLE);
        } else {
            tvOldPrice.setVisibility(GONE);
        }
        setTextSize();
    }

    /**
     * 设置积分
     *
     * @param integral
     */
    public void setIntegral(int integral) {
        this.integral = integral;
        if (integral > 0) {
            this.isIntegral = true;
        } else {
            this.isIntegral = false;
        }
        if (isIntegral) {
            tvPrice.setText(integral + "");
            tvIntegralUnit.setVisibility(integralSrc > 0 ? GONE : VISIBLE);
            if (price > 0) {
                tvIntegralPrice.setText(unit + removeZero(price));
                tvIntegralPrice.setVisibility(VISIBLE);
                tvIntegralPlus.setVisibility(VISIBLE);
            } else {
                tvIntegralPrice.setVisibility(GONE);
                tvIntegralPlus.setVisibility(GONE);
            }
            tvUnit.setVisibility(GONE);
        } else {
            tvPrice.setText(removeZero(price) + "");
            tvIntegralUnit.setVisibility(GONE);
            tvIntegralPrice.setVisibility(GONE);
            tvIntegralPlus.setVisibility(GONE);
            tvUnit.setVisibility(VISIBLE);
        }

        if (integralPriceTextSize != 0) {
            setTextSize();
        }
    }

    /**
     * 积分，价格
     *
     * @param integral
     * @param price
     */
    public void setIntegral(int integral, double price) {
        this.integral = integral;
        this.price = price;
        if (integral > 0) {
            this.isIntegral = true;
        } else {
            this.isIntegral = false;
        }
        if (isIntegral) {
            tvPrice.setText(integral + "");
            tvIntegralUnit.setVisibility(integralSrc > 0 ? GONE : VISIBLE);
            if (price > 0) {
                tvIntegralPrice.setText(unit + removeZero(price));
                tvIntegralPrice.setVisibility(VISIBLE);
                tvIntegralPlus.setVisibility(VISIBLE);
            } else {
                tvIntegralPrice.setVisibility(GONE);
                tvIntegralPlus.setVisibility(GONE);
            }
            tvUnit.setVisibility(GONE);
        } else {
            tvPrice.setText(removeZero(price) + "");
            tvIntegralUnit.setVisibility(GONE);
            tvIntegralPrice.setVisibility(GONE);
            tvIntegralPlus.setVisibility(GONE);
            tvUnit.setVisibility(VISIBLE);
        }
        if (integralPriceTextSize != 0) {
            setTextSize();
        }
    }

    /**
     * 设置是否展示积分
     *
     * @param isIntegral
     */
    public void setIsIntegral(boolean isIntegral) {
        this.isIntegral = isIntegral;
        if (isIntegral) {
            tvPrice.setText(integral + "");
            tvIntegralUnit.setVisibility(integralSrc > 0 ? GONE : VISIBLE);
            if (price > 0) {
                tvIntegralPrice.setText(unit + removeZero(price));
                tvIntegralPrice.setVisibility(VISIBLE);
                tvIntegralPlus.setVisibility(VISIBLE);
            } else {
                tvIntegralPrice.setVisibility(GONE);
                tvIntegralPlus.setVisibility(GONE);
            }
            tvUnit.setVisibility(GONE);
        } else {
            tvPrice.setText(removeZero(price) + "");
            tvIntegralUnit.setVisibility(GONE);
            tvIntegralPrice.setVisibility(GONE);
            tvIntegralPlus.setVisibility(GONE);
            tvUnit.setVisibility(VISIBLE);
        }
        if (integralPriceTextSize != 0) {
            setTextSize();
        }
    }

    /**
     * 设置显示单位的字体大小
     *
     * @param unitTvSize
     */
    public void setUnitTvSize(float unitTvSize) {
        if (unitTvSize != 0) {
            this.unitTvSize = unitTvSize;
            tvUnit.setTextSize(TypedValue.COMPLEX_UNIT_PX, unitTvSize);
        }
    }

    /**
     * 设置显示金额的字体大小
     *
     * @param priceTvSize
     */
    public void setPriceTvSize(float priceTvSize) {
        if (priceTvSize != 0) {
            this.priceTvSize = priceTvSize;
            tvPrice.setTextSize(TypedValue.COMPLEX_UNIT_PX, priceTvSize);
            tvNegative.setTextSize(TypedValue.COMPLEX_UNIT_PX, priceTvSize);

            if (unitTvSize == 0) {
                tvUnit.setTextSize(TypedValue.COMPLEX_UNIT_PX, priceTvSize);
            }

            if (integralUnitTextSize == 0) {
                tvIntegralUnit.setTextSize(TypedValue.COMPLEX_UNIT_PX, priceTvSize);
            }

            if (plusTvSize == 0) {
                tvIntegralPlus.setTextSize(TypedValue.COMPLEX_UNIT_PX, priceTvSize);
            }

            if (integralPriceTextSize == 0) {
                tvIntegralPrice.setTextSize(TypedValue.COMPLEX_UNIT_PX, priceTvSize);
            }

            if (pricePointTvSize != 0) {
                setTextSize();
            }
        }
    }

    /**
     * 设置显示金额的单位和小数位字体大小
     *
     * @param integtalPriceTextSize
     */
    public void setIntegralPriceTextSize(float integtalPriceTextSize) {
        if (integtalPriceTextSize != 0) {
            this.integralPriceTextSize = integtalPriceTextSize;
            setTextSize();
        }
    }

    /**
     * 设置划线金额的字体大小
     *
     * @param oldPriceTvSize
     */
    public void setOldPriceTvSize(float oldPriceTvSize) {
        if (oldPriceTvSize != 0) {
            this.oldPriceTvSize = oldPriceTvSize;
            tvOldPrice.setTextSize(TypedValue.COMPLEX_UNIT_PX, oldPriceTvSize);
        }
    }

    /**
     * 设置显示金额的字体颜色
     *
     * @param priceTvColor
     */
    public void setPriceTvColor(int priceTvColor) {
        if (priceTvColor != 0) {
            this.priceTvColor = priceTvColor;
            tvPrice.setTextColor(priceTvColor);
            tvNegative.setTextColor(priceTvColor);
            tvUnit.setTextColor(priceTvColor);

            //if (integralUnitTextColor == 0) {
            tvIntegralUnit.setTextColor(priceTvColor);
            //}

            //if (plusTextColor == 0) {
            tvIntegralPlus.setTextColor(priceTvColor);
            //}

            //if (integralPriceTextColor == 0) {
            tvIntegralPrice.setTextColor(priceTvColor);
            //}
        }
    }

    /**
     * 设置划线价的字体颜色
     *
     * @param oldPriceTvColor
     */
    public void setOldPriceTvColor(int oldPriceTvColor) {
        if (oldPriceTvColor != 0) {
            this.oldPriceTvColor = oldPriceTvColor;
            tvOldPrice.setTextColor(oldPriceTvColor);
        }
    }

    /**
     * @param unitTvMargin
     */
    public void setUnitTvMargin(float unitTvMargin) {
        if (unitTvMargin != 0) {
            this.unitTvMargin = unitTvMargin;
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tvUnit.getLayoutParams();
            layoutParams.setMarginEnd((int) unitTvMargin);
            tvUnit.setLayoutParams(layoutParams);
        }
    }

    /**
     * @param priceTvMargin
     */
    public void setPriceTvMargin(float priceTvMargin) {
        if (priceTvMargin != 0) {
            this.priceTvMargin = priceTvMargin;
            if (orientation == 0) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tvOldPrice.getLayoutParams();
                layoutParams.setMarginStart((int) priceTvMargin);
                tvOldPrice.setLayoutParams(layoutParams);
            }
        }
    }

    /**
     * 设置显示金额和划线金额的额比例
     *
     * @param priceTransition
     */
    public void setPriceTransition(float priceTransition) {
        if (priceTransition != 0) {
            this.priceTransition = priceTransition;
            float ps = tvPrice.getTextSize();
            tvOldPrice.setTextSize(ps * priceTransition);
        }
    }

    /**
     * 设置显示金额和划线金额的排列方向
     *
     * @param orientation
     */
    public void setOrientation(int orientation) {
        this.orientation = orientation;
        if (orientation == 0) {
            view.setOrientation(LinearLayout.HORIZONTAL);
        } else {
            view.setOrientation(LinearLayout.VERTICAL);
        }
        if (orientation == 0) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tvOldPrice.getLayoutParams();
            layoutParams.setMarginStart((int) priceTvMargin);
            tvOldPrice.setLayoutParams(layoutParams);
        }
    }

    /**
     * 显示价格（左边的价格）
     */
    public void showPrice(boolean showPrice) {
        this.showLeftPrice = showPrice;
        if (showPrice) {
            viewLeft.setVisibility(VISIBLE);
        } else {
            viewLeft.setVisibility(GONE);
        }
    }

    /**
     * 显示价格左边的负号（左边的价格）
     */
    public void showLeftNegative(boolean showLeftNegative) {
        this.showLeftNegative = showLeftNegative;
        if (showLeftNegative) {
            tvNegative.setVisibility(VISIBLE);
            tvNegative.setTextSize(TypedValue.COMPLEX_UNIT_PX, priceTvSize);
            tvNegative.setTextColor(priceTvColor);
            tvNegative.getPaint().setFakeBoldText(priceTextBold);
        } else {
            tvNegative.setVisibility(GONE);
        }
    }

    /**
     * 设置价格加粗
     *
     * @param priceTextBold
     */
    public void setPriceTextBold(boolean priceTextBold) {
        this.priceTextBold = priceTextBold;
        tvPrice.getPaint().setFakeBoldText(priceTextBold);
        tvNegative.getPaint().setFakeBoldText(priceTextBold);
        tvUnit.getPaint().setFakeBoldText(priceTextBold);

        tvIntegralUnit.getPaint().setFakeBoldText(priceTextBold);
        tvIntegralPrice.getPaint().setFakeBoldText(priceTextBold);
        tvIntegralPlus.getPaint().setFakeBoldText(priceTextBold);
    }

    private void setTextSize() {
        if (isIntegral) {
            if (integralPriceTextSize != 0) {
                tvIntegralPrice.setTextSize(TypedValue.COMPLEX_UNIT_PX, integralPriceTextSize);
            }
            if (plusTvSize != 0) {
                tvIntegralPlus.setTextSize(TypedValue.COMPLEX_UNIT_PX, plusTvSize);
            }
            if (integralUnitTextSize != 0) {
                tvIntegralUnit.setTextSize(TypedValue.COMPLEX_UNIT_PX, integralUnitTextSize);
            }
        } else {
            String resText = tvPrice.getText().toString();
            if (tvPrice != null && pricePointTvSize != 0 && priceTvSize != 0) {
                if (!NullUtil.notEmpty(resText)) {
                    resText = tvPrice.getText().toString();
                }
                SpannableString sStr = new SpannableString(resText);
                if (resText.contains(".")) {
                    int pointIndex = resText.indexOf(".");
                    if (pointIndex == resText.length() - 1) {
                        // 没有小数，只有末尾一个小数点（正常情况下不存在此情况）
                        sStr.setSpan(new AbsoluteSizeSpan((int) pricePointTvSize), resText.length() - 1, resText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else if (pointIndex == resText.length() - 2) {
                        // 一位小数
                        sStr.setSpan(new AbsoluteSizeSpan((int) pricePointTvSize), resText.length() - 2, resText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else if (pointIndex == resText.length() - 3) {
                        // 两位小数
                        sStr.setSpan(new AbsoluteSizeSpan((int) pricePointTvSize), resText.length() - 3, resText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
                tvPrice.setText(sStr);
            }
        }
    }

    /**
     * 数字 去零
     *
     * @param value
     * @return
     */
    public static String removeZero(double value) {
        value = NumberUtils.str2Db(k2Dec(value));
        try {
            String a = BigDecimal.valueOf(value).stripTrailingZeros().toPlainString();
            return a;
        } catch (Exception e) {
            return value + "";
        }
    }

    /**
     * 保留两位小数
     *
     * @param value
     * @return
     */
    public static String k2Dec(double value) {
        if (value == 0) {
            return "0.00";
        }
        return String.format("%.2f", value);
    }

}
