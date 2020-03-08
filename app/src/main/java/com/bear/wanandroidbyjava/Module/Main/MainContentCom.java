package com.bear.wanandroidbyjava.Module.Main;

import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bear.wanandroidbyjava.R;
import com.example.libframework.CoreUI.ActComponent;

public class MainContentCom extends ActComponent {
    public ViewPager mViewPager;
    public MainAdapter mMainAdapter;

    @Override
    protected void onCreate() {
        super.onCreate();
        mViewPager = findViewById(R.id.vp_main_container);
        mMainAdapter = new MainAdapter(mMain.getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT);
        mViewPager.setAdapter(mMainAdapter);
    }

    public void switchTab(int tabIndex) {
        mViewPager.setCurrentItem(tabIndex, false);
    }
}
