package com.bear.wanandroidbyjava.Module.System.Nav;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bear.wanandroidbyjava.Data.Bean.Nav;
import com.bear.wanandroidbyjava.Controller.NavController;
import com.bear.wanandroidbyjava.Storage.KV.SpValHelper;
import com.example.libbase.Util.CollectionUtil;
import com.example.libbase.Util.NetWorkUtil;
import com.example.liblog.SLog;

import java.util.List;

public class NavVM extends ViewModel implements NavController.NavDataListener{
    private static final String TAG = "NavVM";
    public static final byte LOAD_NET_ERROR = 1;
    public static final byte LOAD_NO_DATA = 2;
    public static final byte LOAD_PROGRESS_SHOW = 3;
    public static final byte LOAD_PROGRESS_HIDE = 4;
    private boolean isFirstLoadComplete = false;
    private boolean isLoadDone = false;
    private NavController navController = new NavController();
    private MutableLiveData<Byte> loadStateLD = new MutableLiveData<>();
    private MutableLiveData<List<Nav>> navLD = new MutableLiveData<>();

    public void loadNavData(boolean includeStorage) {
        if (includeStorage) {
            navController.loadDataFromStorage(this);
        }
        if (!NetWorkUtil.isConnected()) {
            SLog.d(TAG, "loadNavData: net is not connected");
            loadStateLD.setValue(LOAD_NET_ERROR);
            return;
        }
        if (!SpValHelper.hasNavStorageData.get()) {
            loadStateLD.setValue(LOAD_PROGRESS_SHOW);
        }
        navController.loadDataFromNet(this);
    }

    public boolean isFirstLoadComplete() {
        return isFirstLoadComplete;
    }

    public MutableLiveData<Byte> getLoadStateLD() {
        return loadStateLD;
    }

    public MutableLiveData<List<Nav>> getNavLD() {
        return navLD;
    }

    @Override
    public void onLoad(List<Nav> navList, boolean fromNet) {
        if (fromNet) {
            if (!isLoadDone) {
                isLoadDone = true;
            }
            isFirstLoadComplete = true;
            loadStateLD.setValue(LOAD_PROGRESS_HIDE);
            if (!CollectionUtil.isEmpty(navList)) {
                SpValHelper.hasNavStorageData.set(true);
                navLD.postValue(navList);
            } else {
                List<Nav> lastNavList = navLD.getValue();
                if (CollectionUtil.isEmpty(lastNavList)) {
                    loadStateLD.setValue(LOAD_NO_DATA);
                }
            }
        } else {
            if (!isLoadDone && !CollectionUtil.isEmpty(navList)) {
                navLD.postValue(navList);
            }
        }
    }
}
