package com.bear.wanandroidbyjava.Module.Project;

import android.view.View;
import android.widget.ProgressBar;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.bear.wanandroidbyjava.Bean.ProjectTab;
import com.bear.wanandroidbyjava.EventKey;
import com.bear.wanandroidbyjava.R;
import com.example.libbase.Util.CollectionUtil;
import com.example.libframework.Bus.Bus;
import com.example.libframework.Bus.Event;
import com.example.libframework.Bus.EventCallback;
import com.example.libframework.CoreUI.FragComponent;
import com.google.android.material.tabs.TabLayout;

import java.util.List;
import java.util.Set;

public class ProjectCom extends FragComponent {
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private ProgressBar mPbProjectLoading;
    private ProjectListFragAdapter mProjectListFragAdapter;
    private ProjectVM mProjectVM;

    @Override
    protected void onCreate() {
        initData();
        initBus();
    }

    @Override
    protected void onCreateView(View contentView) {
        mPbProjectLoading = findViewById(R.id.pb_project_loading);
        mViewPager = findViewById(R.id.vp_project_container);
        mTabLayout = findViewById(R.id.tl_project_layout);
        mTabLayout.setupWithViewPager(mViewPager);
        mProjectListFragAdapter = new ProjectListFragAdapter(mMain.getChildFragmentManager());
        mViewPager.setAdapter(mProjectListFragAdapter);
        mProjectListFragAdapter.setProjectTabList(mProjectVM.getProjectTabLD().getValue());
    }

    public void scrollToTop() {
        int curIndex = mViewPager.getCurrentItem();
        List<ProjectTab> projectTabList = mProjectVM.getProjectTabLD().getValue();
        int cid = projectTabList.get(curIndex).id;
        ProjectListCom projectListCom = mComActivity.getComponent(ProjectListCom.class, cid);
        if (projectListCom != null) {
            projectListCom.scrollToTop();
        }
    }

    private void initData() {
        mProjectVM = new ViewModelProvider(mMain).get(ProjectVM.class);
        mProjectVM.getProjectTabLD().observe(mMain, new Observer<List<ProjectTab>>() {
            @Override
            public void onChanged(List<ProjectTab> publicTabList) {
                mProjectListFragAdapter.setProjectTabList(publicTabList);
            }
        });
        mProjectVM.getShowProgressLD().observe(mMain, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean show) {
                if (show != null) {
                    mPbProjectLoading.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            }
        });
    }

    private void initBus() {
        Bus.get().register(new EventCallback() {
            @Override
            protected void onEvent(Event event) {
                switch (event.eventKey) {
                    case EventKey.KEY_NET_CHANGE:
                        if (event.data instanceof Boolean && (Boolean) event.data && mProjectVM.isFirstLoad()) {
                            mProjectVM.fetchProjectTab();
                        }
                        break;
                }
            }

            @Override
            protected Set<String> eventKeySet() {
                return CollectionUtil.asSet(EventKey.KEY_NET_CHANGE);
            }
        });
    }

    @Override
    protected void onFirstVisible() {
        mProjectVM.fetchProjectTab();
    }
}
