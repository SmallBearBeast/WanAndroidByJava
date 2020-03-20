package com.bear.wanandroidbyjava.Module.System.Nav;

import android.view.View;
import android.widget.ProgressBar;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bear.wanandroidbyjava.Bean.Nav;
import com.bear.wanandroidbyjava.EventKey;
import com.bear.wanandroidbyjava.R;
import com.example.libbase.Util.CollectionUtil;

import com.example.libframework.Bus.Bus;
import com.example.libframework.Bus.Event;
import com.example.libframework.Bus.EventCallback;
import com.example.libframework.CoreUI.FragComponent;
import com.example.libframework.Rv.DataManager;
import com.example.libframework.Rv.RvUtil;
import com.example.libframework.Rv.VHAdapter;

import java.util.List;
import java.util.Set;

public class NavCom extends FragComponent {
    private RecyclerView mRecyclerView;
    private ProgressBar mPbNavLoading;
    private VHAdapter mVhAdapter;
    private DataManager mDataManager;
    private NavVM mNavVM;

    private EventCallback mEventCallback = new EventCallback() {
        @Override
        protected void onEvent(Event event) {
            switch (event.eventKey) {
                case EventKey.KEY_NET_CHANGE:
                    if (event.data instanceof Boolean && (Boolean) event.data && mNavVM.isFirstLoad()) {
                        mNavVM.fetchNav();
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
        super.onCreate();
        initData();
        initBus();
    }

    @Override
    protected void onCreateView() {
        mPbNavLoading = findViewById(R.id.pb_nav_loading);
        mRecyclerView = findViewById(R.id.rv_navi_container);
        mVhAdapter = new VHAdapter(mMain.getViewLifecycleOwner().getLifecycle());
        mDataManager = mVhAdapter.getDataManager();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mComActivity));
        mVhAdapter.register(new NavVHBridge(), Nav.class);
        mRecyclerView.setAdapter(mVhAdapter);
        mDataManager.setData(mNavVM.getNavLD().getValue());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Bus.get().unRegister(mEventCallback);
    }

    private void initData() {
        mNavVM = new ViewModelProvider(mMain).get(NavVM.class);
        mNavVM.getNavLD().observe(mMain, new Observer<List<Nav>>() {
            @Override
            public void onChanged(List<Nav> navList) {
                mDataManager.setData(navList);
            }
        });
        mNavVM.getShowProgressLD().observe(mMain, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean show) {
                if (show != null) {
                    mPbNavLoading.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            }
        });
    }

    private void initBus() {
        Bus.get().register(mEventCallback);
    }

    @Override
    protected void onFirstVisible() {
        doNetWork();
    }

    private void doNetWork() {
        mNavVM.fetchNav();
    }

    public void scrollToTop() {
        RvUtil.scrollToTop(mRecyclerView, true);
    }

    @Override
    protected void onDestroyView() {
        mRecyclerView = null;
        mPbNavLoading = null;
        mVhAdapter = null;
        mDataManager = null;
    }
}
