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
import com.example.libbase.Util.NetWorkUtil;
import com.example.libbase.Util.StringUtil;
import com.example.libbase.Util.ThreadUtil;
import com.example.liblog.SLog;
import com.example.libokhttp.OkCallback;
import com.example.libokhttp.OkHelper;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class HomeListVM extends ViewModel {
    private static final String TAG = "HomeListVM";
    private int mNextPageIndex = 1;
    private boolean mIsNoLoadMore;
    private boolean mIsFetchingList;
    private CountDownLatch mCountDownLatch;
    public List mTotalList = new CopyOnWriteArrayList();
    public List mTempList = new CopyOnWriteArrayList();
    public MutableLiveData<List<Article>> mArticleListLD = new MutableLiveData<>();
    public MutableLiveData<List> mTotalListLD = new MutableLiveData<>();

    private void fetchBanner() {
        SLog.d(TAG, "fetchBanner: start");
        OkHelper.getInstance().getMethod(NetUrl.BANNER, new OkCallback<WanResponce<List<BannerBean>>>(new TypeToken<WanResponce<List<BannerBean>>>(){}) {
            @Override
            protected void onSuccess(WanResponce<List<BannerBean>> data) {
                if (data != null) {
                    SLog.d(TAG, "fetchBanner: data.errorCode = " + data.errorCode + (StringUtil.isEmpty(data.errorMsg) ? "" : ", data.errorMsg = " + data.errorMsg));
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
            }
        });
    }

    private void fetchTopArticle() {
        SLog.d(TAG, "fetchTopArticle: start");
        OkHelper.getInstance().getMethod(NetUrl.TOP_ARTICLE, new OkCallback<WanResponce<List<ArticleBean>>>(new TypeToken<WanResponce<List<ArticleBean>>>(){}) {
            @Override
            protected void onSuccess(WanResponce<List<ArticleBean>> data) {
                if (data != null) {
                    SLog.d(TAG, "fetchTopArticle: data.errorCode = " + data.errorCode + (StringUtil.isEmpty(data.errorMsg) ? "" : ", data.errorMsg = " + data.errorMsg));
                    if (data.data != null) {
                        if (CollectionUtil.isEmpty(data.data)) {
                            SLog.d(TAG, "fetchTopArticle: articleBeanList is empty");
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
                            SLog.d(TAG, "fetchTopArticle: articleList.size = " + articleList.size() + ", articleList = " + articleList);
                            mCountDownLatch.countDown();
                        }
                    }
                }
            }

            @Override
            protected void onFail() {
                SLog.d(TAG, "fetchTopArticle: onFail");
            }
        });
    }

    public void loadMore() {
        if (!NetWorkUtil.isConnected()) {
            SLog.d(TAG, "loadMore: net is unConnected");
            return;
        }
        SLog.d(TAG, "loadMore: mIsNoLoadMore = " + mIsNoLoadMore + ", mIsFetchingList = "
                + mIsFetchingList + ", mNextPageIndex = " + mNextPageIndex);
        if (!canLoadMore()) {
            return;
        }
        mIsFetchingList = true;
        fetchList(mNextPageIndex);
    }

    public boolean canLoadMore() {
        return !mIsNoLoadMore && !mIsFetchingList;
    }

    public void refresh() {
        if (!NetWorkUtil.isConnected()) {
            SLog.d(TAG, "refresh: net is unConnected");
            return;
        }
        SLog.d(TAG, "refresh: mIsFetchingList = " + mIsFetchingList);
        if (mIsFetchingList) {
            return;
        }
        mIsFetchingList = true;
        mTempList = mTotalList;
        mTotalList.clear();
        mCountDownLatch = new CountDownLatch(3);
        ThreadUtil.execute(new Runnable() {
            @Override
            public void run() {
                fetchBanner();
                fetchTopArticle();
                fetchList(1);
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
            SLog.d(TAG, "checkRefreshFinish: mTotalList.size = " + mTotalList.size());
            if (CollectionUtil.isEmpty(mTotalList)) {
                mTotalList = mTempList;
            }
            mTotalListLD.postValue(mTotalList);
        }
    }

    private void fetchList(final int pageIndex) {
        SLog.d(TAG, "fetchList: pageIndex = " + pageIndex + ", mIsFetchingList = " + mIsFetchingList);
        OkHelper.getInstance().getMethod(NetUrl.getHomeArticleList(pageIndex), new OkCallback<WanResponce<ArticleListBean>>(new TypeToken<WanResponce<ArticleListBean>>(){}) {
            @Override
            protected void onSuccess(WanResponce<ArticleListBean> data) {
                if (data != null) {
                    SLog.d(TAG, "fetchList: data.errorCode = " + data.errorCode + (StringUtil.isEmpty(data.errorMsg) ? "" : ", data.errorMsg = " + data.errorMsg));
                    if (data.data != null) {
                        if (CollectionUtil.isEmpty(data.data.datas)) {
                            SLog.d(TAG, "fetchList: articleBeanList is empty");
                            mIsNoLoadMore = true;
                            mArticleListLD.postValue(null);
                        } else {
                            List<ArticleBean> articleBeanList = data.data.datas;
                            List<Article> articleList = new ArrayList<>();
                            for (ArticleBean articleBean : articleBeanList) {
                                articleList.add(articleBean.toArticle());
                            }
                            mTotalList.addAll(articleList);
                            SLog.d(TAG, "fetchList: articleList.size = " + articleList.size() + ", mTotalList.size = " + mTotalList.size() + ", articleList = " + articleList);
                            if (pageIndex == 1) {
                                SLog.d(TAG, "fetchList: pageIndex is 1, mIsNoLoadMore is false");
                                mIsNoLoadMore = false;
                                mCountDownLatch.countDown();
                            } else {
                                mArticleListLD.postValue(articleList);
                            }
                        }
                        mNextPageIndex = pageIndex + 1;
                    }
                }
                mIsFetchingList = false;
            }

            @Override
            protected void onFail() {
                SLog.d(TAG, "fetchList: onFail");
                mIsFetchingList = false;
            }
        });
    }
}
