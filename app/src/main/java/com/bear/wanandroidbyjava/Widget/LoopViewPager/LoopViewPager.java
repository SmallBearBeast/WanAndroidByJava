package com.bear.wanandroidbyjava.Widget.LoopViewPager;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class LoopViewPager extends ViewPager {
    private static final String TAG = "LoopViewPager";
    private static final long DEFAULT_LOOP_INTERVAL = 2000;
    private long mLoopInterval = DEFAULT_LOOP_INTERVAL;
    private boolean mIsStartLoop = true;

    public LoopViewPager(@NonNull Context context) {
        this(context, null);
    }

    public LoopViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        if (context instanceof AppCompatActivity) {
            AppCompatActivity appCompatActivity = (AppCompatActivity) context;
            appCompatActivity.getLifecycle().addObserver(new LifecycleEventObserver() {
                @Override
                public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                    switch (event) {
                        case ON_START:
                            startLoop();
                            break;
                        case ON_STOP:
                            stopLoop();
                            break;
                    }
                }
            });
        }
    }

    @Override
    public void setAdapter(@NonNull PagerAdapter adapter) {
        super.setAdapter(new LoopAdapter(adapter));
        int count = adapter.getCount();
        int curIndex = Integer.MAX_VALUE / 2 / count * count;
        setCurrentItem(curIndex);
    }

    public void startLoop() {
        if (mIsStartLoop) {
            stopLoop();
            postDelayed(loopRun, mLoopInterval);
        }
    }

    public void stopLoop() {
        if (mIsStartLoop) {
            removeCallbacks(loopRun);
        }
    }

    private Runnable loopRun = new Runnable() {
        @Override
        public void run() {
            setCurrentItem(getCurrentItem() + 1);
            startLoop();
        }
    };

    public void setLoopInterval(long loopInterval) {
        mLoopInterval = loopInterval;
    }

    public void setIsStartLoop(boolean isStartLoop) {
        mIsStartLoop = isStartLoop;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "onTouch: action_down or action_move");
                stopLoop();
                break;

            case MotionEvent.ACTION_UP:
                Log.d(TAG, "onTouch: action_up");
                startLoop();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
