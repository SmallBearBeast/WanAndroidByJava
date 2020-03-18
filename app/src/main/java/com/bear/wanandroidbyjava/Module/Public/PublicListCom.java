package com.bear.wanandroidbyjava.Module.Public;

import android.util.Pair;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bear.wanandroidbyjava.Bean.Article;
import com.bear.wanandroidbyjava.EventKey;
import com.bear.wanandroidbyjava.Module.Home.HomeListVHBridge;
import com.bear.wanandroidbyjava.Module.Home.LoadMoreVHBridge;
import com.bear.wanandroidbyjava.Module.Home.NoMoreDataVHBridge;
import com.bear.wanandroidbyjava.R;
import com.example.libbase.Util.CollectionUtil;

import com.example.libframework.Bus.Bus;
import com.example.libframework.Bus.Event;
import com.example.libframework.Bus.EventCallback;
import com.example.libframework.CoreUI.FragComponent;
import com.example.libframework.Rv.DataManager;
import com.example.libframework.Rv.DataType;
import com.example.libframework.Rv.RvUtil;
import com.example.libframework.Rv.VHAdapter;

import java.util.List;
import java.util.Set;

public class PublicListCom extends FragComponent {
    private static final int LOAD_MORE_OFFSET = 3;
    private static final int BRIDGE_LOAD_MORE = 1;
    private static final int BRIDGE_NO_MORE_DATA = 2;
    private int mTabId;
    private RecyclerView mRecyclerView;
    private ProgressBar mPbPublicListLoading;
    private VHAdapter mVHAdapter;
    private DataManager mDataManager;
    private PublicListVM mPublicListVM;

    public PublicListCom(int tabId) {
        mTabId = tabId;
    }

    @Override
    protected void onCreate() {
        initData();
        initBus();
    }

    @Override
    protected void onCreateView() {
        mPbPublicListLoading = findViewById(R.id.pb_public_list_loading);
        mRecyclerView = findViewById(R.id.rv_public_list_container);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mComActivity));
        mVHAdapter = new VHAdapter(mMain.getViewLifecycleOwner().getLifecycle());
        mDataManager = mVHAdapter.getDataManager();
        mVHAdapter.register(new HomeListVHBridge(), Article.class);
        mVHAdapter.register(new LoadMoreVHBridge(), BRIDGE_LOAD_MORE);
        mVHAdapter.register(new NoMoreDataVHBridge(), BRIDGE_NO_MORE_DATA);
        mVHAdapter.setOnGetDataType(new VHAdapter.OnGetDataType() {
            @Override
            public int getType(Object obj, int pos) {
                if (obj.equals(BRIDGE_LOAD_MORE)) {
                    return BRIDGE_LOAD_MORE;
                } else if (obj.equals(BRIDGE_NO_MORE_DATA)) {
                    return BRIDGE_NO_MORE_DATA;
                }
                return -1;
            }
        });
        mRecyclerView.setAdapter(mVHAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!mPublicListVM.canLoadMore()) {
                    return;
                }
                int lastVisibleItemPos = RvUtil.findLastVisibleItemPosition(recyclerView);
                if (lastVisibleItemPos > mDataManager.size() - LOAD_MORE_OFFSET - 1) {
                    mPublicListVM.loadMore(mTabId);
                }
            }
        });
        mDataManager.setData(mPublicListVM.getTotalList());
    }

    private void initData() {
        mPublicListVM = new ViewModelProvider(mMain).get(PublicListVM.class);
        mPublicListVM.getRefreshArticlePairLD().observe(mMain, new Observer<Pair<Boolean, List<Article>>>() {
            @Override
            public void onChanged(Pair<Boolean, List<Article>> pair) {
                if (!CollectionUtil.isEmpty(pair.second)) {
                    mDataManager.setData(pair.second);
                    if (pair.first) {
                        mPublicListVM.saveTabArticleList(mTabId, pair.second);
                    }
                }
            }
        });
        mPublicListVM.getLoadMoreArticleLD().observe(mMain, new Observer<List<Article>>() {
            @Override
            public void onChanged(List<Article> articles) {
                if (CollectionUtil.isEmpty(articles)) {
                    mDataManager.remove(DataType.of(BRIDGE_LOAD_MORE));
                    mDataManager.addLast(DataType.of(BRIDGE_NO_MORE_DATA));
                } else {
                    mDataManager.remove(DataType.of(BRIDGE_LOAD_MORE));
                    mDataManager.addLast(articles);
                    mDataManager.addLast(DataType.of(BRIDGE_LOAD_MORE));
                }
            }
        });
        mPublicListVM.getShowProgressLD().observe(mMain, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean show) {
                if (show != null) {
                    mPbPublicListLoading.setVisibility(show ? View.VISIBLE : View.GONE);
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
                        if (event.data instanceof Boolean && (Boolean) event.data && mPublicListVM.isFirstLoad()) {
                            doNetWork();
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
        doNetWork();
    }

    private void doNetWork() {
        mPublicListVM.refresh(mTabId);
    }

    public void scrollToTop() {
        RvUtil.scrollToTop(mRecyclerView, 3, 0);
    }

    @Override
    protected void onDestroyView() {
        mRecyclerView = null;
        mPbPublicListLoading = null;
        mVHAdapter = null;
        mDataManager = null;
    }
}
