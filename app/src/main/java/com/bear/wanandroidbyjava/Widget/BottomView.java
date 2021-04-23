package com.bear.wanandroidbyjava.Widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingParent2;
import androidx.core.view.ViewCompat;

public class BottomView {
    private static final String TAG = "BottomView";
    private final int MINIMUM_FLING_VELOCITY;
    private boolean mIsCreated;
    private boolean mIsDoingAnim;
    private boolean mIsShowed;
    private boolean mDimEnable = false; // 是否支持MaskView透明度变化
    private boolean mDraggable = true; // 是否支持下拉消失
    private boolean mMaskable = true; // MaskView是否可点击
    private boolean mMaskCancelable = true; // MaskView是否可点击退出BottomView
    private float mDimAmount = 0.5f; // MaskView最终透明度值 0.0~1.0
    private int mBottomOffset = 0; // BottomView与屏幕底部边距
    private int mAnimDuration = 200; // BottomView弹出和消失动画时长
    private float mHideProportion = 0.3f; // 滑动距离与View高度比例大于该值触发下滑隐藏
    private int mHideVelocity; // 滑动速度大于该值触发下滑隐藏，默认值是MINIMUM_FLING_VELOCITY
    private Activity mActivity;
    private View mMaskView;
    private View mContentView;
    private InternalView mInternalView;
    private FrameLayout mParentView;
    private Handler mMainHandler = new Handler();

    public BottomView(Activity activity) {
        mActivity = activity;
        ViewConfiguration configuration = ViewConfiguration.get(activity);
        MINIMUM_FLING_VELOCITY = configuration.getScaledMinimumFlingVelocity();
        mHideVelocity = MINIMUM_FLING_VELOCITY;
    }

    public BottomView contentView(@LayoutRes int layoutId) {
        FrameLayout decorViewGroup = (FrameLayout) getDecorView();
        mContentView = LayoutInflater.from(mActivity).inflate(layoutId, decorViewGroup, false);
        return this;
    }

    public BottomView contentView(View contentView) {
        mContentView = contentView;
        return this;
    }

    public BottomView bottomOffset(int bottomOffset) {
        mBottomOffset = bottomOffset;
        return this;
    }

    public BottomView dimEnable(boolean dimEnable) {
        mDimEnable = dimEnable;
        return this;
    }

    public BottomView dimAmount(float dimAmount) {
        mDimAmount = dimAmount;
        return this;
    }

    public BottomView animDuration(int animDuration) {
        mAnimDuration = animDuration;
        return this;
    }

    public BottomView dragEnable(boolean dragEnable) {
        mDraggable = dragEnable;
        return this;
    }

    public BottomView maskEnable(boolean maskEnable) {
        mMaskable = maskEnable;
        return this;
    }

    public BottomView maskCancelable(boolean cancelable) {
        mMaskCancelable = cancelable;
        return this;
    }

    public BottomView hideProportion(float proportion) {
        mHideProportion = proportion;
        return this;
    }

    public BottomView hideVelocity(int hideVelocity) {
        mHideVelocity = hideVelocity;
        return this;
    }

