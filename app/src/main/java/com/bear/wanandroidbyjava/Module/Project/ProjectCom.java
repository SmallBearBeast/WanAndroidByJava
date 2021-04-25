package com.bear.wanandroidbyjava.Module.Project;

import android.util.Pair;
import android.view.View;
import android.widget.ProgressBar;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.bear.libcomponent.ComponentFrag;
import com.bear.libcomponent.ComponentService;
import com.bear.libcomponent.ViewComponent;
import com.bear.wanandroidbyjava.Data.Bean.ProjectTab;
import com.bear.wanandroidbyjava.EventKey;
import com.bear.wanandroidbyjava.R;
import com.example.libbase.Util.CollectionUtil;

import com.example.libframework.Bus.Bus;
import com.example.libframework.Bus.Event;
import com.example.libframework.Bus.EventCallback;
import com.google.android.material.tabs.TabLayout;

import java.util.List;
import java.util.Set;

public class ProjectCom extends ViewComponent<ComponentFrag> implements IProjectCom {
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private ProgressBar mPbProjectLoading;
    private ProjectListFragAdapter mProjectListFragAdapter;
    private ProjectVM mProjectVM;

    private EventCallback mEventCallback = new EventCallback() {
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
    };

    @Override
    protected void onCreate() {
        initData();
        initBus();
    }

    @Override
    protected void onCreateView() {
        mPbProjectLoading = findViewById(R.id.pb_project_loading);
        mViewPager = findViewById(R.id.vp_project_container);
        mTabLayout = findViewById(R.id.tl_project_layout);
        mTabLayout.setupWithViewPager(mViewPager);
        mProjectListFragAdapter = new ProjectListFragAdapter(getDependence().getChildFragmentManager());
        mViewPager.setAdapter(mProjectListFragAdapter);
        mProjectListFragAdapter.setProjectTabList(mProjectVM.getProjectTabList());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Bus.get().unRegister(mEventCallback);
    }

    @Override
    public void scrollToTop() {
        int curIndex = mViewPager.getCurrentItem();
        List<ProjectTab> projectTabList = mProjectVM.getProjectTabList();
        int cid = projectTabList.get(curIndex).projectTabId;
        ComponentService.get().getComponent(IProjectListCom.class, cid).scrollToTop();
    }

    private void initData() {
        mProjectVM = new ViewModelProvider(getDependence()).get(ProjectVM.class);
        mProjectVM.getProjectTabPairLD().observe(getDependence(), new Observer<Pair<Boolean, List<ProjectTab>>>() {
            @Override
            public void onChanged(Pair<Boolean, List<ProjectTab>> pair) {
                if (!CollectionUtil.isEmpty(pair.second)) {
                    mProjectListFragAdapter.setProjectTabList(pair.second);
                    if (pair.first) {
                        mProjectVM.saveProjectTabList(pair.second);
                    }
                }
            }
        });
        mProjectVM.getShowProgressLD().observe(getDependence(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean show) {
                if (show != null) {
                    mPbProjectLoading.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            }
        });
    }

    private void initBus() {
        Bus.get().register(mEventCallback);
    }

    @Override
    protected void onFirstVisible() {
        mProjectVM.fetchProjectTab();
    }

    @Override
    protected void onDestroyView() {
        clear(mPbProjectLoading, mViewPager, mTabLayout, mProjectListFragAdapter);
    }
}
