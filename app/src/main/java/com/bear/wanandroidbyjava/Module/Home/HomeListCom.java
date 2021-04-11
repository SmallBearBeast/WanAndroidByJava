package com.bear.wanandroidbyjava.Module.Home;

import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bear.libcomponent.ComponentFrag;
import com.bear.libcomponent.ViewComponent;
import com.bear.librv.CustomData;
import com.bear.librv.DataManager;
import com.bear.librv.RvUtil;
import com.bear.librv.VHAdapter;
import com.bear.wanandroidbyjava.Data.Bean.Article;
import com.bear.wanandroidbyjava.Data.Bean.BannerSet;
import com.bear.wanandroidbyjava.EventKey;
import com.bear.wanandroidbyjava.R;
import com.example.libbase.Util.CollectionUtil;

import com.example.libframework.Bus.Bus;
import com.example.libframework.Bus.Event;
import com.example.libframework.Bus.EventCallback;

import java.util.List;
import java.util.Set;

@SuppressWarnings({"rawtypes"})
public class HomeListCom extends ViewComponent<ComponentFrag> {
    private static final String TAG = "HomeListCom";
    private static final int LOAD_MORE_OFFSET = 3;
    private static final int BRIDGE_LOAD_MORE = 1;
    private static final int BRIDGE_NO_MORE_DATA = 2;
    private RecyclerView recyclerView;
    private ProgressBar pbLoading;
    private VHAdapter vhAdapter;
    private DataManager dataManager;
    private HomeListVM homeListVM;

    private EventCallback mEventCallback = new EventCallback() {
        @Override
        protected void onEvent(Event event) {
            if (EventKey.KEY_NET_CHANGE.equals(event.eventKey)) {
                if (event.data instanceof Boolean && (Boolean) event.data && !homeListVM.isFinishFirstRefresh()) {
                    doNetWork();
                }
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
    protected void onDestroy() {
        super.onDestroy();
        Bus.get().unRegister(mEventCallback);
    }

    private void initData() {
        homeListVM = new ViewModelProvider(getDependence()).get(HomeListVM.class);
        homeListVM.getRefreshDataListLD().observe(getDependence(), new Observer<List>() {
            @Override
            public void onChanged(List list) {
                dataManager.setData(list);
                dataManager.addLast(CustomData.of(BRIDGE_LOAD_MORE));
            }
        });
        homeListVM.getLoadMoreArticleListLD().observe(getDependence(), new Observer<List<Article>>() {
            @Override
            public void onChanged(List<Article> articles) {
                if (CollectionUtil.isEmpty(articles)) {
                    dataManager.remove(CustomData.of(BRIDGE_LOAD_MORE));
                    dataManager.addLast(CustomData.of(BRIDGE_NO_MORE_DATA));
                } else {
                    dataManager.remove(CustomData.of(BRIDGE_LOAD_MORE));
                    dataManager.addLast(articles);
                    dataManager.addLast(CustomData.of(BRIDGE_LOAD_MORE));
                }
            }
        });
        homeListVM.getShowProgressLD().observe(getDependence(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean show) {
                if (show != null) {
                    pbLoading.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            }
        });
    }

    private void initBus() {
        Bus.get().register(mEventCallback);
    }

    @Override
    protected void onCreateView() {
        pbLoading = findViewById(R.id.pb_loading);
        recyclerView = findViewById(R.id.rv_home_container);
        recyclerView.setLayoutManager(new LinearLayoutManager(getComActivity()));
        vhAdapter = new VHAdapter(getDependence().getViewLifecycleOwner().getLifecycle());
        dataManager = vhAdapter.getDataManager();
        vhAdapter.register(new BannerVHBridge(), BannerSet.class);
        vhAdapter.register(new HomeListVHBridge(), Article.class);
        vhAdapter.register(new LoadMoreVHBridge(), CustomData.of(BRIDGE_LOAD_MORE));
        vhAdapter.register(new NoMoreDataVHBridge(), CustomData.of(BRIDGE_NO_MORE_DATA));
        recyclerView.setAdapter(vhAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (!homeListVM.canLoadMore()) {
                    return;
                }
                int lastVisibleItemPos = RvUtil.findLastVisibleItemPosition(recyclerView);
                if (lastVisibleItemPos > dataManager.size() - LOAD_MORE_OFFSET - 1) {
                    homeListVM.loadMore();
                }
            }
        });
        dataManager.setData(homeListVM.getTotalDataList());
    }

    public void scrollToTop() {
        RvUtil.scrollToTop(recyclerView, 3, 0);
    }

    @Override
    protected void onFirstVisible() {
        doNetWork();
    }

    private void doNetWork() {
        homeListVM.refresh();
    }

    @Override
    protected void onDestroyView() {
        clear(recyclerView, pbLoading, vhAdapter, dataManager);
    }
}
