package com.bear.wanandroidbyjava.Module.System.Nav;

import android.util.Log;
import android.view.View;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bear.libcomponent.component.FragmentComponent;
import com.bear.librv.DataManager;
import com.bear.librv.RvDivider;
import com.bear.librv.RvUtil;
import com.bear.librv.VHAdapter;
import com.bear.wanandroidbyjava.Data.Bean.Nav;
import com.bear.wanandroidbyjava.EventKey;
import com.bear.wanandroidbyjava.R;
import com.bear.wanandroidbyjava.Tool.Case.CaseHelper;
import com.bear.wanandroidbyjava.Tool.Case.CaseView;
import com.bear.wanandroidbyjava.Tool.Util.OtherUtil;
import com.example.libbase.Util.CollectionUtil;

import com.example.libbase.Util.DensityUtil;
import com.example.libframework.Bus.Bus;
import com.example.libframework.Bus.Event;
import com.example.libframework.Bus.EventCallback;

import java.util.List;
import java.util.Set;

public class NavCom extends FragmentComponent implements View.OnClickListener {
    private static final String TAG = "NavCom";
    private RecyclerView recyclerView;
    private CaseView caseView;
    private VHAdapter vhAdapter;
    private DataManager dataManager;
    private NavVM navVM;

    private final EventCallback eventCallback = new EventCallback() {
        @Override
        protected void onEvent(Event event) {
            if (EventKey.KEY_NET_CHANGE.equals(event.eventKey)) {
                if (event.data instanceof Boolean && (Boolean) event.data && !navVM.isFirstLoadComplete()) {
                    navVM.loadNavData(false);
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
        initData();
        initBus();
    }

    @Override
    protected void onCreateView() {
        caseView = findViewById(R.id.case_view);
        caseView.setOnClickListener(this);
        vhAdapter = new VHAdapter(getFragment().getViewLifecycleOwner().getLifecycle());
        vhAdapter.register(new NavVHBridge(), Nav.class);
        dataManager = vhAdapter.getDataManager();
        recyclerView = findViewById(R.id.rv_nav_container);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new RvDivider(layoutManager, DensityUtil.dp2Px(20)));
        recyclerView.setAdapter(vhAdapter);
        dataManager.setData(navVM.getNavLD().getValue());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Bus.get().unRegister(eventCallback);
    }

    private void initData() {
        navVM = new ViewModelProvider(getFragment()).get(NavVM.class);
        navVM.getNavLD().observe(getFragment(), new Observer<List<Nav>>() {
            @Override
            public void onChanged(List<Nav> navList) {
                dataManager.setData(navList);
            }
        });
        navVM.getLoadStateLD().observe(getFragment(), new Observer<Byte>() {
            @Override
            public void onChanged(Byte loadState) {
                Log.d(TAG, "onChanged: loadState = " + loadState);
                if (loadState == null) {
                    return;
                }
                switch (loadState) {
                    case NavVM.LOAD_NET_ERROR:
                        CaseHelper.showNetError(caseView);
                        break;
                    case NavVM.LOAD_NO_DATA:
                        CaseHelper.showNoData(caseView);
                        break;
                    case NavVM.LOAD_PROGRESS_SHOW:
                        CaseHelper.showLoading(caseView);
                        break;
                    case NavVM.LOAD_PROGRESS_HIDE:
                        CaseHelper.hide(caseView);
                        break;
                    default:
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
        navVM.loadNavData(true);
    }

    public void scrollToTop() {
        RvUtil.scrollToTop(recyclerView, true);
    }

    @Override
    protected void onDestroyView() {
        OtherUtil.clear(recyclerView, caseView, vhAdapter, dataManager);
    }

    @Override
    public void onClick(View view) {
        if (caseView == view) {
            navVM.loadNavData(false);
        }
    }
}
