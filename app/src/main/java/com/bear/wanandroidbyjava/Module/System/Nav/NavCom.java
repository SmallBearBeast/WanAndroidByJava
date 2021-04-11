package com.bear.wanandroidbyjava.Module.System.Nav;

import android.view.View;
import android.widget.ProgressBar;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bear.libcomponent.ComponentFrag;
import com.bear.libcomponent.ViewComponent;
import com.bear.librv.DataManager;
import com.bear.librv.RvUtil;
import com.bear.librv.VHAdapter;
import com.bear.wanandroidbyjava.Data.Bean.Nav;
import com.bear.wanandroidbyjava.EventKey;
import com.bear.wanandroidbyjava.R;
import com.example.libbase.Util.CollectionUtil;

import com.example.libframework.Bus.Bus;
import com.example.libframework.Bus.Event;
import com.example.libframework.Bus.EventCallback;

import java.util.List;
import java.util.Set;

public class NavCom extends ViewComponent<ComponentFrag> {
    private RecyclerView recyclerView;
    private ProgressBar pbNavLoading;
    private VHAdapter vhAdapter;
    private DataManager dataManager;
    private NavVM navVM;

    private EventCallback eventCallback = new EventCallback() {
        @Override
        protected void onEvent(Event event) {
            switch (event.eventKey) {
                case EventKey.KEY_NET_CHANGE:
                    if (event.data instanceof Boolean && (Boolean) event.data && navVM.isFirstLoad()) {
                        navVM.loadNavData();
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
        pbNavLoading = findViewById(R.id.pb_nav_loading);
        recyclerView = findViewById(R.id.rv_navi_container);
        vhAdapter = new VHAdapter(getDependence().getViewLifecycleOwner().getLifecycle());
        dataManager = vhAdapter.getDataManager();
        recyclerView.setLayoutManager(new LinearLayoutManager(getComActivity()));
        vhAdapter.register(new NavVHBridge(), Nav.class);
        recyclerView.setAdapter(vhAdapter);
        dataManager.setData(navVM.getNavLD().getValue());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Bus.get().unRegister(eventCallback);
    }

    private void initData() {
        navVM = new ViewModelProvider(getDependence()).get(NavVM.class);
        navVM.getNavLD().observe(getDependence(), new Observer<List<Nav>>() {
            @Override
            public void onChanged(List<Nav> navList) {
                dataManager.setData(navList);
            }
        });
        navVM.getShowProgressLD().observe(getDependence(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean show) {
                if (show != null) {
                    pbNavLoading.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            }
        });
    }

    private void initBus() {
        Bus.get().register(eventCallback);
    }

    @Override
    protected void onFirstVisible() {
        doNetWork();
    }

    private void doNetWork() {
        navVM.loadNavData();
    }

    public void scrollToTop() {
        RvUtil.scrollToTop(recyclerView, true);
    }

    @Override
    protected void onDestroyView() {
        clear(recyclerView, pbNavLoading, vhAdapter, dataManager);
    }
}
