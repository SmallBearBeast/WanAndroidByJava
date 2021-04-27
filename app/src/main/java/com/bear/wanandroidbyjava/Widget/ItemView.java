package com.bear.wanandroidbyjava.Widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bear.wanandroidbyjava.R;

public class ItemView extends ConstraintLayout {
    private static final int INVALID_ID = -1;
    private ImageView iconIv;
    private TextView textTv;

    private boolean iconTintEnable = false;
    private int iconRes = INVALID_ID;
    private int iconBgRes = INVALID_ID;
    private int iconSize = dp2px(60);
    private int iconPadding = 0;
    private int iconTint = 0;
    private int offset = dp2px(10);
    private int textSize = dp2px(14);
    private int textColor = Color.parseColor("#AAAAAA");
    private String text = "";

    public ItemView(Context context) {
        this(context, null);
    }

    public ItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(attrs);
        initView();
    }

    private void initAttr(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ItemView);
        iconRes = typedArray.getResourceId(R.styleable.ItemView_iv_icon_res, iconRes);
        iconBgRes = typedArray.getResourceId(R.styleable.ItemView_iv_icon_bg_res, iconBgRes);
        iconSize = typedArray.getDimensionPixelSize(R.styleable.ItemView_iv_icon_size, iconSize);
        iconPadding = typedArray.getDimensionPixelSize(R.styleable.ItemView_iv_icon_padding, iconPadding);
        if (typedArray.hasValue(R.styleable.ItemView_iv_icon_tint)) {
            iconTintEnable = true;
            iconTint = typedArray.getColor(R.styleable.ItemView_iv_icon_tint, iconTint);
        }
        offset = typedArray.getDimensionPixelOffset(R.styleable.ItemView_iv_offset, offset);
        textSize = typedArray.getDimensionPixelOffset(R.styleable.ItemView_iv_text_size, textSize);
        textColor = typedArray.getColor(R.styleable.ItemView_iv_text_color, textColor);
        text = typedArray.getString(R.styleable.ItemView_iv_text_str);
        typedArray.recycle();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_item, this);
        initIconIv();
        initTextTv();
    }

    private void initIconIv() {
        iconIv = findViewById(R.id.iconIv);
        setIconSize(iconSize);
        setIconRes(iconRes);
        setIconBgRes(iconBgRes);
        setIconPadding(iconPadding);
        if (iconTintEnable) {
            setIconTint(iconTint);
        }
    }

    private void initTextTv() {
        textTv = findViewById(R.id.textTv);
        setOffset(offset);
        setText(text);
        setTextSize(textSize);
        setTextColor(textColor);
    }

    private int dp2px(float dpValue) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5F);
    }

    public ImageView getIconIv() {
        return iconIv;
    }

    public TextView getTextTv() {
        return textTv;
    }

    public void setIconRes(int iconRes) {
        if (iconRes != INVALID_ID) {
            this.iconRes = iconRes;
            iconIv.setImageResource(iconRes);
        }
    }

    public void setIconBgRes(int iconBgRes) {
        if (iconBgRes != INVALID_ID) {
            this.iconBgRes = iconBgRes;
            iconIv.setBackgroundResource(iconBgRes);
        }
    }

    public void setIconSize(int iconSize) {
        if (iconSize >= 0 || iconSize == ViewGroup.LayoutParams.MATCH_PARENT
                || iconSize == ViewGroup.LayoutParams.WRAP_CONTENT) {
            this.iconSize = iconSize;
            LayoutParams lp = (LayoutParams) iconIv.getLayoutParams();
            lp.width = iconSize;
            lp.height = iconSize;
            iconIv.setLayoutParams(lp);
        }
    }

    public void setIconPadding(int iconPadding) {
        this.iconPadding = iconPadding;
        iconIv.setPadding(iconPadding, iconPadding, iconPadding, iconPadding);
    }

    public void setIconTint(@ColorInt int iconTint) {
        this.iconTint = iconTint;
        iconIv.setImageTintList(ColorStateList.valueOf(iconTint));
    }

    public void setOffset(int offset) {
        if (offset >= 0) {
            this.offset = offset;
            LayoutParams lp = (LayoutParams) textTv.getLayoutParams();
            lp.topMargin = offset;
            textTv.setLayoutParams(lp);
        }
    }

    public void setTextSize(int textSize) {
        if (textSize >= 0) {
            this.textSize = textSize;
            textTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }
    }

    public void setTextColor(@ColorInt int textColor) {
        this.textColor = textColor;
        textTv.setTextColor(textColor);
    }

    public void setText(String text) {
        this.text = text;
        textTv.setText(text);
    }
}
