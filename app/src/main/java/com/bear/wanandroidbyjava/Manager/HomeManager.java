package com.bear.wanandroidbyjava.Manager;

import com.bear.wanandroidbyjava.Data.Bean.Article;
import com.bear.wanandroidbyjava.Data.Bean.BannerSet;
import com.bear.wanandroidbyjava.Data.NetBean.ArticleBean;
import com.bear.wanandroidbyjava.Data.NetBean.ArticleListBean;
import com.bear.wanandroidbyjava.Data.NetBean.BannerBean;
import com.bear.wanandroidbyjava.Net.NetUrl;
import com.bear.wanandroidbyjava.Net.WanOkCallback;
import com.bear.wanandroidbyjava.Net.WanResponce;
import com.bear.wanandroidbyjava.Net.WanTypeToken;
import com.bear.wanandroidbyjava.Storage.HomeStorage;
import com.bear.wanandroidbyjava.Tool.Help.DataHelper;
import com.example.libbase.Util.CollectionUtil;
import com.example.libbase.Util.ExecutorUtil;
import com.example.libbase.Util.StringUtil;
import com.example.liblog.SLog;
import com.example.libokhttp.OkHelper;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SuppressWarnings({"unchecked", "rawtypes", "BooleanMethodIsAlwaysInverted"})
public class HomeManager {
    private static final String TAG = "HomeManager";
    private static final int REQUEST_COUNT = 3;
    private static final int FIRST_PAGE_INDEX = 1;
    private static final int TIME_OUT_DURATION = 10;
    private int nextPageIndex = FIRST_PAGE_INDEX;
    private int refreshCompleteCount = 3;
    private CountDownLatch countDownLatch;
    private List totalDataList = new CopyOnWriteArrayList();
    private List tempTotalDataList = new CopyOnWriteArrayList();

    public void loadDataFromStorage(final HomeDataListener listener) {
        ExecutorUtil.execute(new Runnable() {
            @Override
            public void run() {
                BannerSet bannerSet = HomeStorage.getBannerSet();
                if (bannerSet != null) {
                    totalDataList.add(bannerSet);
                }
                List<Article> topArticleList = HomeStorage.getTopArticleList();
                if (!CollectionUtil.isEmpty(topArticleList)) {
                    totalDataList.addAll(topArticleList);
                }
                List<Article> normalArticleList = HomeStorage.getNormalArticleList();
                if (!CollectionUtil.isEmpty(normalArticleList)) {
                    totalDataList.addAll(normalArticleList);
                }
                SLog.d(TAG, "loadDataFromStorage: totalDataList = " + totalDataList);
                if (listener != null) {
                    listener.onRefresh(totalDataList, false);
                }
            }
        });
    }

    public void loadAllData(final HomeDataListener listener) {
        ExecutorUtil.execute(new Runnable() {
            @Override
            public void run() {
                tempTotalDataList.clear();
                countDownLatch = new CountDownLatch(REQUEST_COUNT);
                refreshCompleteCount = REQUEST_COUNT;
                nextPageIndex = FIRST_PAGE_INDEX;
                loadBannerSet();
                loadTopArticle();
                loadFirstNormalArticle();
                checkRefreshFinish(listener);
            }
        });
    }

    private void loadBannerSet() {
        SLog.d(TAG, "loadBannerSet: start");
        OkHelper.getInstance().getMethod(NetUrl.BANNER, new WanOkCallback<List<BannerBean>>(WanTypeToken.BANNER_TOKEN) {
            @Override
            protected void onSuccess(WanResponce<List<BannerBean>> data) {
                if (data != null) {
                    SLog.d(TAG, "loadBannerSet: errorCode = " + data.errorCode
                            + (StringUtil.isEmpty(data.errorMsg) ? "" : ", errorMsg = " + data.errorMsg));
                    BannerSet bannerSet = DataHelper.bannerBeanToBannerSet(data.data);
                    SLog.d(TAG, "loadBannerSet: bannerSet = " + bannerSet);
                    if (bannerSet != null) {
                        tempTotalDataList.add(0, bannerSet);
                        HomeStorage.saveBannerSet(bannerSet);
                    }
                    completeOneLoadTask();
                }
            }

            @Override
            protected void onFail() {
                SLog.d(TAG, "loadBannerSet: onFail");
                countDownLatch.countDown();
            }
        });
    }

