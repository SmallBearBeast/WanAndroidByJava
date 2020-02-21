package com.bear.wanandroidbyjava.Module.Main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.bear.wanandroidbyjava.Module.Home.HomeFrag;
import com.bear.wanandroidbyjava.Module.Personal.PersonalFrag;
import com.bear.wanandroidbyjava.Module.Project.ProjectFrag;
import com.bear.wanandroidbyjava.Module.Public.PublicFrag;
import com.bear.wanandroidbyjava.Module.System.SystemFrag;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList = new ArrayList<>();

    public MainAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        fragmentList.add(HomeFrag.newInstance());
        fragmentList.add(SystemFrag.newInstance());
        fragmentList.add(PublicFrag.newInstance());
        fragmentList.add(ProjectFrag.newInstance());
        fragmentList.add(PersonalFrag.newInstance());
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
