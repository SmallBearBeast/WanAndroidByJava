package com.bear.wanandroidbyjava.Module.Home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bear.wanandroidbyjava.Data.Bean.Article;
import com.bear.wanandroidbyjava.Data.Bean.BannerSet;
import com.bear.wanandroidbyjava.Data.NetBean.ArticleBean;
import com.bear.wanandroidbyjava.Data.NetBean.ArticleListBean;
import com.bear.wanandroidbyjava.Data.NetBean.BannerBean;
import com.bear.wanandroidbyjava.Manager.HomeManager;
import com.bear.wanandroidbyjava.Net.NetUrl;
import com.bear.wanandroidbyjava.Net.WanOkCallback;
import com.bear.wanandroidbyjava.Net.WanResponce;
import com.bear.wanandroidbyjava.Net.WanTypeToken;
import com.bear.wanandroidbyjava.Storage.HomeStorage;
import com.bear.wanandroidbyjava.Tool.Help.DataHelper;
import com.example.libbase.Util.CollectionUtil;
import com.example.libbase.Util.ExecutorUtil;
import com.example.libbase.Util.NetWorkUtil;
import com.example.libbase.Util.StringUtil;
import com.example.liblog.SLog;
import com.example.libokhttp.OkHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SuppressWarnings({"unchecked", "rawtypes", "BooleanMethodIsAlwaysInverted"})
public class HomeListVM extends ViewModel implements HomeManager.HomeDataListener {
    private static final String TAG = "HomeListVM";
    private boolean canLoadMore = true;
    private boolean isLoadingData = false;
    private boolean isFinishFirstRefresh = false;
    private HomeManager homeManager = new HomeManager();
    private MutableLiveData<List<Article>> loadMoreArticleListLD = new MutableLiveData<>();
    private MutableLiveData<List> refreshDataListLD = new MutableLiveData<>();
    private MutableLiveData<Boolean> showProgressLD = new MutableLiveData<>();

    public void refresh() {
        homeManager.loadDataFromStorage(this);
        if (!NetWorkUtil.isConnected()) {
            SLog.d(TAG, "refresh: net is not connected");
            showProgressLD.postValue(false);
            return;
        }
        SLog.d(TAG, "refresh: isLoadingData = " + isLoadingData);
        if (isLoadingData) {
            return;
        }
        isLoadingData = true;
        if (!isFinishFirstRefresh) {
            showProgressLD.postValue(true);
        }
        homeManager.loadAllData(this);
    }

    public void loadMore() {
        if (!NetWorkUtil.isConnected()) {
            SLog.d(TAG, "loadMore: net is unConnected");
            return;
        }
        SLog.d(TAG, "loadMore: canLoadMore = " + canLoadMore + ", isLoadingData = " + isLoadingData);
        if (!canLoadMore()) {
            return;
        }
        isLoadingData = true;
        homeManager.loadMoreNormalArticle(this);
    }

    public boolean canLoadMore() {
        return canLoadMore && !isLoadingData;
    }

    public boolean isFinishFirstRefresh() {
        return isFinishFirstRefresh;
    }

    public List getTotalDataList() {
        return homeManager.getTotalDataList();
    }

    public MutableLiveData<List<Article>> getLoadMoreArticleListLD() {
        return loadMoreArticleListLD;
    }

    public MutableLiveData<List> getRefreshDataListLD() {
        return refreshDataListLD;
    }

    public MutableLiveData<Boolean> getShowProgressLD() {
        return showProgressLD;
    }

    @Override
    public void onRefresh(List dataList, boolean fromNet) {
        // TODO: 2021/4/11 The sequence of loading data from Net and Storage
        if (fromNet) {
            isLoadingData = false;
            if (!CollectionUtil.isEmpty(dataList)) {
                refreshDataListLD.postValue(dataList);
                isFinishFirstRefresh = true;
            } else {
                List lastDataList = refreshDataListLD.getValue();
                if (CollectionUtil.isEmpty(lastDataList)) {
                    refreshDataListLD.postValue(null);
                }
            }
            showProgressLD.postValue(false);
        } else {
            if (!CollectionUtil.isEmpty(dataList)) {
                refreshDataListLD.postValue(dataList);
            }
        }
    }

    @Override
    public void onLoadMore(List<Article> articleList) {
        isLoadingData = false;
        if (CollectionUtil.isEmpty(articleList)) {
            canLoadMore = false;
        } else {
            canLoadMore = true;
            loadMoreArticleListLD.postValue(articleList);
        }
    }
}
