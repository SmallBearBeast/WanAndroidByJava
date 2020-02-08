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
        if (mIsFetchingList) {
            return;
        }
        fetchProject(cid, 1);
    }

    public void loadMore(int cid) {
        if (!NetWorkUtil.isConnected()) {
            SLog.d(TAG, "loadMore: net is unConnected");
            return;
        }
        if (!canLoadMore()) {
            return;
        }
        fetchProject(cid, mNextPageIndex);
    }

    private void fetchProject(int cid, final int pageIndex) {
        OkHelper.getInstance().getMethod(NetUrl.getProjectArticleList(cid, pageIndex), new OkCallback<WanResponce<ArticleListBean>>(new TypeToken<WanResponce<ArticleListBean>>() {}) {
            @Override
            protected void onSuccess(WanResponce<ArticleListBean> data) {
                if (data != null) {
                    SLog.d(TAG, "fetchProject: data.errorCode = " + data.errorCode + (StringUtil.isEmpty(data.errorMsg) ? "" : ", data.errorMsg = " + data.errorMsg));
                    if (data.data != null) {
                        if (CollectionUtil.isEmpty(data.data.datas)) {
                            SLog.d(TAG, "fetchProject: articleBeanList is empty");
                            mProjectArticleListLD.postValue(null);
                            mIsNoLoadMore = true;
                        } else {
                            List<ArticleBean> articleBeanList = data.data.datas;
                            List<Article> articleList = new ArrayList<>();
                            for (ArticleBean articleBean : articleBeanList) {
                                articleList.add(articleBean.toArticle());
                            }
                            mProjectArticleListLD.postValue(articleList);
                            mTotalList.addAll(articleList);
                            SLog.d(TAG, "fetchProject: articleList.size = " + articleList.size());
                            SLog.d(TAG, "fetchProject: articleList = " + articleList);
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
                SLog.d(TAG, "fetchProject: onFail");
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
