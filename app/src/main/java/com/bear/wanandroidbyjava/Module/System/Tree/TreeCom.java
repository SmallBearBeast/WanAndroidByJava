package com.bear.wanandroidbyjava.Module.System.Tree;

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
import com.bear.wanandroidbyjava.Data.Bean.Tree;
import com.bear.wanandroidbyjava.EventKey;
import com.bear.wanandroidbyjava.R;
import com.example.libbase.Util.CollectionUtil;

import com.example.libframework.Bus.Bus;
import com.example.libframework.Bus.Event;
import com.example.libframework.Bus.EventCallback;

import java.util.List;
import java.util.Set;

public class TreeCom extends ViewComponent<ComponentFrag> {
    private RecyclerView mRecyclerView;
    private ProgressBar mPbTreeLoading;
    private VHAdapter mVHAdapter;
    private DataManager mDataManager;
    private TreeVM mTreeVM;

    private EventCallback mEventCallback = new EventCallback() {
        @Override
        protected void onEvent(Event event) {
            switch (event.eventKey) {
                case EventKey.KEY_NET_CHANGE:
                    if (event.data instanceof Boolean && (Boolean) event.data && mTreeVM.isFirstLoad()) {
                        mTreeVM.fetchTree();
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
        mRecyclerView = findViewById(R.id.rv_tree_container);
        mPbTreeLoading = findViewById(R.id.pb_tree_loading);
        mVHAdapter = new VHAdapter(getDependence().getViewLifecycleOwner().getLifecycle());
        mDataManager = mVHAdapter.getDataManager();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getComActivity()));
        mVHAdapter.register(new TreeVHBridge(), Tree.class);
        mRecyclerView.setAdapter(mVHAdapter);
        mDataManager.setData(mTreeVM.getTreeLD().getValue());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Bus.get().unRegister(mEventCallback);
    }

    private void initData() {
        mTreeVM = new ViewModelProvider(getDependence()).get(TreeVM.class);
        mTreeVM.getTreeLD().observe(getDependence(), new Observer<List<Tree>>() {
            @Override
            public void onChanged(List<Tree> trees) {
                mDataManager.setData(trees);
            }
        });
        mTreeVM.getShowProgressLD().observe(getDependence(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean show) {
                if (show != null) {
                    mPbTreeLoading.setVisibility(show ? View.VISIBLE : View.GONE);
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
        mTreeVM.fetchTree();
    }

    public void scrollToTop() {
        RvUtil.scrollToTop(mRecyclerView, true);
    }

    @Override
    protected void onDestroyView() {
        mRecyclerView = null;
        mPbTreeLoading = null;
        mVHAdapter = null;
        mDataManager = null;
    }
}
