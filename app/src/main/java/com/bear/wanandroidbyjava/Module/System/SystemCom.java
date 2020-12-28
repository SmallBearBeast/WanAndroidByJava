package com.bear.wanandroidbyjava.Module.System;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bear.libcomponent.ComponentFrag;
import com.bear.libcomponent.ComponentService;
import com.bear.libcomponent.ViewComponent;
import com.bear.wanandroidbyjava.Module.System.Nav.NavCom;
import com.bear.wanandroidbyjava.Module.System.Nav.NavFrag;
import com.bear.wanandroidbyjava.Module.System.Tree.TreeCom;
import com.bear.wanandroidbyjava.Module.System.Tree.TreeFrag;
import com.bear.wanandroidbyjava.R;

import com.example.libbase.Util.ResourceUtil;
import com.google.android.material.tabs.TabLayout;

public class SystemCom extends ViewComponent<ComponentFrag> {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreateView() {
        mTabLayout = findViewById(R.id.tl_title_layout);
        mViewPager = findViewById(R.id.vp_sys_container);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setAdapter(new SystemFragAdapter(getDependence().getChildFragmentManager()));
    }

    public void scrollToTop() {
        int index = mViewPager.getCurrentItem();
        if (index == 0) {
            ComponentService.get().getComponent(TreeCom.class).scrollToTop();
        } else if (index == 1) {
            ComponentService.get().getComponent(NavCom.class).scrollToTop();
        }
    }

    @Override
    protected void onDestroyView() {
        mTabLayout = null;
        mViewPager = null;
    }

    private static class SystemFragAdapter extends FragmentPagerAdapter {

        private SystemFragAdapter(@NonNull FragmentManager fm) {
            super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return position == 0 ? TreeFrag.newInstance() : NavFrag.newInstance();
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return position == 0 ? ResourceUtil.getString(R.string.str_system) : ResourceUtil.getString(R.string.str_navigation);
        }
    }
}
