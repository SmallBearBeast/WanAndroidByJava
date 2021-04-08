package com.bear.wanandroidbyjava.Module.Home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bear.wanandroidbyjava.Data.Bean.Article;
import com.bear.wanandroidbyjava.Data.Bean.BannerSet;
import com.bear.wanandroidbyjava.Data.NetBean.ArticleBean;
import com.bear.wanandroidbyjava.Data.NetBean.ArticleListBean;
import com.bear.wanandroidbyjava.Data.NetBean.BannerBean;
import com.bear.wanandroidbyjava.Net.WanOkCallback;
import com.bear.wanandroidbyjava.Net.WanResponce;
import com.bear.wanandroidbyjava.Net.WanTypeToken;
import com.bear.wanandroidbyjava.Net.NetUrl;
import com.bear.wanandroidbyjava.Storage.DataBase.WanRoomDataBase;
import com.bear.wanandroidbyjava.Storage.KV.BannerKV;
import com.bear.wanandroidbyjava.Tool.Help.DataHelper;
import com.example.libbase.Util.CollectionUtil;
import com.example.libbase.Util.ExecutorUtil;
import com.example.libbase.Util.NetWorkUtil;
import com.example.libbase.Util.StringUtil;
import com.example.liblog.SLog;
import com.example.libokhttp.OkHelper;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SuppressWarnings({"unchecked", "rawtypes", "BooleanMethodIsAlwaysInverted"})
public class HomeListVM extends ViewModel {
    private static final String TAG = "HomeListVM";
    private static final int START_PAGE_INDEX = 1;
    private static final int TIME_OUT_DURATION = 10;
    private int mNextPageIndex = 1;
    private boolean mCanLoadMore;
    private boolean mIsLoadingData;
    private boolean mIsFinishFirstRefresh = false;
    private CountDownLatch mCountDownLatch;
    private List mTotalDataList = new CopyOnWriteArrayList();
    private List mLastTotalDataList = new CopyOnWriteArrayList();
    private MutableLiveData<List<Article>> mArticleListLD = new MutableLiveData<>();
    private MutableLiveData<List> mTotalListLD = new MutableLiveData<>();
    private MutableLiveData<Boolean> mShowProgressLD = new MutableLiveData<>();

    private void loadBannerSet() {
        SLog.d(TAG, "loadBannerSet: start");
        OkHelper.getInstance().getMethod(NetUrl.BANNER, new WanOkCallback<List<BannerBean>>(WanTypeToken.BANNER_TOKEN) {
            @Override
            protected void onSuccess(WanResponce<List<BannerBean>> data) {
                if (data != null) {
                    SLog.d(TAG, "loadBannerSet: errorCode = " + data.errorCode + (StringUtil.isEmpty(data.errorMsg) ? "" : ", errorMsg = " + data.errorMsg));
                    if (CollectionUtil.isEmpty(data.data)) {
                        SLog.d(TAG, "loadBannerSet: bannerBeanList is empty");
                    } else {
                        BannerSet bannerSet = DataHelper.bannerBeanToBannerSet(data.data);
                        mTotalDataList.add(0, bannerSet);
                        SLog.d(TAG, "loadBannerSet: bannerSet = " + bannerSet);
                        mCountDownLatch.countDown();
                        BannerKV.saveBannerSet(bannerSet);
                    }
                }
            }

            @Override
            protected void onFail() {
                SLog.d(TAG, "loadBannerSet: onFail");
                mCountDownLatch.countDown();
            }
        });
    }

    private void loadTopArticles() {
        SLog.d(TAG, "loadTopArticles: start");
        OkHelper.getInstance().getMethod(NetUrl.TOP_ARTICLE, new WanOkCallback<List<ArticleBean>>(WanTypeToken.ARTICLE_TOKEN) {
            @Override
            protected void onSuccess(WanResponce<List<ArticleBean>> data) {
                if (data != null) {
                    SLog.d(TAG, "loadTopArticles: errorCode = " + data.errorCode + (StringUtil.isEmpty(data.errorMsg) ? "" : ", errorMsg = " + data.errorMsg));
                    if (data.data != null) {
                        if (CollectionUtil.isEmpty(data.data)) {
                            SLog.d(TAG, "loadTopArticles: articleBeanList is empty");
                        } else {
                            List<Article> articleList = DataHelper.articleBeanToArticle(data.data, true);
                            if (!mTotalDataList.isEmpty()) {
                                if (mTotalDataList.get(0) instanceof BannerSet) {
                                    mTotalDataList.addAll(1, articleList);
                                } else {
                                    mTotalDataList.addAll(0, articleList);
                                }
                            }
                            SLog.d(TAG, "loadTopArticles: articleListSize = " + articleList.size() +
                                    ", articleList = " + articleList);
                            mCountDownLatch.countDown();
                            WanRoomDataBase.get().homeDao().insertHomeArticle(articleList);
                        }
                    }
                }
            }

            @Override
            protected void onFail() {
                SLog.d(TAG, "loadTopArticles: onFail");
                mCountDownLatch.countDown();
            }
        });
    }

