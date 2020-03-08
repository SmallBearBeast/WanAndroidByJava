package com.bear.wanandroidbyjava.Module.Public;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.bear.wanandroidbyjava.Bean.PublicTab;

import java.util.ArrayList;
import java.util.List;

public class PublicListFragAdapter extends FragmentPagerAdapter {
    private List<PublicTab> mPublicTabList = new ArrayList<>();

    public PublicListFragAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return PublicListFrag.newInstance(mPublicTabList.get(position).publicTabId);
    }

    @Override
    public int getCount() {
        return mPublicTabList.size();
    }

    public void setPublicTabList(List<PublicTab> publicTabList) {
        if (publicTabList != null) {
            mPublicTabList = publicTabList;
            notifyDataSetChanged();
        }
    }
}
