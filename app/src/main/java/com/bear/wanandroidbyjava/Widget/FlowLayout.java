package com.bear.wanandroidbyjava.Widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.bear.wanandroidbyjava.R;

import java.util.ArrayList;
import java.util.List;

public class FlowLayout extends ViewGroup {
    private int mVerticalSpace;
    private int mHorizontalSpace;
    private List<View> mChildViewList = new ArrayList<>();
    private OnFlowClickListener mFlowClickListener;

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
        mVerticalSpace = typedArray.getDimensionPixelOffset(R.styleable.FlowLayout_fl_vertical_space, 0);
        mHorizontalSpace = typedArray.getDimensionPixelOffset(R.styleable.FlowLayout_fl_horizontal_space, 0);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();
        int widthSize = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        int useHeight = 0;
        int useWidth = 0;
        int lineMaxHeight = 0;
        int marginWidth = 0;
        int marginHeight = 0;
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() == View.GONE) {
                continue;
            }
            if (childView.getLayoutParams() instanceof MarginLayoutParams) {
                measureChildWithMargins(childView, widthMeasureSpec, 0, heightMeasureSpec, 0);
                MarginLayoutParams marginLp = (MarginLayoutParams) childView.getLayoutParams();
                marginHeight = marginLp.topMargin + marginLp.bottomMargin;
                marginWidth = marginLp.leftMargin + marginLp.rightMargin;
            } else {
                measureChild(childView, widthMeasureSpec, heightMeasureSpec);
            }
            if (useWidth != 0) {
                useWidth = useWidth + mHorizontalSpace;
            }
            if (useWidth + childView.getMeasuredWidth() + marginWidth > widthSize) {
                useWidth = 0;
                if (lineMaxHeight != 0) {
                    useHeight = useHeight + mVerticalSpace;
                }
                useHeight = useHeight + lineMaxHeight;
                lineMaxHeight = 0;
            }
            useWidth = useWidth + childView.getMeasuredWidth() + marginWidth;
            lineMaxHeight = Math.max(lineMaxHeight, childView.getMeasuredHeight() + marginHeight);
        }
        useHeight = useHeight + lineMaxHeight;
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY ?
                MeasureSpec.getSize(heightMeasureSpec) : useHeight + getPaddingTop() + getPaddingBottom());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int L = getPaddingLeft(), T = getPaddingTop();
        int lineMaxHeight = 0;
        int usefulWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() == View.GONE) {
                continue;
            }
            int leftMargin = 0;
            int rightMargin = 0;
            int topMargin = 0;
            int bottomMargin = 0;
            if (childView.getLayoutParams() instanceof MarginLayoutParams) {
                MarginLayoutParams marginLp = (MarginLayoutParams) childView.getLayoutParams();
                leftMargin = marginLp.leftMargin;
                rightMargin = marginLp.rightMargin;
                topMargin = marginLp.topMargin;
                bottomMargin = marginLp.bottomMargin;
            }
            if (L != getPaddingLeft()) {
                L = L + mHorizontalSpace;
            }
            if (L + childView.getMeasuredWidth() + leftMargin + rightMargin > usefulWidth) {
                if (lineMaxHeight != 0) {
                    T = T + mVerticalSpace;
                }
                T = T + lineMaxHeight;
                L = getPaddingLeft();
                lineMaxHeight = 0;
            }
            childView.layout(L + leftMargin, T + topMargin, L + leftMargin + childView.getMeasuredWidth(), T + topMargin + childView.getMeasuredHeight());
            L = L + childView.getMeasuredWidth() + leftMargin + rightMargin;
            lineMaxHeight = Math.max(lineMaxHeight, childView.getMeasuredHeight() + topMargin + bottomMargin);
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(super.generateDefaultLayoutParams());
    }

    public interface OnFlowClickListener {
        void onClick(View view);
    }

    public void setFlowClickListener(final OnFlowClickListener flowClickListener) {
        mFlowClickListener = flowClickListener;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (flowClickListener != null) {
                childView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        flowClickListener.onClick(v);
                    }
                });
            }

        }
    }

    @Override
    public void onViewAdded(View child) {
        mChildViewList.add(child);
        if (mFlowClickListener != null) {
            child.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFlowClickListener.onClick(v);
                }
            });
        }
    }

    @Override
    public void onViewRemoved(View child) {
        mChildViewList.remove(child);
        child.setOnClickListener(null);
    }
}