    private void loadTopArticle() {
        SLog.d(TAG, "loadTopArticle: start");
        OkHelper.getInstance().getMethod(NetUrl.TOP_ARTICLE, new WanOkCallback<List<ArticleBean>>(WanTypeToken.ARTICLE_TOKEN) {
            @Override
            protected void onSuccess(WanResponce<List<ArticleBean>> data) {
                if (data != null) {
                    SLog.d(TAG, "loadTopArticle: errorCode = " + data.errorCode +
                            (StringUtil.isEmpty(data.errorMsg) ? "" : ", errorMsg = " + data.errorMsg));
                    if (data.data != null) {
                        List<Article> articleList = DataHelper.articleBeanToArticle(data.data, true);
                        SLog.d(TAG, "loadTopArticle: articleList.size = " + articleList.size() +
                                ", articleList = " + articleList);
                        if (!CollectionUtil.isEmpty(articleList)) {
                            if (!tempTotalDataList.isEmpty()) {
                                if (tempTotalDataList.get(0) instanceof BannerSet) {
                                    tempTotalDataList.addAll(1, articleList);
                                } else {
                                    tempTotalDataList.addAll(0, articleList);
                                }
                            }
                            HomeStorage.saveTopArticleList(articleList);
                        }
                        completeOneLoadTask();
                    }
                }
            }

            @Override
            protected void onFail() {
                SLog.d(TAG, "loadTopArticle: onFail");
                countDownLatch.countDown();
            }
        });
    }

    private void loadFirstNormalArticle() {
        SLog.d(TAG, "loadFirstNormalArticle: pageIndex = " + FIRST_PAGE_INDEX);
        OkHelper.getInstance().getMethod(NetUrl.getHomeArticleList(FIRST_PAGE_INDEX), new WanOkCallback<ArticleListBean>(WanTypeToken.ARTICLE_LIST_TOKEN) {
            @Override
            protected void onSuccess(WanResponce<ArticleListBean> data) {
                if (data != null) {
                    SLog.d(TAG, "loadFirstNormalArticle: errorCode = " + data.errorCode +
                            (StringUtil.isEmpty(data.errorMsg) ? "" : ", errorMsg = " + data.errorMsg));
                    if (data.data != null) {
                        List<Article> articleList = DataHelper.articleBeanToArticle(data.data.datas);
                        SLog.d(TAG, "loadFirstNormalArticle: articleList.size = " + articleList.size()
                                + ", articleList = " + articleList);
                        if (!CollectionUtil.isEmpty(articleList)) {
                            tempTotalDataList.addAll(articleList);
                            HomeStorage.saveNormalArticleList(articleList);
                        }
                        completeOneLoadTask();
                        nextPageIndex++;
                    }
                }
            }

            @Override
            protected void onFail() {
                SLog.d(TAG, "loadNormalArticle: onFail");
                countDownLatch.countDown();
            }
        });
    }

    public void loadMoreNormalArticle(final HomeDataListener listener) {
        SLog.d(TAG, "loadMoreNormalArticle: nextPageIndex = " + nextPageIndex);
        OkHelper.getInstance().getMethod(NetUrl.getHomeArticleList(nextPageIndex), new WanOkCallback<ArticleListBean>(WanTypeToken.ARTICLE_LIST_TOKEN) {
            @Override
            protected void onSuccess(WanResponce<ArticleListBean> data) {
                if (data != null) {
                    SLog.d(TAG, "loadMoreNormalArticle: errorCode = " + data.errorCode +
                            (StringUtil.isEmpty(data.errorMsg) ? "" : ", errorMsg = " + data.errorMsg));
                    if (data.data != null) {
                        List<Article> articleList = DataHelper.articleBeanToArticle(data.data.datas);
                        SLog.d(TAG, "loadMoreNormalArticle: articleListSize = " + articleList.size() +
                                ", totalDataListSize = " + totalDataList.size() + ", articleList = " + articleList);
                        if (!CollectionUtil.isEmpty(articleList)) {
                            totalDataList.addAll(articleList);
                        }
                        if (listener != null) {
                            listener.onLoadMore(articleList);
                        }
                        nextPageIndex++;
                    }
                }
            }

            @Override
            protected void onFail() {
                SLog.d(TAG, "loadMoreNormalArticle: onFail");
            }
        });
    }

    private void checkRefreshFinish(final HomeDataListener listener) {
        try {
            countDownLatch.await(TIME_OUT_DURATION, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SLog.d(TAG, "checkRefreshFinish: refreshCompleteCount = " + refreshCompleteCount +
                    ", tempTotalDataList.size = " + tempTotalDataList.size());
            if (refreshCompleteCount == 0 && !CollectionUtil.isEmpty(tempTotalDataList)) {
                totalDataList.clear();
                totalDataList.addAll(tempTotalDataList);
            }
            if (listener != null) {
                listener.onRefresh(totalDataList, true);
            }
        }
    }

    private void completeOneLoadTask() {
        refreshCompleteCount--;
        countDownLatch.countDown();
    }

    public List getTotalDataList() {
        return totalDataList;
    }

    public interface HomeDataListener {
        void onRefresh(List dataList, boolean fromNet);

        void onLoadMore(List<Article> articleList);
    }
}
