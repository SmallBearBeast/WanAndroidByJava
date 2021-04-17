package com.bear.wanandroidbyjava.Module.Home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bear.wanandroidbyjava.Data.Bean.Article;
import com.bear.wanandroidbyjava.Manager.HomeManager;
import com.bear.wanandroidbyjava.Storage.KV.SpValHelper;
import com.example.libbase.Util.CollectionUtil;
import com.example.libbase.Util.NetWorkUtil;
import com.example.liblog.SLog;

import java.util.List;

@SuppressWarnings({"unchecked", "rawtypes", "BooleanMethodIsAlwaysInverted"})
public class HomeListVM extends ViewModel implements HomeManager.HomeDataListener {
    private static final String TAG = "HomeListVM";
    public static final byte LOAD_MORE_NET_ERROR = 1;
    public static final byte LOAD_MORE_NO_DATA = 2;
    public static final byte LOAD_MORE_PROGRESS = 3;
    public static final byte REFRESH_NET_ERROR = 4;
    public static final byte REFRESH_NO_DATA = 5;
    public static final byte REFRESH_PROGRESS_SHOW = 6;
    public static final byte REFRESH_PROGRESS_HIDE = 7;
    public static final byte REFRESH_LAYOUT_SHOW = 8;
    public static final byte REFRESH_LAYOUT_HIDE = 9;
    public static final byte REFRESH_LAYOUT_FAIL = 10;
    private boolean isRefreshDone = false;
    private boolean canLoadMore = true;
    private boolean isLoadingNetData = false;
    private boolean isFinishFirstNetRefresh = false;
    private HomeManager homeManager = new HomeManager();
    private MutableLiveData<List> refreshDataListLD = new MutableLiveData<>();
    private MutableLiveData<List<Article>> loadMoreDataListLD = new MutableLiveData<>();
    private MutableLiveData<Byte> refreshStateLD = new MutableLiveData<>();
    private MutableLiveData<Byte> loadMoreStateLD = new MutableLiveData<>();

    public void refresh(boolean includeStorage) {
        if (includeStorage) {
            homeManager.loadDataFromStorage(this);
        }
        SLog.d(TAG, "refresh: includeStorage = " + includeStorage + ", isLoadingNetData = " + isLoadingNetData);
        if (!NetWorkUtil.isConnected()) {
            SLog.d(TAG, "refresh: net is not connected");
            if (!SpValHelper.hasHomeStorageData.get()) {
                setRefreshState(REFRESH_NET_ERROR);
            } else if (!includeStorage) {
                setRefreshState(REFRESH_LAYOUT_FAIL);
            }
            return;
        }
        if (isLoadingNetData) {
            return;
        }
        isLoadingNetData = true;
        setRefreshState(SpValHelper.hasHomeStorageData.get() ? REFRESH_LAYOUT_SHOW : REFRESH_PROGRESS_SHOW);
        homeManager.loadDataFromNet(this);
    }

    public void loadMore() {
        SLog.d(TAG, "loadMore: canLoadMore = " + canLoadMore + ", isLoadingNetData = " + isLoadingNetData);
        if (isLoadingNetData) {
            return;
        }
        if (!canLoadMore) {
            setLoadMoreState(LOAD_MORE_NO_DATA);
            return;
        }
        if (!NetWorkUtil.isConnected()) {
            SLog.d(TAG, "loadMore: net is unConnected");
            setLoadMoreState(LOAD_MORE_NET_ERROR);
            return;
        }
        setLoadMoreState(LOAD_MORE_PROGRESS);
        isLoadingNetData = true;
        homeManager.loadMoreNormalArticle(this);
    }

    private void setRefreshState(byte curRefreshState) {
        refreshStateLD.setValue(curRefreshState);
    }

    private void setLoadMoreState(byte curLoadMoreState) {
        Byte lastLoadMoreState = loadMoreStateLD.getValue();
        if (lastLoadMoreState == null || lastLoadMoreState != curLoadMoreState) {
            loadMoreStateLD.setValue(curLoadMoreState);
        }
    }

    public void refreshViewModel() {
        if (!CollectionUtil.isEmpty(getTotalDataList())) {
            refreshDataListLD.setValue(getTotalDataList());
        }
        setSelfValue(refreshStateLD);
        setSelfValue(loadMoreStateLD);
    }

    private List getTotalDataList() {
        return homeManager.getTotalDataList();
    }

    private void setSelfValue(MutableLiveData liveData) {
        Object value = liveData.getValue();
        if (value != null) {
            liveData.setValue(value);
        }
    }

    public boolean canLoadMore() {
        return canLoadMore && !isLoadingNetData;
    }

    public boolean isFinishFirstNetRefresh() {
        return isFinishFirstNetRefresh;
    }

    public LiveData<List<Article>> getLoadMoreDataListLD() {
        return loadMoreDataListLD;
    }

    public LiveData<List> getRefreshDataListLD() {
        return refreshDataListLD;
    }
    
    public LiveData<Byte> getLoadMoreStateLD() {
        return loadMoreStateLD;
    }

    public LiveData<Byte> getRefreshStateLD() {
        return refreshStateLD;
    }

    @Override
    public void onRefresh(List dataList, boolean fromNet) {
        if (fromNet) {
            isLoadingNetData = false;
            isFinishFirstNetRefresh = true;
            if (!isRefreshDone) {
                isRefreshDone = true;
            }
            setRefreshState(REFRESH_PROGRESS_HIDE);
            setRefreshState(REFRESH_LAYOUT_HIDE);
            if (!CollectionUtil.isEmpty(dataList)) {
                SpValHelper.hasHomeStorageData.set(true);
                refreshDataListLD.setValue(dataList);
            } else {
                List lastDataList = refreshDataListLD.getValue();
                if (CollectionUtil.isEmpty(lastDataList)) {
                    setRefreshState(REFRESH_NO_DATA);
                }
            }
        } else {
            if (!isRefreshDone && !CollectionUtil.isEmpty(dataList)) {
                refreshDataListLD.setValue(dataList);
            }
        }
    }

    @Override
    public void onLoadMore(List<Article> articleList) {
        isLoadingNetData = false;
        canLoadMore = !CollectionUtil.isEmpty(articleList);
        loadMoreDataListLD.setValue(articleList);
        setSelfValue(loadMoreStateLD);
    }
}
