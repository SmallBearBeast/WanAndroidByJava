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
    private RecyclerView recyclerView;
    private ProgressBar pbTreeLoading;
    private VHAdapter vhAdapter;
    private DataManager dataManager;
    private TreeVM treeVM;

    private EventCallback eventCallback = new EventCallback() {
        @Override
        protected void onEvent(Event event) {
            switch (event.eventKey) {
                case EventKey.KEY_NET_CHANGE:
                    if (event.data instanceof Boolean && (Boolean) event.data && treeVM.isFirstLoad()) {
                        treeVM.loadTreeData();
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
        recyclerView = findViewById(R.id.rv_tree_container);
        pbTreeLoading = findViewById(R.id.pb_tree_loading);
        vhAdapter = new VHAdapter(getDependence().getViewLifecycleOwner().getLifecycle());
        dataManager = vhAdapter.getDataManager();
        recyclerView.setLayoutManager(new LinearLayoutManager(getComActivity()));
        vhAdapter.register(new TreeVHBridge(), Tree.class);
        recyclerView.setAdapter(vhAdapter);
        dataManager.setData(treeVM.getTreeLD().getValue());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Bus.get().unRegister(eventCallback);
    }

    private void initData() {
        treeVM = new ViewModelProvider(getDependence()).get(TreeVM.class);
        treeVM.getTreeLD().observe(getDependence(), new Observer<List<Tree>>() {
            @Override
            public void onChanged(List<Tree> trees) {
                dataManager.setData(trees);
            }
        });
        treeVM.getShowProgressLD().observe(getDependence(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean show) {
                if (show != null) {
                    pbTreeLoading.setVisibility(show ? View.VISIBLE : View.GONE);
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
        treeVM.loadTreeData();
    }

    public void scrollToTop() {
        RvUtil.scrollToTop(recyclerView, true);
    }

    @Override
    protected void onDestroyView() {
        clear(recyclerView, pbTreeLoading, vhAdapter, dataManager);
    }
}
