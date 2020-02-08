package com.bear.wanandroidbyjava.Module.System.Tree;

import android.view.View;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bear.wanandroidbyjava.Bean.Tree;
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

public class TreeCom extends FragComponent {
    private RecyclerView mRecyclerView;
    private VHAdapter mVhAdapter;
    private DataManager mDataManager;
    private TreeVM mTreeVM;
    private boolean mFirstLoad;

    @Override
    protected void onCreate() {
        super.onCreate();
        initData();
        initBus();
    }

    @Override
    protected void onCreateView(View contentView) {
        mRecyclerView = findViewById(R.id.rv_tree_container);
        mVhAdapter = new VHAdapter();
        mDataManager = mVhAdapter.getDataManager();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mComActivity));
        mVhAdapter.register(new TreeVHBridge(), Tree.class);
        mRecyclerView.setAdapter(mVhAdapter);
        mDataManager.setData(mTreeVM.mTreeLD.getValue());
    }

    private void initData() {
        mTreeVM = new ViewModelProvider(mMain).get(TreeVM.class);
        mTreeVM.mTreeLD.observe(mMain, new Observer<List<Tree>>() {
            @Override
            public void onChanged(List<Tree> trees) {
                mFirstLoad = true;
                mDataManager.setData(trees);
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
                            mTreeVM.fetchTree();
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
        mTreeVM.fetchTree();
    }

    public void scrollToTop() {
        RvUtil.scrollToTop(mRecyclerView, true);
    }
}
