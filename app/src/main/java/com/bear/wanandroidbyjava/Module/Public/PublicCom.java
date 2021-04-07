package com.bear.wanandroidbyjava.Module.Public;

import android.util.Pair;
import android.view.View;
import android.widget.ProgressBar;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bear.libcomponent.ComponentFrag;
import com.bear.libcomponent.ComponentService;
import com.bear.libcomponent.ViewComponent;
import com.bear.librv.VHAdapter;
import com.bear.wanandroidbyjava.Data.Bean.PublicTab;
import com.bear.wanandroidbyjava.EventKey;
import com.bear.wanandroidbyjava.R;
import com.bear.wanandroidbyjava.Widget.NoSwitchViewPager;
import com.example.libbase.Util.CollectionUtil;
import com.example.libframework.Bus.Bus;
import com.example.libframework.Bus.Event;
import com.example.libframework.Bus.EventCallback;

import java.util.List;
import java.util.Set;

public class PublicCom extends ViewComponent<ComponentFrag> {
    private RecyclerView mRvTabContainer;
    private ProgressBar mPbPublicTabLoading;
    private VHAdapter mTabListAdapter;
    private NoSwitchViewPager mNoSwitchViewPager;
    private PublicListFragAdapter mPublicListFragAdapter;
    private PublicTabVM mPublicTabVM;

    private EventCallback mEventCallback = new EventCallback() {
        @Override
        protected void onEvent(Event event) {
            switch (event.eventKey) {
                case EventKey.KEY_NET_CHANGE:
                    if (event.data instanceof Boolean && (Boolean) event.data && mPublicTabVM.isFirstLoad()) {
                        mPublicTabVM.fetchTab();
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
        mPbPublicTabLoading = findViewById(R.id.pb_public_tab_loading);
        mNoSwitchViewPager = findViewById(R.id.vp_article_container);
        mPublicListFragAdapter = new PublicListFragAdapter(getDependence().getChildFragmentManager());
        mNoSwitchViewPager.setAdapter(mPublicListFragAdapter);
        mPublicListFragAdapter.setPublicTabList(mPublicTabVM.getPublicTabList());

        mRvTabContainer = findViewById(R.id.rv_tab_container);
        mRvTabContainer.setLayoutManager(new LinearLayoutManager(getComActivity()));
        mTabListAdapter = new VHAdapter(getDependence().getViewLifecycleOwner().getLifecycle());
        mTabListAdapter.register(new PublicTabVHBridge(), PublicTab.class);
        mRvTabContainer.setAdapter(mTabListAdapter);
        mTabListAdapter.getDataManager().setData(mPublicTabVM.getPublicTabList());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Bus.get().unRegister(mEventCallback);
    }

    private void initData() {
        mPublicTabVM = new ViewModelProvider(getDependence()).get(PublicTabVM.class);
        mPublicTabVM.getPublicTabLD().observe(getDependence(), new Observer<Pair<Boolean, List<PublicTab>>>() {
            @Override
            public void onChanged(Pair<Boolean, List<PublicTab>> pair) {
                if (!CollectionUtil.isEmpty(pair.second)) {
                    mPublicListFragAdapter.setPublicTabList(pair.second);
                    mTabListAdapter.getDataManager().setData(pair.second);
                    if (pair.first) {
                        mPublicTabVM.savePublicTabList(pair.second);
                    }
                }
            }
        });
        mPublicTabVM.getShowProgressLD().observe(getDependence(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean show) {
                if (show != null) {
                    mPbPublicTabLoading.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            }
        });
    }

    private void initBus() {
        Bus.get().register(mEventCallback);
    }

    @Override
    protected void onFirstVisible() {
        mPublicTabVM.fetchTab();
    }

    public void switchTabArticle(int tabId) {
        List<PublicTab> publicTabList = mPublicTabVM.getPublicTabList();
        int index = 0;
        for (int i = 0; i < publicTabList.size(); i++) {
            if (publicTabList.get(i).publicTabId == tabId) {
                index = i;
                break;
            }
        }
        mNoSwitchViewPager.setCurrentItem(index, false);
    }


    public void scrollToTop() {
        List<PublicTab> publicTabList = mPublicTabVM.getPublicTabList();
        int curIndex = mNoSwitchViewPager.getCurrentItem();
        PublicTab curPublicTab = publicTabList.get(curIndex);
        PublicListCom publicListCom = ComponentService.get().getComponent(PublicListCom.class, curPublicTab.publicTabId);
        if (publicListCom != null) {
            publicListCom.scrollToTop();
        }
    }

    @Override
    protected void onDestroyView() {
        clear(mRvTabContainer, mPbPublicTabLoading, mTabListAdapter, mNoSwitchViewPager, mPublicListFragAdapter);
    }
}
