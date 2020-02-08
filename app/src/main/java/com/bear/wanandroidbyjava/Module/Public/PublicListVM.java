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
        if (mIsFetchingList) {
            return;
        }
        fetchTabArticle(id, 1);
    }

    public void loadMore(int id) {
        if (!NetWorkUtil.isConnected()) {
            SLog.d(TAG, "loadMore: net is unConnected");
            return;
        }
        if (!canLoadMore()) {
            return;
        }
        fetchTabArticle(id, mNextPageIndex);
    }

    private void fetchTabArticle(int id, final int pageIndex) {
        SLog.d(TAG, "fetchTabArticle: id = " + id + ", pageIndex = " + pageIndex);
        mIsFetchingList = true;
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
                            SLog.d(TAG, "fetchTabArticle: articleList.size = " + articleList.size());
                            SLog.d(TAG, "fetchTabArticle: articleList = " + articleList);
                        }
                        mNextPageIndex = pageIndex + 1;
                    }
                }
                mIsFetchingList = false;
                if (pageIndex == 0) {
                    mIsNoLoadMore = false;
                }
            }

            @Override
            protected void onFail() {
                SLog.d(TAG, "fetchTabArticle: onFail");
                mIsFetchingList = false;
                if (pageIndex == 0) {
                    mIsNoLoadMore = false;
                }
            }
        });
    }

    public boolean canLoadMore() {
        return !mIsNoLoadMore && !mIsFetchingList;
    }
}
