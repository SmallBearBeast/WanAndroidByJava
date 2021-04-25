package com.bear.wanandroidbyjava.Module.Public;

import android.util.Pair;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bear.wanandroidbyjava.Data.Bean.Article;
import com.bear.wanandroidbyjava.Data.NetBean.ArticleBean;
import com.bear.wanandroidbyjava.Data.NetBean.ArticleListBean;
import com.bear.wanandroidbyjava.Net.WanOkCallback;
import com.bear.wanandroidbyjava.Net.WanResponce;
import com.bear.wanandroidbyjava.Net.WanTypeToken;
import com.bear.wanandroidbyjava.Net.NetUrl;
import com.bear.wanandroidbyjava.Storage.DataBase.WanRoomDataBase;
import com.example.libbase.Executor.BgThreadExecutor;
import com.example.libbase.Util.CollectionUtil;
import com.example.libbase.Util.NetWorkUtil;
import com.example.libbase.Util.StringUtil;
import com.example.liblog.SLog;
import com.example.libokhttp.OkHelper;

import java.util.ArrayList;
import java.util.List;

public class PublicListVM extends ViewModel {
    private static final String TAG = "PublicListVM";
    private boolean mIsNoLoadMore;
    private boolean mIsFetchingList;
    private int mNextPageIndex = 1;
    private boolean mIsFirstLoad = true;
    private MutableLiveData<Boolean> mShowProgressLD = new MutableLiveData<>();
    private List mTotalList = new ArrayList();
    private MutableLiveData<Pair<Boolean, List<Article>>> mRefreshArticlePairLD = new MutableLiveData<>();
    private MutableLiveData<List<Article>> mLoadMoreArticleLD = new MutableLiveData<>();

    private void refreshFromDb(final int id) {
        BgThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    SLog.d(TAG, "refreshFromDb start");
                    List<Article> articleList = WanRoomDataBase.get().publicDao().queryPublicArticle(id);
                    if (!CollectionUtil.isEmpty(articleList)) {
                        mTotalList.addAll(articleList);
                        mRefreshArticlePairLD.postValue(new Pair<>(false, articleList));
                        mIsFirstLoad = false;
                    }
                } catch (Exception e) {
                    SLog.d(TAG, "refreshFromDb fail");
                }
            }
        });
    }

    public void refresh(int id) {
        refreshFromDb(id);
        if (!NetWorkUtil.isConnected()) {
            SLog.d(TAG, "refresh: net is unConnected");
            return;
        }
        SLog.d(TAG, "refresh: articleId = " + id + ", mIsFetchingList = " + mIsFetchingList + ", mNextPageIndex = " + mNextPageIndex);
        if (mIsFetchingList) {
            return;
        }
        mIsFetchingList = true;
        if (mIsFirstLoad) {
            mShowProgressLD.postValue(true);
        }
        fetchTabArticle(id, 1);
    }

    public void loadMore(int id) {
        if (!NetWorkUtil.isConnected()) {
            SLog.d(TAG, "loadMore: net is unConnected");
            return;
        }
        SLog.d(TAG, "refresh: articleId = " + id + ", mIsFetchingList = " + mIsFetchingList
                + ", mIsNoLoadMore = " + mIsNoLoadMore + ", mNextPageIndex = " + mNextPageIndex);
        if (!canLoadMore()) {
            return;
        }
        mIsFetchingList = true;
        fetchTabArticle(id, mNextPageIndex);
    }

    private void fetchTabArticle(int id, final int pageIndex) {
        SLog.d(TAG, "fetchTabArticle: articleId = " + id + ", pageIndex = " + pageIndex);
        OkHelper.getInstance().getMethod(NetUrl.getPublicArticleList(id, pageIndex), new WanOkCallback<ArticleListBean>(WanTypeToken.ARTICLE_LIST_TOKEN) {
            @Override
            protected void onSuccess(WanResponce<ArticleListBean> data) {
                if (data != null) {
                    SLog.d(TAG, "fetchTabArticle: data.errorCode = " + data.errorCode + (StringUtil.isEmpty(data.errorMsg) ? "" : ", data.errorMsg = " + data.errorMsg));
                    if (data.data != null) {
                        if (CollectionUtil.isEmpty(data.data.datas)) {
                            SLog.d(TAG, "fetchTabArticle: articleBeanList is empty");
                            mLoadMoreArticleLD.postValue(null);
                            mIsNoLoadMore = true;
                        } else {
                            List<ArticleBean> articleBeanList = data.data.datas;
                            List<Article> articleList = new ArrayList<>();
                            for (ArticleBean articleBean : articleBeanList) {
                                articleList.add(articleBean.toArticle());
                            }
                            SLog.d(TAG, "fetchTabArticle: articleList.size = " + articleList.size() + ", mTotalList.size = " + mTotalList.size() + ", articleList = " + articleList);
                            if (pageIndex == 1) {
                                SLog.d(TAG, "fetchTabArticle: pageIndex is 1, mIsNoLoadMore is false");
                                mTotalList.clear();
                                mRefreshArticlePairLD.postValue(new Pair<>(true, articleList));
                                mIsNoLoadMore = false;
                            } else {
                                mLoadMoreArticleLD.postValue(articleList);
                            }
                            mTotalList.addAll(articleList);
                            mIsFirstLoad = false;
                        }
                        mNextPageIndex = pageIndex + 1;
                    }
                }
                mIsFetchingList = false;
                mShowProgressLD.postValue(false);
            }

            @Override
            protected void onFail() {
                SLog.d(TAG, "fetchTabArticle: onFail");
                mIsFetchingList = false;
                mShowProgressLD.postValue(false);
            }
        });
    }

    public void saveTabArticleList(final int publicTabId, final List<Article> publicArticleList) {
        BgThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    SLog.d(TAG, "saveTabArticleList start");
                    WanRoomDataBase.get().publicDao().deletePublicArticle(publicTabId);
                    WanRoomDataBase.get().publicDao().insertPublicArticle(publicTabId, publicArticleList);
                } catch (Exception e) {
                    SLog.d(TAG, "saveTabArticleList fail");
                }
            }
        });
    }

    public boolean canLoadMore() {
        return !mIsNoLoadMore && !mIsFetchingList;
    }

    public boolean isFirstLoad() {
        return mIsFirstLoad;
    }

    public MutableLiveData<Boolean> getShowProgressLD() {
        return mShowProgressLD;
    }

    public List getTotalList() {
        return mTotalList;
    }

    public MutableLiveData<Pair<Boolean, List<Article>>> getRefreshArticlePairLD() {
        return mRefreshArticlePairLD;
    }

    public MutableLiveData<List<Article>> getLoadMoreArticleLD() {
        return mLoadMoreArticleLD;
    }
}
