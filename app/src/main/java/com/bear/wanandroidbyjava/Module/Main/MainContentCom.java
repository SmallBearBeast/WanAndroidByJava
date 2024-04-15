package com.bear.wanandroidbyjava.Module.Main;

import androidx.lifecycle.Lifecycle;
import androidx.viewpager.widget.ViewPager;

import com.bear.libcomponent.component.ActivityComponent;
import com.bear.wanandroidbyjava.R;

public class MainContentCom extends ActivityComponent {
    public static final int TAB_HOME = 0;
    public static final int TAB_SYSTEM = TAB_HOME + 1;
    public static final int TAB_PUBLIC = TAB_SYSTEM + 1;
    public static final int TAB_PROJECT = TAB_PUBLIC + 1;
    public static final int TAB_PERSONAL = TAB_PROJECT + 1;
    private ViewPager mViewPager;

    public MainContentCom(Lifecycle lifecycle) {
        super(lifecycle);
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        mViewPager = findViewById(R.id.mainViewPager);
        mViewPager.setAdapter(new MainAdapter(getActivity().getSupportFragmentManager()));
    }

    public void switchTab(@Tab int tabIndex) {
        mViewPager.setCurrentItem(tabIndex, false);
    }
}
