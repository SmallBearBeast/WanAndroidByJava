package com.bear.wanandroidbyjava.Module.System.Nav;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bear.wanandroidbyjava.Data.Bean.Nav;
import com.bear.wanandroidbyjava.Data.Bean.Tree;
import com.bear.wanandroidbyjava.Manager.NavManager;
import com.example.libbase.Util.CollectionUtil;
import com.example.libbase.Util.NetWorkUtil;
import com.example.liblog.SLog;

import java.util.List;

public class NavVM extends ViewModel implements NavManager.NavDataListener{
    private static final String TAG = "NavVM";
    private boolean isFirstLoad = true;
    private NavManager navManager = new NavManager();
    private MutableLiveData<Boolean> showProgressLD = new MutableLiveData<>();
    private MutableLiveData<List<Nav>> navLD = new MutableLiveData<>();

    public void loadNavData() {
        navManager.loadDataFromStorage(this);
        if (!NetWorkUtil.isConnected()) {
            SLog.d(TAG, "loadNavData: net is not connected");
            showProgressLD.postValue(false);
            return;
        }
        if (isFirstLoad) {
            showProgressLD.postValue(true);
        }
        navManager.loadDataFromNet(this);
    }

    public boolean isFirstLoad() {
        return isFirstLoad;
    }

    public MutableLiveData<Boolean> getShowProgressLD() {
        return showProgressLD;
    }

    public MutableLiveData<List<Nav>> getNavLD() {
        return navLD;
    }

    @Override
    public void onLoad(List<Nav> navList, boolean fromNet) {
        if (fromNet) {
            if (!CollectionUtil.isEmpty(navList)) {
                navLD.postValue(navList);
                isFirstLoad = false;
            } else {
                List<Nav> lastNavList = navLD.getValue();
                if (CollectionUtil.isEmpty(lastNavList)) {
                    navLD.postValue(null);
                }
            }
            showProgressLD.postValue(false);
        } else {
            if (!CollectionUtil.isEmpty(navList)) {
                navLD.postValue(navList);
            }
        }
    }
}
