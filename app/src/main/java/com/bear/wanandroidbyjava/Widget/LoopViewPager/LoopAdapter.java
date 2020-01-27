package com.bear.wanandroidbyjava.Widget.LoopViewPager;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

class LoopAdapter extends PagerAdapter {
    private PagerAdapter mPagerAdapter;

    LoopAdapter(PagerAdapter pagerAdapter) {
        mPagerAdapter = pagerAdapter;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        return mPagerAdapter.instantiateItem(container, position % mPagerAdapter.getCount());
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        mPagerAdapter.destroyItem(container, position % mPagerAdapter.getCount(), object);
    }
}
