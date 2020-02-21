package com.bear.wanandroidbyjava.Module.Public;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bear.wanandroidbyjava.Bean.Article;
import com.bear.wanandroidbyjava.NetBean.ArticleBean;
import com.bear.wanandroidbyjava.NetBean.ArticleListBean;
import com.bear.wanandroidbyjava.NetBean.WanResponce;
import com.bear.wanandroidbyjava.NetUrl;
import com.example.libbase.Util.CollectionUtil;
import com.example.libbase.Util.NetWorkUtil;
import com.example.libbase.Util.StringUtil;
import com.example.liblog.SLog;
import com.example.libokhttp.OkCallback;
import com.example.libokhttp.OkHelper;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class PublicListVM extends ViewModel {
    private static final String TAG = "PublicListVM";
    private boolean mIsNoLoadMore;
    private boolean mIsFetchingList;
    private int mNextPageIndex = 1;
    public List mTotalList = new ArrayList();
    public MutableLiveData<List<Article>> mArticleListLD = new MutableLiveData<>();

    public void refresh(int id) {
        if (!NetWorkUtil.isConnected()) {
            SLog.d(TAG, "refresh: net is unConnected");
            return;
        }
        SLog.d(TAG, "refresh: id = " + id + ", mIsFetchingList = " + mIsFetchingList + ", mNextPageIndex = " + mNextPageIndex);
        if (mIsFetchingList) {
            return;
        }
        mIsFetchingList = true;
        fetchTabArticle(id, 1);
    }

    public void loadMore(int id) {
        if (!NetWorkUtil.isConnected()) {
            SLog.d(TAG, "loadMore: net is unConnected");
            return;
        }
        SLog.d(TAG, "refresh: id = " + id + ", mIsFetchingList = " + mIsFetchingList
                + ", mIsNoLoadMore = " + mIsNoLoadMore + ", mNextPageIndex = " + mNextPageIndex);
        if (!canLoadMore()) {
            return;
        }
        mIsFetchingList = true;
        fetchTabArticle(id, mNextPageIndex);
    }

    private void fetchTabArticle(int id, final int pageIndex) {
        SLog.d(TAG, "fetchTabArticle: id = " + id + ", pageIndex = " + pageIndex);
        OkHelper.getInstance().getMethod(NetUrl.getPublicArticleList(id, pageIndex), new OkCallback<WanResponce<ArticleListBean>>(new TypeToken<WanResponce<ArticleListBean>>(){}) {
            @Override
            protected void onSuccess(WanResponce<ArticleListBean> data) {
                if (data != null) {
                    SLog.d(TAG, "fetchTabArticle: data.errorCode = " + data.errorCode + (StringUtil.isEmpty(data.errorMsg) ? "" : ", data.errorMsg = " + data.errorMsg));
                    if (data.data != null) {
                        if (CollectionUtil.isEmpty(data.data.datas)) {
                            SLog.d(TAG, "fetchTabArticle: articleBeanList is empty");
                            mArticleListLD.postValue(null);
                            mIsNoLoadMore = true;
                        } else {
                            List<ArticleBean> articleBeanList = data.data.datas;
                            List<Article> articleList = new ArrayList<>();
                            for (ArticleBean articleBean : articleBeanList) {
                                articleList.add(articleBean.toArticle());
                            }
                            mArticleListLD.postValue(articleList);
                            mTotalList.addAll(articleList);
                            SLog.d(TAG, "fetchTabArticle: articleList.size = " + articleList.size() + ", mTotalList.size = " + mTotalList.size() + ", articleList = " + articleList);
                            if (pageIndex == 1) {
                                SLog.d(TAG, "fetchTabArticle: pageIndex is 1, mIsNoLoadMore is false");
                                mIsNoLoadMore = false;
                            }
                        }
                        mNextPageIndex = pageIndex + 1;
                    }
                }
                mIsFetchingList = false;
            }

            @Override
            protected void onFail() {
                SLog.d(TAG, "fetchTabArticle: onFail");
                mIsFetchingList = false;
            }
        });
    }

    public boolean canLoadMore() {
        return !mIsNoLoadMore && !mIsFetchingList;
    }
}
