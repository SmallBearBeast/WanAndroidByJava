package com.bear.wanandroidbyjava.Module.Home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bear.wanandroidbyjava.Bean.Article;
import com.bear.wanandroidbyjava.Bean.Banner;
import com.bear.wanandroidbyjava.NetBean.ArticleBean;
import com.bear.wanandroidbyjava.NetBean.ArticleListBean;
import com.bear.wanandroidbyjava.NetBean.BannerBean;
import com.bear.wanandroidbyjava.NetBean.WanResponce;
import com.bear.wanandroidbyjava.NetUrl;
import com.example.libbase.Util.CollectionUtil;
import com.example.libbase.Util.ExecutorUtil;
import com.example.libbase.Util.NetWorkUtil;
import com.example.libbase.Util.StringUtil;
import com.example.liblog.SLog;
import com.example.libokhttp.OkCallback;
import com.example.libokhttp.OkHelper;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SuppressWarnings({"unchecked", "rawtypes", "BooleanMethodIsAlwaysInverted"})
public class HomeListVM extends ViewModel {
    private static final String TAG = "HomeListVM";
    private static final int START_PAGE_INDEX = 1;
    private int mNextPageIndex = 1;
    private boolean mCanLoadMore;
    private boolean mFetchingData;
    private boolean mHasRefresh = false;
    private CountDownLatch mCountDownLatch;
    private List mTotalList = new CopyOnWriteArrayList();
    private List mLastTotalList = new CopyOnWriteArrayList();
    private MutableLiveData<List<Article>> mArticleListLD = new MutableLiveData<>();
    private MutableLiveData<List> mTotalListLD = new MutableLiveData<>();
    private MutableLiveData<Boolean> mShowProgressLD = new MutableLiveData<>();

    private void fetchBanner() {
        SLog.d(TAG, "fetchBanner: start");
        OkHelper.getInstance().getMethod(NetUrl.BANNER, new OkCallback<WanResponce<List<BannerBean>>>(new TypeToken<WanResponce<List<BannerBean>>>(){}) {
            @Override
            protected void onSuccess(WanResponce<List<BannerBean>> data) {
                if (data != null) {
                    SLog.d(TAG, "fetchBanner: errorCode = " + data.errorCode + (StringUtil.isEmpty(data.errorMsg) ? "" : ", errorMsg = " + data.errorMsg));
                    if (CollectionUtil.isEmpty(data.data)) {
                        SLog.d(TAG, "fetchBanner: bannerBeanList is empty");
                    } else {
                        List<BannerBean> bannerBeanList = data.data;
                        List<String> imageUrlList = new ArrayList<>();
                        List<String> clickUrlList = new ArrayList<>();
                        for (BannerBean bannerBean : bannerBeanList) {
                            imageUrlList.add(bannerBean.imagePath);
                            clickUrlList.add(bannerBean.url);
                        }
                        Banner banner = new Banner();
                        banner.imageUrlList = imageUrlList;
                        banner.clickUrlList = clickUrlList;
                        mTotalList.add(0, banner);
                        SLog.d(TAG, "fetchBanner: banner = " + banner);
                        mCountDownLatch.countDown();
                    }
                }
            }

            @Override
            protected void onFail() {
                SLog.d(TAG, "fetchBanner: onFail");
                mCountDownLatch.countDown();
            }
        });
    }

    private void fetchTopArticles() {
        SLog.d(TAG, "fetchTopArticles: start");
        OkHelper.getInstance().getMethod(NetUrl.TOP_ARTICLE, new OkCallback<WanResponce<List<ArticleBean>>>(new TypeToken<WanResponce<List<ArticleBean>>>(){}) {
            @Override
            protected void onSuccess(WanResponce<List<ArticleBean>> data) {
                if (data != null) {
                    SLog.d(TAG, "fetchTopArticles: errorCode = " + data.errorCode + (StringUtil.isEmpty(data.errorMsg) ? "" : ", errorMsg = " + data.errorMsg));
                    if (data.data != null) {
                        if (CollectionUtil.isEmpty(data.data)) {
                            SLog.d(TAG, "fetchTopArticles: articleBeanList is empty");
                        } else {
                            List<ArticleBean> articleBeanList = data.data;
                            List<Article> articleList = new ArrayList<>();
                            for (ArticleBean articleBean : articleBeanList) {
                                Article article = articleBean.toArticle();
                                articleList.add(article);
                                article.top = true;
                            }
                            if (!mTotalList.isEmpty()) {
                                if (mTotalList.get(0) instanceof Banner) {
                                    mTotalList.addAll(1, articleList);
                                } else {
                                    mTotalList.addAll(0, articleList);
                                }
                            }
                            SLog.d(TAG, "fetchTopArticles: articleListSize = " + articleList.size() + ", articleList = " + articleList);
                            mCountDownLatch.countDown();
                        }
                    }
                }
            }

            @Override
            protected void onFail() {
                SLog.d(TAG, "fetchTopArticles: onFail");
                mCountDownLatch.countDown();
            }
        });
    }

