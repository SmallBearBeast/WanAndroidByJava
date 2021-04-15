package com.bear.wanandroidbyjava.Module.Home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bear.wanandroidbyjava.Data.Bean.Article;
import com.bear.wanandroidbyjava.Manager.HomeManager;
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
            if (CollectionUtil.isEmpty(getTotalDataList())) {
                postRefreshState(REFRESH_NET_ERROR);
            }
            return;
        }
        if (isLoadingNetData) {
            return;
        }
        isLoadingNetData = true;
        if (!includeStorage && CollectionUtil.isEmpty(getTotalDataList())) {
            postRefreshState(REFRESH_PROGRESS_SHOW);
        }
        homeManager.loadDataFromNet(this);
    }

    public void loadMore() {
        SLog.d(TAG, "loadMore: canLoadMore = " + canLoadMore + ", isLoadingNetData = " + isLoadingNetData);
        if (isLoadingNetData) {
            return;
        }
        if (!canLoadMore) {
            postLoadMoreState(LOAD_MORE_NO_DATA);
            return;
        }
        if (!NetWorkUtil.isConnected()) {
            SLog.d(TAG, "loadMore: net is unConnected");
            postLoadMoreState(LOAD_MORE_NET_ERROR);
            return;
        }
        postLoadMoreState(LOAD_MORE_PROGRESS);
        isLoadingNetData = true;
        homeManager.loadMoreNormalArticle(this);
    }

    private void postRefreshState(byte curRefreshState) {
        Byte lastRefreshState = refreshStateLD.getValue();
        if (lastRefreshState == null || lastRefreshState != curRefreshState) {
            refreshStateLD.postValue(curRefreshState);
        }
    }

    private void postLoadMoreState(byte curLoadMoreState) {
        Byte lastLoadMoreState = loadMoreStateLD.getValue();
        if (lastLoadMoreState == null || lastLoadMoreState != curLoadMoreState) {
            loadMoreStateLD.postValue(curLoadMoreState);
        }
    }

    public void refreshViewModel() {
        if (!CollectionUtil.isEmpty(getTotalDataList())) {
            refreshDataListLD.postValue(getTotalDataList());
        }
        postSelfValue(refreshStateLD);
        postSelfValue(loadMoreStateLD);
    }

    private List getTotalDataList() {
        return homeManager.getTotalDataList();
    }

    private void postSelfValue(MutableLiveData liveData) {
        Object value = liveData.getValue();
        liveData.postValue(value);
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
            if (!CollectionUtil.isEmpty(dataList)) {
                refreshDataListLD.postValue(dataList);
            } else {
                List lastDataList = refreshDataListLD.getValue();
                if (CollectionUtil.isEmpty(lastDataList)) {
                    postRefreshState(REFRESH_NO_DATA);
                }
            }
            postRefreshState(REFRESH_PROGRESS_HIDE);
        } else {
            if (!isRefreshDone) {
                if (!NetWorkUtil.isConnected() && CollectionUtil.isEmpty(dataList)) {
                    postRefreshState(REFRESH_NET_ERROR);
                } else if (!CollectionUtil.isEmpty(dataList)) {
                    refreshDataListLD.postValue(dataList);
                } else {
                    postRefreshState(REFRESH_PROGRESS_SHOW);
                }
            }
        }
    }

    @Override
    public void onLoadMore(List<Article> articleList) {
        isLoadingNetData = false;
        canLoadMore = !CollectionUtil.isEmpty(articleList);
        loadMoreDataListLD.postValue(articleList);
        postSelfValue(loadMoreStateLD);
    }
}
