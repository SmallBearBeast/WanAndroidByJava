package com.bear.wanandroidbyjava.Module.Home;

import android.util.Log;
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
import com.bear.wanandroidbyjava.Tool.Case.CaseHelper;
import com.bear.wanandroidbyjava.Tool.Case.CaseView;
import com.example.libbase.Util.CollectionUtil;

import com.example.libframework.Bus.Bus;
import com.example.libframework.Bus.Event;
import com.example.libframework.Bus.EventCallback;

import java.util.List;
import java.util.Set;

@SuppressWarnings({"rawtypes"})
public class HomeListCom extends ViewComponent<ComponentFrag> implements View.OnClickListener {
    private static final String TAG = "HomeListCom";
    private static final int LOAD_MORE_OFFSET = 3;
    private static final int BRIDGE_LOAD_MORE = 1;
    private static final int BRIDGE_NO_MORE_DATA = 2;
    private static final int BRIDGE_LOAD_FAIL = 3;
    private RecyclerView recyclerView;
    private CaseView caseView;
    private VHAdapter vhAdapter;
    private DataManager dataManager;
    private HomeListVM homeListVM;

    private EventCallback mEventCallback = new EventCallback() {
        @Override
        protected void onEvent(Event event) {
            if (EventKey.KEY_NET_CHANGE.equals(event.eventKey)) {
                if (event.data instanceof Boolean && (Boolean) event.data && !homeListVM.isFinishFirstNetRefresh()) {
                    homeListVM.refresh(false);
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
        initViewModel();
        initBus();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Bus.get().unRegister(mEventCallback);
    }

    private void initViewModel() {
        homeListVM = new ViewModelProvider(getDependence()).get(HomeListVM.class);
        homeListVM.getRefreshDataListLD().observe(getDependence(), new Observer<List>() {
            @Override
            public void onChanged(List list) {
                if (!CollectionUtil.isEmpty(list)) {
                    dataManager.setData(list);
                }
            }
        });
        homeListVM.getLoadMoreDataListLD().observe(getDependence(), new Observer<List<Article>>() {
            @Override
            public void onChanged(List<Article> articles) {
                if (!CollectionUtil.isEmpty(articles)) {
                    dataManager.addLast(articles);
                }
            }
        });
        homeListVM.getRefreshStateLD().observe(getDependence(), new Observer<Byte>() {
            @Override
            public void onChanged(Byte refreshState) {
                Log.d(TAG, "onChanged: refreshState = " + refreshState);
                if (refreshState == null) {
                    return;
                }
                switch (refreshState) {
                    case HomeListVM.REFRESH_NET_ERROR:
                        CaseHelper.showNetError(caseView);
                        break;
                    case HomeListVM.REFRESH_NO_DATA:
                        CaseHelper.showNoData(caseView);
                        break;
                    case HomeListVM.REFRESH_PROGRESS_SHOW:
                        CaseHelper.showLoading(caseView);
                        break;
                    case HomeListVM.REFRESH_PROGRESS_HIDE:
                        CaseHelper.hide(caseView);
                        break;
                }
            }
        });
        homeListVM.getLoadMoreStateLD().observe(getDependence(), new Observer<Byte>() {
            @Override
            public void onChanged(Byte loadMoreState) {
                Log.d(TAG, "onChanged: loadMoreState = " + loadMoreState);
                if (loadMoreState == null) {
                    return;
                }
                switch (loadMoreState) {
                    case HomeListVM.LOAD_MORE_NET_ERROR:
                        updateLastBridgeItem(BRIDGE_LOAD_FAIL);
                        break;
                    case HomeListVM.LOAD_MORE_NO_DATA:
                        updateLastBridgeItem(BRIDGE_NO_MORE_DATA);
                        break;
                    case HomeListVM.LOAD_MORE_PROGRESS:
                        updateLastBridgeItem(BRIDGE_LOAD_MORE);
                        break;
                }
            }
        });
    }

    private void updateLastBridgeItem(int bridgeType) {
        dataManager.remove(CustomData.of(BRIDGE_LOAD_MORE));
        dataManager.remove(CustomData.of(BRIDGE_NO_MORE_DATA));
        dataManager.remove(CustomData.of(BRIDGE_LOAD_FAIL));
        dataManager.addLast(CustomData.of(bridgeType));
    }

    private void initBus() {
        Bus.get().register(mEventCallback);
    }

    @Override
    protected void onCreateView() {
        caseView = findViewById(R.id.case_view);
        caseView.setOnClickListener(this);
        recyclerView = findViewById(R.id.rv_home_container);
        recyclerView.setLayoutManager(new LinearLayoutManager(getComActivity()));
        vhAdapter = new VHAdapter(getDependence().getViewLifecycleOwner().getLifecycle());
        dataManager = vhAdapter.getDataManager();
        vhAdapter.register(new BannerVHBridge(), BannerSet.class);
        vhAdapter.register(new HomeListVHBridge(), Article.class);
        vhAdapter.register(new LoadMoreVHBridge(), CustomData.of(BRIDGE_LOAD_MORE));
        vhAdapter.register(new NoMoreDataVHBridge(), CustomData.of(BRIDGE_NO_MORE_DATA));
        vhAdapter.register(new LoadFailVHBridge(), CustomData.of(BRIDGE_LOAD_FAIL));
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
        homeListVM.refreshViewModel();
    }

    public void scrollToTop() {
        RvUtil.scrollToTop(recyclerView, 3, 0);
    }

    @Override
    protected void onFirstVisible() {
        homeListVM.refresh(true);
    }

    @Override
    protected void onDestroyView() {
        clear(recyclerView, caseView, vhAdapter, dataManager);
    }

    public void loadMore() {
        homeListVM.loadMore();
    }

    @Override
    public void onClick(View view) {
        if (view == caseView) {
            homeListVM.refresh(false);
        }
    }
}