    public void loadMore() {
        if (!NetWorkUtil.isConnected()) {
            SLog.d(TAG, "loadMore: net is unConnected");
            return;
        }
        SLog.d(TAG, "loadMore: mCanLoadMore = " + mCanLoadMore + ", mFetchingData = "
                + mFetchingData + ", mNextPageIndex = " + mNextPageIndex);
        if (!canLoadMore()) {
            return;
        }
        mFetchingData = true;
        fetchNormalArticles(mNextPageIndex);
    }

    public boolean canLoadMore() {
        return mCanLoadMore && !mFetchingData;
    }

    public void refresh() {
        if (!NetWorkUtil.isConnected()) {
            SLog.d(TAG, "refresh: net is unConnected");
            mShowProgressLD.postValue(false);
            return;
        }
        SLog.d(TAG, "refresh: mFetchingData = " + mFetchingData);
        if (mFetchingData) {
            return;
        }
        mFetchingData = true;
        mLastTotalList = mTotalList;
        mTotalList.clear();
        mCountDownLatch = new CountDownLatch(3);
        if (!mHasRefresh) {
            mShowProgressLD.postValue(true);
        }
        ExecutorUtil.execute(new Runnable() {
            @Override
            public void run() {
                fetchBanner();
                fetchTopArticles();
                fetchNormalArticles(START_PAGE_INDEX);
                checkRefreshFinish();
            }
        });
    }

    private void checkRefreshFinish() {
        try {
            mCountDownLatch.await(3, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SLog.d(TAG, "checkRefreshFinish: mTotalListSize = " + mTotalList.size());
            if (CollectionUtil.isEmpty(mTotalList)) {
                mTotalList = mLastTotalList;
            }
            if (!CollectionUtil.isEmpty(mTotalList)) {
                mTotalListLD.postValue(mTotalList);
                mHasRefresh = true;
            }
            mShowProgressLD.postValue(false);
        }
    }

    private void fetchNormalArticles(final int pageIndex) {
        SLog.d(TAG, "fetchNormalArticles: pageIndex = " + pageIndex + ", mFetchingData = " + mFetchingData);
        OkHelper.getInstance().getMethod(NetUrl.getHomeArticleList(pageIndex), new OkCallback<WanResponce<ArticleListBean>>(new TypeToken<WanResponce<ArticleListBean>>(){}) {
            @Override
            protected void onSuccess(WanResponce<ArticleListBean> data) {
                if (data != null) {
                    SLog.d(TAG, "fetchNormalArticles: errorCode = " + data.errorCode + (StringUtil.isEmpty(data.errorMsg) ? "" : ", errorMsg = " + data.errorMsg));
                    if (data.data != null) {
                        if (CollectionUtil.isEmpty(data.data.datas)) {
                            SLog.d(TAG, "fetchNormalArticles: articleBeanList is empty");
                            mCanLoadMore = false;
                            mArticleListLD.postValue(null);
                        } else {
                            mCanLoadMore = true;
                            List<ArticleBean> articleBeanList = data.data.datas;
                            List<Article> articleList = new ArrayList<>();
                            for (ArticleBean articleBean : articleBeanList) {
                                articleList.add(articleBean.toArticle());
                            }
                            mTotalList.addAll(articleList);
                            SLog.d(TAG, "fetchNormalArticles: articleListSize = " + articleList.size() + ", mTotalListSize = " + mTotalList.size() + ", articleList = " + articleList);
                            if (pageIndex == 1) {
                                mCountDownLatch.countDown();
                            } else {
                                mArticleListLD.postValue(articleList);
                            }
                        }
                        mNextPageIndex = pageIndex + 1;
                    }
                }
                mFetchingData = false;
            }

            @Override
            protected void onFail() {
                SLog.d(TAG, "fetchNormalArticles: onFail");
                mFetchingData = false;
                if (pageIndex == 1) {
                    mCountDownLatch.countDown();
                }
            }
        });
    }

    public boolean hasRefresh() {
        return mHasRefresh;
    }

    public List getTotalList() {
        return mTotalList;
    }

    public MutableLiveData<List<Article>> getArticleListLD() {
        return mArticleListLD;
    }

    public MutableLiveData<List> getTotalListLD() {
        return mTotalListLD;
    }

    public MutableLiveData<Boolean> getShowProgressLD() {
        return mShowProgressLD;
    }
}
