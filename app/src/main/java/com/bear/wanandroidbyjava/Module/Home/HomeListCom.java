package com.bear.wanandroidbyjava.Module.Home;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bear.wanandroidbyjava.Bean.Article;
import com.bear.wanandroidbyjava.Bean.Banner;
import com.bear.wanandroidbyjava.EventKey;
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

public class HomeListCom extends FragComponent {
    private static final String TAG = "HomeListCom";
    private static final int LOAD_MORE_OFFSET = 3;
    private static final int VHBRIDGE_LOAD_MORE = 1;
    private static final int VHBRIDGE_NO_MORE_DATA = 2;
    private boolean mFirstLoad;
    private RecyclerView mRecyclerView;
    private VHAdapter mVHAdapter;
    private DataManager mDataManager;
    private HomeListVM mHomeListVM;

    @Override
    protected void onCreate() {
        super.onCreate();
        initData();
        initBus();
    }

    private void initData() {
        mHomeListVM = new ViewModelProvider(mMain).get(HomeListVM.class);
        mHomeListVM.mArticleListLD.observe(mMain, new Observer<List<Article>>() {
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
        mHomeListVM.mBannerListLD.observe(mMain, new Observer<Banner>() {
            @Override
            public void onChanged(Banner banner) {
                if (mDataManager.get(0) instanceof Banner) {
                    mDataManager.update(0, banner);
                } else {
                    mDataManager.addFirst(banner);
                }
            }
        });
        mHomeListVM.mTopArticleListLD.observe(mMain, new Observer<List<Article>>() {
            @Override
            public void onChanged(List<Article> articles) {
                if (!CollectionUtil.isEmpty(articles)) {
                    if (mDataManager.get(0) instanceof Banner) {
                        mDataManager.add(1, articles);
                    } else {
                        mDataManager.addFirst(articles);
                    }
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
                            mHomeListVM.refresh();
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
    protected void onCreateView(View contentView) {
        mRecyclerView = findViewById(R.id.rv_home_container);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mComActivity));
        mVHAdapter = new VHAdapter();
        mDataManager = mVHAdapter.getDataManager();
        mVHAdapter.register(new BannerVHBridge(), Banner.class);
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
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (mHomeListVM.canLoadMore()) {
                    return;
                }
                int lastVisibleItemPos = RvUtil.findLastVisibleItemPosition(recyclerView);
                if (lastVisibleItemPos > mDataManager.size() - LOAD_MORE_OFFSET - 1) {
                    mHomeListVM.loadMore();
                }
            }
        });
        mDataManager.setData(mHomeListVM.mTotalList);
    }

    public void scrollToTop() {
        RvUtil.scrollToTop(mRecyclerView, 3, 0);
    }

    @Override
    protected void onFirstVisible() {
        doNetWork();
    }

    private void doNetWork() {
        mHomeListVM.refresh();
        mHomeListVM.fetchBanner();
        mHomeListVM.fetchTopArticle();
    }
}
