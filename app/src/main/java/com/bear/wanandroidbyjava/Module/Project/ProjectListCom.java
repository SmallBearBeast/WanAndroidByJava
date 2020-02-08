package com.bear.wanandroidbyjava.Module.Project;

import android.view.View;

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

public class ProjectListCom extends FragComponent {
    private static final int LOAD_MORE_OFFSET = 3;
    private static final int VHBRIDGE_LOAD_MORE = 1;
    private static final int VHBRIDGE_NO_MORE_DATA = 2;
    private boolean mFirstLoad;
    private int mTabId;
    private RecyclerView mRecyclerView;
    private VHAdapter mVHAdapter;
    private DataManager mDataManager;
    private ProjectListVM mProjectListVM;

    public ProjectListCom(int tabId) {
        mTabId = tabId;
    }

    @Override
    protected void onCreate() {
        initData();
        initBus();
    }

    @Override
    protected void onCreateView(View contentView) {
        mRecyclerView = findViewById(R.id.rv_tab_article_container);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mComActivity));
        mVHAdapter = new VHAdapter();
        mDataManager = mVHAdapter.getDataManager();
        mVHAdapter.register(new HomeListVHBridge(), Article.class);
        mVHAdapter.register(new LoadMoreVHBridge(), VHBRIDGE_LOAD_MORE);
        mVHAdapter.register(new NoMoreDataVHBridge(), VHBRIDGE_NO_MORE_DATA);
        mVHAdapter.setOnGetDataType(new VHAdapter.OnGetDataType() {
            @Override
            public int getType(Object obj, int pos) {
                if (obj.equals(VHBRIDGE_LOAD_MORE)) {
                    return VHBRIDGE_LOAD_MORE;
                } else if (obj.equals(VHBRIDGE_NO_MORE_DATA)) {
                    return VHBRIDGE_NO_MORE_DATA;
                }
                return -1;
            }
        });
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
        mDataManager.setData(mProjectListVM.mTotalList);
    }

    private void initData() {
        mProjectListVM = new ViewModelProvider(mMain).get(ProjectListVM.class);
        mProjectListVM.mProjectArticleListLD.observe(mMain, new Observer<List<Article>>() {
            @Override
            public void onChanged(List<Article> articles) {
                if (CollectionUtil.isEmpty(articles)) {
                    mDataManager.remove(DataType.of(VHBRIDGE_LOAD_MORE));
                    mDataManager.addLast(DataType.of(VHBRIDGE_NO_MORE_DATA));
                } else {
                    mDataManager.remove(DataType.of(VHBRIDGE_LOAD_MORE));
                    mDataManager.addLast(articles);
                    mDataManager.addLast(DataType.of(VHBRIDGE_LOAD_MORE));
                    mFirstLoad = true;
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
                        if (event.data instanceof Boolean && (Boolean) event.data && !mFirstLoad) {
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
        mProjectListVM.refresh(mTabId);
    }

    public void scrollToTop() {
        RvUtil.scrollToTop(mRecyclerView, 3, 0);
    }
}
