package com.bear.wanandroidbyjava.Manager;

import com.bear.wanandroidbyjava.Data.Bean.Article;
import com.bear.wanandroidbyjava.Data.Bean.BannerSet;
import com.bear.wanandroidbyjava.Data.NetBean.ArticleBean;
import com.bear.wanandroidbyjava.Data.NetBean.ArticleListBean;
import com.bear.wanandroidbyjava.Data.NetBean.BannerBean;
import com.bear.wanandroidbyjava.Net.WanUrl;
import com.bear.wanandroidbyjava.Net.WanOkCallback;
import com.bear.wanandroidbyjava.Net.WanResponce;
import com.bear.wanandroidbyjava.Net.WanTypeToken;
import com.bear.wanandroidbyjava.Storage.HomeStorage;
import com.bear.wanandroidbyjava.Tool.Helper.DataHelper;
import com.example.libbase.Executor.BgThreadExecutor;
import com.example.libbase.Executor.MainThreadExecutor;
import com.example.libbase.Util.CollectionUtil;
import com.example.libbase.Util.StringUtil;
import com.example.liblog.SLog;
import com.example.libokhttp.OkHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SuppressWarnings({"unchecked", "rawtypes"})
public class HomeManager {
    private static final String TAG = "HomeManager";
    private static final int REQUEST_COUNT = 3;
    private static final int FIRST_PAGE_INDEX = 1;
    private static final int TIME_OUT_DURATION = 10;
    private int nextPageIndex = FIRST_PAGE_INDEX;
    private int refreshCompleteCount = 3;
    private CountDownLatch countDownLatch;
    private final List totalDataList = new CopyOnWriteArrayList();
    private final BannerSet topBannerSet = new BannerSet();
    private final List<Article> topArticleList = new CopyOnWriteArrayList<>();
    private final List<Article> normalArticleList = new CopyOnWriteArrayList<>();
    private final List firstPageDataList = new CopyOnWriteArrayList();

