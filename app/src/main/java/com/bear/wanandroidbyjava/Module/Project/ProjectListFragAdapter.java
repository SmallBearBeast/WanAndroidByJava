package com.bear.wanandroidbyjava.Module.Project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.bear.wanandroidbyjava.Bean.ProjectTab;
import java.util.ArrayList;
import java.util.List;

public class ProjectListFragAdapter extends FragmentPagerAdapter {
    private List<ProjectTab> mProjectTabList = new ArrayList<>();

    public ProjectListFragAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return ProjectListFrag.newInstance(mProjectTabList.get(position).projectTabId);
    }

    @Override
    public int getCount() {
        return mProjectTabList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mProjectTabList.get(position).name;
    }

    public void setProjectTabList(List<ProjectTab> projectTabList) {
        if (projectTabList != null) {
            mProjectTabList = projectTabList;
            notifyDataSetChanged();
        }
    }
}
