package com.bear.wanandroidbyjava.Module.Main;

import androidx.viewpager.widget.ViewPager;

import com.bear.libcomponent.ComponentAct;
import com.bear.libcomponent.ViewComponent;
import com.bear.wanandroidbyjava.R;

public class MainContentCom extends ViewComponent<ComponentAct> {
    public ViewPager mViewPager;
    public MainAdapter mMainAdapter;

    @Override
    protected void onCreate() {
        super.onCreate();
        mViewPager = findViewById(R.id.vp_main_container);
        mMainAdapter = new MainAdapter(getDependence().getSupportFragmentManager());
        mViewPager.setAdapter(mMainAdapter);
    }

    public void switchTab(int tabIndex) {
        mViewPager.setCurrentItem(tabIndex, false);
    }
}