    public void loadDataFromStorage(final HomeDataListener listener) {
        SLog.d(TAG, "loadDataFromStorage: ");
        BgThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                List dataList = new ArrayList();
                BannerSet bannerSet = HomeStorage.getBannerSet();
                if (bannerSet != null && !bannerSet.isEmpty()) {
                    dataList.add(bannerSet);
                }
                addBannerSet(bannerSet, false);
                List<Article> articleList = HomeStorage.getTopArticleList();
                if (!CollectionUtil.isEmpty(articleList)) {
                    dataList.addAll(articleList);
                }
                addTopArticleList(articleList, false);
                articleList = HomeStorage.getNormalArticleList();
                if (!CollectionUtil.isEmpty(articleList)) {
                    dataList.addAll(articleList);
                }
                SLog.d(TAG, "loadDataFromStorage: dataList = " + dataList);
                addNormalArticleList(articleList, false);
                addDataList(listener, dataList, false);
            }
        });
    }

    public void loadDataFromNet(final HomeDataListener listener) {
        BgThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                firstPageDataList.clear();
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
        OkHelper.getInstance().getMethod(WanUrl.BANNER_URL, new WanOkCallback<List<BannerBean>>(WanTypeToken.LIST_BANNER_TOKEN) {
            @Override
            protected void onSuccess(WanResponce<List<BannerBean>> data) {
                if (data != null) {
                    SLog.d(TAG, "loadBannerSet: errorCode = " + data.errorCode
                            + (StringUtil.isEmpty(data.errorMsg) ? "" : ", errorMsg = " + data.errorMsg));
                    BannerSet bannerSet = DataHelper.bannerBeanToBannerSet(data.data);
                    SLog.d(TAG, "loadBannerSet: bannerSet = " + bannerSet);
                    if (bannerSet != null && !bannerSet.isEmpty()) {
                        firstPageDataList.add(0, bannerSet);
                        addBannerSet(bannerSet, true);
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
        OkHelper.getInstance().getMethod(WanUrl.TOP_ARTICLE_URL, new WanOkCallback<List<ArticleBean>>(WanTypeToken.LIST_ARTICLE_TOKEN) {
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
                            if (!firstPageDataList.isEmpty()) {
                                if (firstPageDataList.get(0) instanceof BannerSet) {
                                    firstPageDataList.addAll(1, articleList);
                                } else {
                                    firstPageDataList.addAll(0, articleList);
                                }
                            }
                            addTopArticleList(articleList, true);
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
        OkHelper.getInstance().getMethod(WanUrl.getHomeArticleListUrl(FIRST_PAGE_INDEX), new WanOkCallback<ArticleListBean>(WanTypeToken.ARTICLE_LIST_TOKEN) {
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
                            firstPageDataList.addAll(articleList);
                            addNormalArticleList(articleList, true);
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
        OkHelper.getInstance().getMethod(WanUrl.getHomeArticleListUrl(nextPageIndex), new WanOkCallback<ArticleListBean>(WanTypeToken.ARTICLE_LIST_TOKEN) {
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
                        callHomeDataLoadMoreListener(listener, articleList);
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
                    ", firstPageDataList.size = " + firstPageDataList.size());
            if (refreshCompleteCount == 0) {
                addDataList(listener, firstPageDataList, true);
            }
        }
    }

    private void completeOneLoadTask() {
        refreshCompleteCount--;
        countDownLatch.countDown();
    }

    private void callHomeDataRefreshListener(final HomeDataListener listener, final boolean fromNet) {
        if (listener == null) {
            return;
        }
        MainThreadExecutor.post(new Runnable() {
            @Override
            public void run() {
                listener.onRefresh(new ArrayList(totalDataList), fromNet);
            }
        });
    }

    private void callHomeDataLoadMoreListener(final HomeDataListener listener, final List<Article> articleList) {
        if (listener == null) {
            return;
        }
        MainThreadExecutor.post(new Runnable() {
            @Override
            public void run() {
                listener.onLoadMore(articleList);
            }
        });
    }

    private synchronized void addBannerSet(BannerSet bannerSet, boolean fromNet) {
        if (bannerSet == null || bannerSet.isEmpty()) {
            return;
        }
        if (fromNet) {
            topBannerSet.clear();
            topBannerSet.add(bannerSet);
        } else if (topBannerSet.isEmpty()) {
            topBannerSet.add(bannerSet);
        }
    }

    private synchronized void addTopArticleList(List<Article> articleList, boolean fromNet) {
        if (CollectionUtil.isEmpty(articleList)) {
            return;
        }
        if (fromNet) {
            topArticleList.clear();
            topArticleList.addAll(articleList);
        } else if (topArticleList.isEmpty()) {
            topArticleList.addAll(articleList);
        }
    }

    private synchronized void addNormalArticleList(List<Article> articleList, boolean fromNet) {
        if (CollectionUtil.isEmpty(articleList)) {
            return;
        }
        if (fromNet) {
            normalArticleList.clear();
            normalArticleList.addAll(articleList);
        } else if (normalArticleList.isEmpty()) {
            normalArticleList.addAll(articleList);
        }
    }

    private synchronized void addDataList(HomeDataListener listener, List dataList, boolean fromNet) {
        if (CollectionUtil.isEmpty(dataList)) {
            return;
        }
        if (fromNet) {
            totalDataList.clear();
            totalDataList.addAll(dataList);
        } else if (totalDataList.isEmpty()) {
            totalDataList.addAll(dataList);
        }
        callHomeDataRefreshListener(listener, fromNet);
    }

    public void saveBannerSet() {
        BgThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                HomeStorage.saveBannerSet(topBannerSet);
            }
        });
    }

    public void saveTopArticleList() {
        BgThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                HomeStorage.saveTopArticleList(topArticleList);
            }
        });
    }

    public void saveNormalArticleList() {
        BgThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                HomeStorage.saveNormalArticleList(normalArticleList);
            }
        });
    }

    public List getTotalDataList() {
        return totalDataList;
    }

    public List getFirstPageDataList() {
        return firstPageDataList;
    }

    public BannerSet getTopBannerSet() {
        return topBannerSet;
    }

    public List<Article> getTopArticleList() {
        return topArticleList;
    }

    public List<Article> getNormalArticleList() {
        return normalArticleList;
    }

    public interface HomeDataListener {
        void onRefresh(List dataList, boolean fromNet);

        void onLoadMore(List<Article> articleList);
    }
}