    public BottomView hideViews(View... views) {
        for (View view : views) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BottomView.this.hide();
                }
            });
        }
        return this;
    }

    public void show() {
        if (!checkActivity()) {
            return;
        }
        if (mContentView == null) {
            return;
        }
        if (mIsShowed || mIsDoingAnim) {
            return;
        }
        mIsShowed = true;
        if (!mIsCreated) {
            mIsCreated = true;
            initView();
            initTouch();
        }
        doShowAnim();
        onShow();
    }

    private void initView() {
        mParentView = new FrameLayout(mActivity);
        mMaskView = new View(mActivity);
        mMaskView.setBackgroundColor(Color.BLACK);
        mMaskView.setAlpha(0);
        FrameLayout.LayoutParams maskViewLp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mParentView.addView(mMaskView, maskViewLp);

        FrameLayout.LayoutParams internalViewLp = (FrameLayout.LayoutParams) mContentView.getLayoutParams();
        if (internalViewLp == null) {
            internalViewLp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        } else {
            internalViewLp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        }
        internalViewLp.gravity = Gravity.BOTTOM;
        mInternalView = new InternalView(mActivity);
        mInternalView.addView(mContentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mParentView.addView(mInternalView, internalViewLp);

        FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        int navigationBarHeight = getNavigationBarHeight();
        if (navigationBarHeight > 0) {
            mBottomOffset = mBottomOffset + navigationBarHeight;
        }
        flp.setMargins(0, 0, 0, mBottomOffset);
        mParentView.setLayoutParams(flp);
    }

    private void initTouch() {
        if (mMaskable) {
            if (mMaskCancelable) {
                mMaskView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BottomView.this.hide();
                    }
                });
            } else {
                mMaskView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
            }
        }
        if (mDraggable) {
            mInternalView.setOnTouchListener(new InternalTouchListener());
        }
    }

    public void hide() {
        if (!mIsShowed || mIsDoingAnim) {
            return;
        }
        mIsShowed = false;
        doHideAnim();
        onHide();
    }

    public void reset() {
        mIsShowed = true;
        float fraction = mContentView.getTranslationY() / mContentView.getHeight();
        Log.d(TAG, "reset: fraction = " + fraction);
        if (fraction > 0f) {
            doResetAnim();
            onReset();
        }
    }

    private void doResetAnim() {
        ObjectAnimator translationYAnim = ObjectAnimator.ofFloat(mContentView, "translationY", 0);
        translationYAnim.setDuration(mAnimDuration);
        translationYAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mIsDoingAnim = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mIsDoingAnim = false;
                animation.removeListener(this);
            }
        });
        translationYAnim.start();
    }

    private void doShowAnim() {
        addToDecorView();
        // 必须设置INVISIBLE，GONE会出现获取不到height值导致动画失效，VISIBLE会出现闪现问题。
        mParentView.setVisibility(View.INVISIBLE);
        // 必须post，保证能获取到height值。
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator translationYAnim = ObjectAnimator.ofFloat(mContentView, "translationY", mContentView.getHeight(), 0);
                translationYAnim.setDuration(mAnimDuration);
                translationYAnim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        mIsDoingAnim = true;
                        mParentView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mIsDoingAnim = false;
                        animation.removeListener(this);
                    }
                });
                translationYAnim.start();
                if (mDimEnable) {
                    ObjectAnimator dimAnim = ObjectAnimator.ofFloat(mMaskView, "alpha", 0, mDimAmount);
                    dimAnim.setDuration(mAnimDuration);
                    dimAnim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            animation.removeListener(this);
                        }
                    });
                    dimAnim.start();
                }

            }
        });
    }

    private void doHideAnim() {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator translationYAnim = ObjectAnimator.ofFloat(mContentView, "translationY", mContentView.getHeight());
                translationYAnim.setDuration(mAnimDuration);
                translationYAnim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        mIsDoingAnim = true;
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mIsDoingAnim = false;
                        animation.removeListener(this);
                        removeFromDecorView();
                    }
                });
                translationYAnim.start();
                if (mDimEnable) {
                    ObjectAnimator dimAnim = ObjectAnimator.ofFloat(mMaskView, "alpha", mDimAmount, 0);
                    dimAnim.setDuration(mAnimDuration);
                    dimAnim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            animation.removeListener(this);
                        }
                    });
                    dimAnim.start();
                }
            }
        });
    }

    private boolean checkActivity() {
        return mActivity != null && !mActivity.isFinishing();
    }

    protected <T extends View> T findViewById(@IdRes int id) {
        return mContentView.findViewById(id);
    }

    protected Activity getActivity() {
        return mActivity;
    }

    public View getContentView() {
        return mContentView;
    }

    public boolean isShow() {
        return mIsShowed;
    }

    private void checkUpAnim() {
        Log.d(TAG, "checkUpAnim: translationY = " + mContentView.getTranslationY() + ", hideHeight = " + mContentView.getHeight() * mHideProportion);
        if (mContentView.getTranslationY() >= mContentView.getHeight() * mHideProportion) {
            hide();
        } else {
            reset();
        }
    }

    private int getNavigationBarHeight() {
        Window window = mActivity.getWindow();
        Display display = window.getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getRealSize(point);
        View decorView = window.getDecorView();
        Configuration configuration = mActivity.getResources().getConfiguration();
        Rect rect = new Rect();
        decorView.getWindowVisibleDisplayFrame(rect);
        if (Configuration.ORIENTATION_LANDSCAPE == configuration.orientation) {
            return point.x - rect.right;
        } else {
            return point.y - rect.bottom;
        }
    }

    private View getDecorView() {
        return mActivity.getWindow().getDecorView();
    }

    private void addToDecorView() {
        ViewGroup viewGroup = (ViewGroup) getDecorView();
        if (mParentView.getParent() == null) {
            viewGroup.addView(mParentView);
        }
    }

    private void removeFromDecorView() {
        ViewGroup viewGroup = (ViewGroup) getDecorView();
        if (mParentView.getParent() == viewGroup) {
            viewGroup.removeView(mParentView);
        }
    }

    protected void onReset() {

    }

    protected void onHide() {

    }

    protected void onShow() {

    }

    protected void onTranslationY(int translationY) {

    }

    /**
     * 外层View容器，包含MaskView和ContentView，处理嵌套滑动事件。
     */
    private class InternalView extends FrameLayout implements NestedScrollingParent2 {
        private int mTotalTranslationY;
        private boolean mIsDoFling;
        private boolean mIsActionUp;

        private InternalView(@NonNull Context context) {
            this(context, null);
        }

        public InternalView(@NonNull Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {
            // 动画中直接消费事件，不处理
            if (mIsDoingAnim) {
                return true;
            }
            // ACTION_DOWN mIsActionUp必须设置为false，否则被拦截就会出现问题
            if (ev.getAction() == MotionEvent.ACTION_UP) {
                mIsActionUp = true;
            } else if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                mIsActionUp = false;
            }
            return super.dispatchTouchEvent(ev);
        }

        @Override
        public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, @ViewCompat.ScrollAxis int axes, @ViewCompat.NestedScrollType int type) {
            boolean scroll = mDraggable && !mIsDoingAnim && type == ViewCompat.TYPE_TOUCH && target instanceof NestedScrollingChild;
            Log.d(TAG, "onStartNestedScroll: axes = " + axes + ", type = " + type + ", scroll = " + scroll);
            return scroll;
        }

        @Override
        public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, @ViewCompat.ScrollAxis int axes, @ViewCompat.NestedScrollType int type) {
            Log.d(TAG, "onNestedScrollAccepted: axes = " + axes + ", type = " + type);
        }

        @Override
        public void onStopNestedScroll(@NonNull View target, @ViewCompat.NestedScrollType int type) {
            Log.d(TAG, "onStopNestedScroll: type = " + type + ", mIsActionUp = " + mIsActionUp + ", mIsDoFling = " + mIsDoFling);
            if (!mIsActionUp) {
                return;
            }
            // fling事件优先。
            if (!mIsDoFling) {
                checkUpAnim();
            }
            mIsDoFling = false;
            mIsActionUp = false;
            mTotalTranslationY = 0;
        }

        @Override
        public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @ViewCompat.NestedScrollType int type) {

        }

        @Override
        public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, @ViewCompat.NestedScrollType int type) {
//            Log.d(TAG, "onNestedPreScroll: dx = " + dx + ", dy = " + dy + ", type = " + type + ", consumed = (" + consumed[0] + ", " +consumed[1] + ")");
            if (dy < 0) {
                if (!target.canScrollVertically(-1)) {
                    consumed[0] = 0;
                    consumed[1] = dy;
                    mTotalTranslationY = mTotalTranslationY - dy;
                    if (mTotalTranslationY > mContentView.getHeight()) {
                        mTotalTranslationY = mContentView.getHeight();
                    }
                    mContentView.setTranslationY(mTotalTranslationY);
                    onTranslationY(mTotalTranslationY);
                }
            } else {
                if (mTotalTranslationY > 0) {
                    consumed[0] = 0;
                    consumed[1] = dy;
                    if (mTotalTranslationY < dy) {
                        mTotalTranslationY = 0;
                    } else {
                        mTotalTranslationY = mTotalTranslationY - dy;
                    }
                    mContentView.setTranslationY(mTotalTranslationY);
                    onTranslationY(mTotalTranslationY);
                }
            }
        }

        @Override
        public boolean onNestedPreFling(@NonNull View target, float velocityX, float velocityY) {
            Log.d(TAG, "onNestedPreFling: velocityY = " + velocityY + ", mIsActionUp = " + mIsActionUp + ", mIsDoFling = " + mIsDoFling);
            if (velocityY < 0) {
                velocityY = -velocityY;
                if (velocityY > mHideVelocity && !target.canScrollVertically(-1)) {
                    hide();
                    mTotalTranslationY = 0;
                    mIsDoFling = true;
                    return true;
                }
            } else {
                if (velocityY > mHideVelocity && !target.canScrollVertically(-1)) {
                    reset();
                    mTotalTranslationY = 0;
                    mIsDoFling = true;
                    return true;
                }
            }
            return super.onNestedPreFling(target, velocityX, velocityY);
        }
    }

    /**
     * 用于普通事件处理。
     */
    private class InternalTouchListener implements View.OnTouchListener {
        private float mStartY;
        private VelocityTracker mVelocityTracker;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // 动画中直接消费事件，不处理
            if (mIsDoingAnim) {
                return true;
            }
            if (mVelocityTracker == null) {
                mVelocityTracker = VelocityTracker.obtain();
            }
            MotionEvent velocityMotionEvent = MotionEvent.obtain(event);
            velocityMotionEvent.setLocation(velocityMotionEvent.getRawX(), velocityMotionEvent.getRawY());
            mVelocityTracker.addMovement(velocityMotionEvent);
            velocityMotionEvent.recycle();
            // 滑动过程插入可能触发不到DOWN事件，这里提前检查一下并初始化，异常case
            if (mStartY == 0) {
                mStartY = event.getRawY();
            }
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mStartY = event.getRawY();
                    Log.d(TAG, "onTouch: ACTION_DOWN mStartY = " + mStartY);
                    break;
                case MotionEvent.ACTION_MOVE:
                    int y = (int) (event.getRawY() - mStartY);
                    //                    Log.d(TAG, "onTouch: ACTION_MOVE mStartY = " + mStartY + ", y = " + y);
                    if (y < 0) {
                        y = 0;
                    } else if (y > mContentView.getHeight()) {
                        y = mContentView.getHeight();
                    }
                    //                    Log.d(TAG, "onTouch: ACTION_MOVE y = " + y);
                    mContentView.setTranslationY(y);
                    onTranslationY(y);
                    break;
                case MotionEvent.ACTION_UP:
                    mVelocityTracker.computeCurrentVelocity(1000);
                    float velocityY = mVelocityTracker.getYVelocity();
                    Log.d(TAG, "onTouch: ACTION_UP velocityY = " + velocityY);
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                    // fling事件优先。
                    if (!onFling(velocityY)) {
                        checkUpAnim();
                    }
                    break;
            }
            return true;
        }

        private boolean onFling(float velocityY) {
            if (Math.abs(velocityY) < MINIMUM_FLING_VELOCITY) {
                return false;
            }
            if (velocityY > 0) {
                if (velocityY > mHideVelocity) {
                    hide();
                    return true;
                }
            } else {
                if (Math.abs(velocityY) > mHideVelocity) {
                    reset();
                    return true;
                }
            }
            return false;
        }
    }
}
