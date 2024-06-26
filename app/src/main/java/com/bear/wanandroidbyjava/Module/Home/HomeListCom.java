package com.bear.wanandroidbyjava.Module.Home;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bear.libcomponent.component.FragmentComponent;
import com.bear.librv.CustomData;
import com.bear.librv.DataManager;
import com.bear.librv.RvUtil;
import com.bear.librv.VHAdapter;
import com.bear.wanandroidbyjava.Data.Bean.Article;
import com.bear.wanandroidbyjava.Data.Bean.BannerSet;
import com.bear.wanandroidbyjava.EventKey;
import com.bear.wanandroidbyjava.Module.Collect.CollectInfo;
import com.bear.wanandroidbyjava.R;
import com.bear.wanandroidbyjava.Tool.Case.CaseHelper;
import com.bear.wanandroidbyjava.Tool.Case.CaseView;
import com.bear.wanandroidbyjava.Tool.Util.OtherUtil;
import com.example.libbase.Util.CollectionUtil;

import com.example.libframework.Bus.Bus;
import com.example.libframework.Bus.Event;
import com.example.libframework.Bus.EventCallback;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.List;
import java.util.Set;

@SuppressWarnings({"rawtypes"})
public class HomeListCom extends FragmentComponent implements View.OnClickListener {
    private static final String TAG = "HomeListCom";
    private static final int LOAD_MORE_OFFSET = 3;
    private static final int BRIDGE_LOAD_MORE = 1;
    private static final int BRIDGE_NO_MORE_DATA = 2;
    private static final int BRIDGE_LOAD_FAIL = 3;
    private RefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private CaseView caseView;
    private VHAdapter vhAdapter;
    private DataManager dataManager;
    private HomeListVM homeListVM;

    private final EventCallback eventCallback = new EventCallback() {
        @Override
        protected void onEvent(Event event) {
            switch (event.eventKey) {
                case EventKey.KEY_NET_CHANGE:
                    if (event.data instanceof Boolean && (Boolean) event.data && !homeListVM.isFinishFirstNetRefresh()) {
                        homeListVM.refresh(false);
                    }
                    break;
                case EventKey.KEY_COLLECT_OR_UN_COLLECT_OUT_EVENT:
                case EventKey.KEY_COLLECT_OR_UN_COLLECT_EVENT:
                    if (event.data instanceof CollectInfo) {
                        CollectInfo collectInfo = (CollectInfo) event.data;
                        homeListVM.updateCollectInfo(collectInfo);
                    }
                    break;

                default:
                    break;
            }
        }

        @Override
        protected Set<String> eventKeySet() {
            return CollectionUtil.asSet(
                    EventKey.KEY_NET_CHANGE,
                    EventKey.KEY_COLLECT_OR_UN_COLLECT_EVENT,
                    EventKey.KEY_COLLECT_OR_UN_COLLECT_OUT_EVENT
            );
        }
    };

    public HomeListCom(Lifecycle lifecycle) {
        super(lifecycle);
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        initViewModel();
        initBus();
    }

    private void initViewModel() {
        homeListVM = new ViewModelProvider(getFragment()).get(HomeListVM.class);
        homeListVM.getRefreshDataListLD().observe(getFragment(), new Observer<List>() {
            @Override
            public void onChanged(List list) {
                if (!CollectionUtil.isEmpty(list)) {
                    dataManager.setData(list);
                }
            }
        });
        homeListVM.getLoadMoreDataListLD().observe(getFragment(), new Observer<List<Article>>() {
            @Override
            public void onChanged(List<Article> articles) {
                if (!CollectionUtil.isEmpty(articles)) {
                    dataManager.addLast(articles);
                }
            }
        });
        homeListVM.getRefreshStateLD().observe(getFragment(), new Observer<Byte>() {
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
                    case HomeListVM.REFRESH_LAYOUT_SHOW:
                        refreshLayout.autoRefresh(300, 300, 1.2f, true);
                        break;
                    case HomeListVM.REFRESH_LAYOUT_HIDE:
                        // Need to delay otherwise the loading icon can not hide.
                        refreshLayout.finishRefresh(500);
                        break;
                    case HomeListVM.REFRESH_LAYOUT_FAIL:
                        // Need to delay otherwise the loading icon can not hide.
                        refreshLayout.finishRefresh(500, false, false);
                        break;
                }
            }
        });
        homeListVM.getLoadMoreStateLD().observe(getFragment(), new Observer<Byte>() {
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
        homeListVM.getUpdateDataLD().observe(getFragment(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer index) {
                if (index != null) {
                    dataManager.update((int)index);
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
        Bus.get().register(eventCallback);
    }

    @Override
    protected void onCreateView() {
        initRefreshLayout();
        initCaseView();
        initRecyclerView();
        homeListVM.refreshViewModel();
    }

    private void initRefreshLayout() {
        refreshLayout = findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                homeListVM.refresh(false);
            }
        });
    }

    private void initCaseView() {
        caseView = findViewById(R.id.case_view);
        caseView.setOnClickListener(this);
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.rv_home_container);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        vhAdapter = new VHAdapter(getFragment().getViewLifecycleOwner().getLifecycle());
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
    }

    public void scrollToTop() {
        if (RvUtil.isTop(recyclerView)) {
            refreshLayout.autoRefresh(0, 300, 1.2f, false);
        } else {
            RvUtil.scrollToTop(recyclerView, 3, 0);
        }
    }

    @Override
    protected void onFirstVisible() {
        homeListVM.refresh(true);
    }

    @Override
    protected void onDestroyView() {
        OtherUtil.clear(recyclerView, caseView, vhAdapter, dataManager, refreshLayout);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Bus.get().unRegister(eventCallback);
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
