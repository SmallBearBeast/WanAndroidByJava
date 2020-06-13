package com.bear.wanandroidbyjava.Module.Main;

import androidx.viewpager.widget.ViewPager;

import com.bear.wanandroidbyjava.R;
import com.example.libframework.CoreUI.ComponentAct;
import com.example.libframework.CoreUI.ViewComponent;

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