    public void loadMore() {
        if (!NetWorkUtil.isConnected()) {
            SLog.d(TAG, "loadMore: net is unConnected");
            return;
        }
        SLog.d(TAG, "loadMore: mCanLoadMore = " + mCanLoadMore + ", mIsLoadingData = "
                + mIsLoadingData + ", mNextPageIndex = " + mNextPageIndex);
        if (!canLoadMore()) {
            return;
        }
        mIsLoadingData = true;
        loadNormalArticles(mNextPageIndex);
    }

    public boolean canLoadMore() {
        return mCanLoadMore && !mIsLoadingData;
    }

    public void refresh() {
        if (!NetWorkUtil.isConnected()) {
            SLog.d(TAG, "refresh: net is not connected");
            mShowProgressLD.postValue(false);
            return;
        }
        SLog.d(TAG, "refresh: mIsLoadingData = " + mIsLoadingData);
        if (mIsLoadingData) {
            return;
        }
        mIsLoadingData = true;
        mLastTotalDataList = mTotalDataList;
        mTotalDataList.clear();
        mCountDownLatch = new CountDownLatch(3);
        if (!mIsFinishFirstRefresh) {
            mShowProgressLD.postValue(true);
        }
        ExecutorUtil.execute(new Runnable() {
            @Override
            public void run() {
                loadBannerSet();
                loadTopArticles();
                loadNormalArticles(START_PAGE_INDEX);
                checkRefreshFinish();
            }
        });
    }

    private void checkRefreshFinish() {
        try {
            mCountDownLatch.await(TIME_OUT_DURATION, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SLog.d(TAG, "checkRefreshFinish: mTotalDataListSize = " + mTotalDataList.size());
            if (CollectionUtil.isEmpty(mTotalDataList)) {
                mTotalDataList = mLastTotalDataList;
            }
            if (!CollectionUtil.isEmpty(mTotalDataList)) {
                mTotalListLD.postValue(mTotalDataList);
                mIsFinishFirstRefresh = true;
            }
            mShowProgressLD.postValue(false);
        }
    }

    private void loadNormalArticles(final int pageIndex) {
        SLog.d(TAG, "loadNormalArticles: pageIndex = " + pageIndex + ", mIsLoadingData = " + mIsLoadingData);
        OkHelper.getInstance().getMethod(NetUrl.getHomeArticleList(pageIndex), new WanOkCallback<ArticleListBean>(WanTypeToken.ARTICLE_LIST_TOKEN) {
            @Override
            protected void onSuccess(WanResponce<ArticleListBean> data) {
                if (data != null) {
                    SLog.d(TAG, "loadNormalArticles: errorCode = " + data.errorCode +
                            (StringUtil.isEmpty(data.errorMsg) ? "" : ", errorMsg = " + data.errorMsg));
                    if (data.data != null) {
                        if (CollectionUtil.isEmpty(data.data.datas)) {
                            SLog.d(TAG, "loadNormalArticles: articleBeanList is empty");
                            mCanLoadMore = false;
                            mArticleListLD.postValue(null);
                        } else {
                            mCanLoadMore = true;
                            List<Article> articleList = DataHelper.articleBeanToArticle(data.data.datas);
                            mTotalDataList.addAll(articleList);
                            SLog.d(TAG, "loadNormalArticles: articleListSize = " + articleList.size() +
                                    ", mTotalDataListSize = " + mTotalDataList.size() + ", articleList = " + articleList);
                            if (pageIndex == 1) {
                                mCountDownLatch.countDown();
                                WanRoomDataBase.get().homeDao().insertHomeArticle(articleList);
                            } else {
                                mArticleListLD.postValue(articleList);
                            }
                        }
                        mNextPageIndex = pageIndex + 1;
                    }
                }
                mIsLoadingData = false;
            }

            @Override
            protected void onFail() {
                SLog.d(TAG, "loadNormalArticles: onFail");
                mIsLoadingData = false;
                if (pageIndex == 1) {
                    mCountDownLatch.countDown();
                }
            }
        });
    }

    public boolean isFinishFirstRefresh() {
        return mIsFinishFirstRefresh;
    }

    public List getTotalDataList() {
        return mTotalDataList;
    }

    public MutableLiveData<List<Article>> getArticleListLD() {
        return mArticleListLD;
    }

    public MutableLiveData<List> getTotalDataListLD() {
        return mTotalListLD;
    }

    public MutableLiveData<Boolean> getShowProgressLD() {
        return mShowProgressLD;
    }
}
