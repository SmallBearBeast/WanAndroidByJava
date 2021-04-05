package com.bear.wanandroidbyjava.Module.Main;

import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.bear.libcomponent.ComponentAct;
import com.bear.libcomponent.ViewComponent;
import com.bear.wanandroidbyjava.R;
import com.example.libbase.Util.ScreenUtil;

public class MainContentCom extends ViewComponent<ComponentAct> {
    public static final int TAB_HOME = 0;
    public static final int TAB_SYSTEM = TAB_HOME + 1;
    public static final int TAB_PUBLIC = TAB_SYSTEM + 1;
    public static final int TAB_PROJECT = TAB_PUBLIC + 1;
    public static final int TAB_PERSONAL = TAB_PROJECT + 1;
    private ViewPager mViewPager;

    @Override
    protected void onCreate() {
        super.onCreate();
        mViewPager = findViewById(R.id.mainViewPager);
        mViewPager.setAdapter(new MainAdapter(getDependence().getSupportFragmentManager()));
        normalScreen();
    }

    private void normalScreen() {
        int color = ContextCompat.getColor(getDependence(), R.color.color_ffffff);
        View homeRootView = findViewById(R.id.mainRootView);
        ScreenUtil.normalScreen(getDependence().getWindow(), color, color, homeRootView);
    }

    public void switchTab(@Tab int tabIndex) {
        mViewPager.setCurrentItem(tabIndex, false);
    }
}
