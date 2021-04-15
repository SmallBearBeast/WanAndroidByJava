package com.bear.wanandroidbyjava.Tool.Case;

import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;

public abstract class CaseInfo {
    protected static final int NONE_ID = -1;
    @DrawableRes
    protected int cover() {
        return NONE_ID;
    }

    @StringRes
    protected int title() {
        return NONE_ID;
    }

    @StringRes
    protected int description() {
        return NONE_ID;
    }

    @StringRes
    protected int clickText() {
        return NONE_ID;
    }

    @LayoutRes
    protected int layoutId() {
        return NONE_ID;
    }

    protected boolean showProgress() {
        return false;
    }

    @CaseHelper.Type
    protected abstract int type();
}
