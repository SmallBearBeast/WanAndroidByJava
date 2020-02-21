package com.bear.wanandroidbyjava.Module.Project;

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

public class ProjectListVM extends ViewModel {
    private static final String TAG = "ProjectListVM";
    private boolean mIsNoLoadMore;
    private boolean mIsFetchingList;
    private int mNextPageIndex = 1;
    public List mTotalList = new ArrayList();
    public MutableLiveData<List<Article>> mProjectArticleListLD = new MutableLiveData<>();

    public void refresh(int cid) {
        if (!NetWorkUtil.isConnected()) {
            SLog.d(TAG, "refresh: net is unConnected");
            return;
        }
        SLog.d(TAG, "refresh: cid = " + cid + ", mIsFetchingList = " + mIsFetchingList + ", mNextPageIndex = " + mNextPageIndex);
        if (mIsFetchingList) {
            return;
        }
        mIsFetchingList = true;
        fetchProject(cid, 1);
    }

    public void loadMore(int cid) {
        if (!NetWorkUtil.isConnected()) {
            SLog.d(TAG, "loadMore: net is unConnected");
            return;
        }
        SLog.d(TAG, "refresh: cid = " + cid + ", mIsFetchingList = " + mIsFetchingList
                + ", mIsNoLoadMore = " + mIsNoLoadMore + ", mNextPageIndex = " + mNextPageIndex);
        if (!canLoadMore()) {
            return;
        }
        mIsFetchingList = true;
        fetchProject(cid, mNextPageIndex);
    }

    private void fetchProject(int cid, final int pageIndex) {
        SLog.d(TAG, "fetchProject: cid = " + cid + ", pageIndex = " + pageIndex);
        OkHelper.getInstance().getMethod(NetUrl.getProjectArticleList(cid, pageIndex), new OkCallback<WanResponce<ArticleListBean>>(new TypeToken<WanResponce<ArticleListBean>>() {}) {
            @Override
            protected void onSuccess(WanResponce<ArticleListBean> data) {
                if (data != null) {
                    SLog.d(TAG, "fetchProject: data.errorCode = " + data.errorCode + (StringUtil.isEmpty(data.errorMsg) ? "" : ", data.errorMsg = " + data.errorMsg));
                    if (data.data != null) {
                        if (CollectionUtil.isEmpty(data.data.datas)) {
                            SLog.d(TAG, "fetchProject: articleBeanList is empty, mIsNoLoadMore is true");
                            mIsNoLoadMore = true;
                            mProjectArticleListLD.postValue(null);
                        } else {
                            List<ArticleBean> articleBeanList = data.data.datas;
                            List<Article> articleList = new ArrayList<>();
                            for (ArticleBean articleBean : articleBeanList) {
                                articleList.add(articleBean.toArticle());
                            }
                            mProjectArticleListLD.postValue(articleList);
                            mTotalList.addAll(articleList);
                            if (pageIndex == 1) {
                                SLog.d(TAG, "fetchProject: pageIndex is 1, mIsNoLoadMore is false");
                                mIsNoLoadMore = false;
                            }
                            SLog.d(TAG, "fetchProject: articleList.size = " + articleList.size() + ", mTotalList.size = " + mTotalList.size() + ", articleList = " + articleList);
                        }
                        mNextPageIndex = pageIndex + 1;
                    }
                }
                mIsFetchingList = false;
            }

            @Override
            protected void onFail() {
                SLog.d(TAG, "fetchProject: onFail");
                mIsFetchingList = false;
            }
        });
    }

    public boolean canLoadMore() {
        return !mIsNoLoadMore && !mIsFetchingList;
    }
}
