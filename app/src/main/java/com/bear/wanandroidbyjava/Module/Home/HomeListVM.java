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
import com.example.libbase.Util.StringUtil;
import com.example.liblog.SLog;
import com.example.libokhttp.OkCallback;
import com.example.libokhttp.OkHelper;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class HomeListVM extends ViewModel {
    private static final String TAG = "HomeListVM";
    private int mCurPageIndex = 0;
    private boolean mIsNoLoadMore;
    private boolean mIsFetchingList;
    public List mTotalList = new ArrayList();
    public MutableLiveData<List<Article>> mArticleListLD = new MutableLiveData<>();
    public MutableLiveData<List<Article>> mTopArticleListLD = new MutableLiveData<>();
    public MutableLiveData<Banner> mBannerListLD = new MutableLiveData<>();

    public void fetchBanner() {
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
                        mBannerListLD.postValue(banner);
                        mTotalList.add(0, banner);
                        SLog.d(TAG, "fetchList: banner = " + banner);
                    }
                }
            }

            @Override
            protected void onFail() {
                SLog.d(TAG, "fetchBanner: onFail");
            }
        });
    }

    public void fetchTopArticle() {
        OkHelper.getInstance().getMethod(NetUrl.TOP_ARTICLE, new OkCallback<WanResponce<List<ArticleBean>>>(new TypeToken<WanResponce<List<ArticleBean>>>(){}) {
            @Override
            protected void onSuccess(WanResponce<List<ArticleBean>> data) {
                if (data != null) {
                    SLog.d(TAG, "fetchTopArticle: data.errorCode = " + data.errorCode + (StringUtil.isEmpty(data.errorMsg) ? "" : ", data.errorMsg = " + data.errorMsg));
                    if (data.data != null) {
                        if (CollectionUtil.isEmpty(data.data)) {
                            SLog.d(TAG, "fetchTopArticle: articleBeanList is empty");
                            mTopArticleListLD.postValue(null);
                        } else {
                            List<ArticleBean> articleBeanList = data.data;
                            List<Article> articleList = new ArrayList<>();
                            for (ArticleBean articleBean : articleBeanList) {
                                Article article = articleBean.toArticle();
                                articleList.add(article);
                                article.top = true;
                            }
                            mTopArticleListLD.postValue(articleList);
                            if (!mTotalList.isEmpty()) {
                                if (mTotalList.get(0) instanceof Banner) {
                                    mTotalList.addAll(1, articleList);
                                } else {
                                    mTotalList.addAll(0, articleList);
                                }
                            }

                            SLog.d(TAG, "fetchTopArticle: articleList.size = " + articleList.size());
                            SLog.d(TAG, "fetchTopArticle: articleList = " + articleList);
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
        SLog.d(TAG, "loadMore: mIsNoLoadMore = " + mIsNoLoadMore + ", mIsFetchingList = " + mIsFetchingList);
        if (mIsFetchingList) {
            return;
        }
        if (!mIsNoLoadMore) {
            mCurPageIndex = mCurPageIndex + 1;
            fetchList(mCurPageIndex);
        }
    }

    public boolean canLoadMore() {
        return !mIsNoLoadMore && !mIsFetchingList;
    }

    public void refresh() {
        SLog.d(TAG, "refresh: mIsFetchingList = " + mIsFetchingList);
        if (mIsFetchingList) {
            return;
        }
        mCurPageIndex = 0;
        mIsNoLoadMore = false;
        fetchList(mCurPageIndex);
    }

    private void fetchList(int pageIndex) {
        SLog.d(TAG, "fetchList: pageIndex = " + pageIndex + ", mIsFetchingList = " + mIsFetchingList);
        mIsFetchingList = true;
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
                            mArticleListLD.postValue(articleList);
                            mTotalList.addAll(articleList);
                            SLog.d(TAG, "fetchList: articleList.size = " + articleList.size());
                            SLog.d(TAG, "fetchList: articleList = " + articleList);
                        }
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
