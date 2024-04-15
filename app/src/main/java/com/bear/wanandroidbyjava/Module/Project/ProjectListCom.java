package com.bear.wanandroidbyjava.Module.Project;

import android.util.Pair;
import android.view.View;
import android.widget.ProgressBar;

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
import com.bear.wanandroidbyjava.EventKey;
import com.bear.wanandroidbyjava.Module.Home.HomeListVHBridge;
import com.bear.wanandroidbyjava.Module.Home.LoadMoreVHBridge;
import com.bear.wanandroidbyjava.Module.Home.NoMoreDataVHBridge;
import com.bear.wanandroidbyjava.R;
import com.bear.wanandroidbyjava.Tool.Util.OtherUtil;
import com.example.libbase.Util.CollectionUtil;

import com.example.libframework.Bus.Bus;
import com.example.libframework.Bus.Event;
import com.example.libframework.Bus.EventCallback;

import java.util.List;
import java.util.Set;

public class ProjectListCom extends FragmentComponent {
    private static final int LOAD_MORE_OFFSET = 3;
    private static final int BRIDGE_LOAD_MORE = 1;
    private static final int BRIDGE_NO_MORE_DATA = 2;
    private int mTabId;
    private RecyclerView mRecyclerView;
    private ProgressBar mPbProjectListLoading;
    private VHAdapter mVHAdapter;
    private DataManager mDataManager;
    private ProjectListVM mProjectListVM;

    private EventCallback mEventCallback = new EventCallback() {
        @Override
        protected void onEvent(Event event) {
            switch (event.eventKey) {
                case EventKey.KEY_NET_CHANGE:
                    if (event.data instanceof Boolean && (Boolean) event.data && !mProjectListVM.isFirstLoad()) {
                        doNetWork();
                    }
                    break;
            }
        }

        @Override
        protected Set<String> eventKeySet() {
            return CollectionUtil.asSet(EventKey.KEY_NET_CHANGE);
        }
    };

    public ProjectListCom(Lifecycle lifecycle, int tabId) {
        super(lifecycle);
        mTabId = tabId;
    }

    @Override
    protected void onCreate() {
        initData();
        initBus();
    }

    @Override
    protected void onCreateView() {
        mPbProjectListLoading = findViewById(R.id.pb_project_list_loading);
        mRecyclerView = findViewById(R.id.rv_project_list_container);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mVHAdapter = new VHAdapter(getFragment().getViewLifecycleOwner().getLifecycle());
        mDataManager = mVHAdapter.getDataManager();
        mVHAdapter.register(new HomeListVHBridge(), Article.class);
        mVHAdapter.register(new LoadMoreVHBridge(), CustomData.of(BRIDGE_LOAD_MORE));
        mVHAdapter.register(new NoMoreDataVHBridge(), CustomData.of(BRIDGE_NO_MORE_DATA));
        mRecyclerView.setAdapter(mVHAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!mProjectListVM.canLoadMore()) {
                    return;
                }
                int lastVisibleItemPos = RvUtil.findLastVisibleItemPosition(recyclerView);
                if (lastVisibleItemPos > mDataManager.size() - LOAD_MORE_OFFSET - 1) {
                    mProjectListVM.loadMore(mTabId);
                }
            }
        });
        mDataManager.setData(mProjectListVM.getTotalList());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Bus.get().unRegister(mEventCallback);
    }

    private void initData() {
        mProjectListVM = new ViewModelProvider(getFragment()).get(ProjectListVM.class);
        mProjectListVM.getLoadMoreArticleLD().observe(getFragment(), new Observer<List<Article>>() {
            @Override
            public void onChanged(List<Article> articles) {
                if (CollectionUtil.isEmpty(articles)) {
                    mDataManager.remove(CustomData.of(BRIDGE_LOAD_MORE));
                    mDataManager.addLast(CustomData.of(BRIDGE_NO_MORE_DATA));
                } else {
                    mDataManager.remove(CustomData.of(BRIDGE_LOAD_MORE));
                    mDataManager.addLast(articles);
                    mDataManager.addLast(CustomData.of(BRIDGE_LOAD_MORE));
                }
            }
        });
        mProjectListVM.getRefreshArticlePairLD().observe(getFragment(), new Observer<Pair<Boolean, List<Article>>>() {
            @Override
            public void onChanged(Pair<Boolean, List<Article>> pair) {
                if (!CollectionUtil.isEmpty(pair.second)) {
                    mDataManager.setData(pair.second);
                    if (pair.first) {
                        mProjectListVM.saveTabArticleList(mTabId, pair.second);
                    }
                }
            }
        });
        mProjectListVM.getShowProgressLD().observe(getFragment(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean show) {
                if (show != null) {
                    mPbProjectListLoading.setVisibility(show ? View.VISIBLE : View.GONE);
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
        mProjectListVM.refresh(mTabId);
    }

    public void scrollToTop() {
        RvUtil.scrollToTop(mRecyclerView, 3, 0);
    }

    @Override
    protected void onDestroyView() {
        OtherUtil.clear(mRecyclerView, mPbProjectListLoading, mVHAdapter, mDataManager);
    }
}
