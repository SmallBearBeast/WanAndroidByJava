package com.bear.wanandroidbyjava.Module.System.Tree;

import android.util.Log;
import android.view.View;

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
import com.bear.wanandroidbyjava.Tool.Case.CaseHelper;
import com.bear.wanandroidbyjava.Tool.Case.CaseView;
import com.example.libbase.Util.CollectionUtil;

import com.example.libframework.Bus.Bus;
import com.example.libframework.Bus.Event;
import com.example.libframework.Bus.EventCallback;

import java.util.List;
import java.util.Set;

public class TreeCom extends ViewComponent<ComponentFrag> implements View.OnClickListener {
    private static final String TAG = "TreeCom";
    private RecyclerView recyclerView;
    private CaseView caseView;
    private VHAdapter vhAdapter;
    private DataManager dataManager;
    private TreeVM treeVM;

    private EventCallback eventCallback = new EventCallback() {
        @Override
        protected void onEvent(Event event) {
            switch (event.eventKey) {
                case EventKey.KEY_NET_CHANGE:
                    if (event.data instanceof Boolean && (Boolean) event.data && !treeVM.isFirstLoadComplete()) {
                        treeVM.loadTreeData(false);
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
        caseView = findViewById(R.id.case_view);
        caseView.setOnClickListener(this);
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
        treeVM.getLoadStateLD().observe(getDependence(), new Observer<Byte>() {
            @Override
            public void onChanged(Byte loadState) {
                Log.d(TAG, "onChanged: loadState = " + loadState);
                if (loadState == null) {
                    return;
                }
                switch (loadState) {
                    case TreeVM.LOAD_NET_ERROR:
                        CaseHelper.showNetError(caseView);
                        break;
                    case TreeVM.LOAD_NO_DATA:
                        CaseHelper.showNoData(caseView);
                        break;
                    case TreeVM.LOAD_PROGRESS_SHOW:
                        CaseHelper.showLoading(caseView);
                        break;
                    case TreeVM.LOAD_PROGRESS_HIDE:
                        CaseHelper.hide(caseView);
                        break;
                }
            }
        });
    }

    private void initBus() {
        Bus.get().register(eventCallback);
    }

    @Override
    protected void onFirstVisible() {
        treeVM.loadTreeData(true);
    }

    public void scrollToTop() {
        RvUtil.scrollToTop(recyclerView, true);
    }

    @Override
    protected void onDestroyView() {
        clear(recyclerView, caseView, vhAdapter, dataManager);
    }

    @Override
    public void onClick(View view) {
        if (caseView == view) {
            treeVM.loadTreeData(false);
        }
    }
}
