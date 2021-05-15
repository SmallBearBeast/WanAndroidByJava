package com.bear.wanandroidbyjava.Manager;

import com.bear.wanandroidbyjava.Net.WanOkCallback;
import com.bear.wanandroidbyjava.Net.WanResponce;
import com.bear.wanandroidbyjava.Net.WanTypeToken;
import com.bear.wanandroidbyjava.Net.WanUrl;
import com.example.libbase.Executor.MainThreadExecutor;
import com.example.libokhttp.OkHelper;

public class CollectManager {
    public void loadCollectArticleList() {
        OkHelper.getInstance().getMethod(WanUrl.getCollectArticleListUrl(0), new WanOkCallback<String>(WanTypeToken.STRING_TOKEN) {
            @Override
            protected void onSuccess(WanResponce<String> data) {
                super.onSuccess(data);
            }

            @Override
            protected void onFail() {
                super.onFail();
            }
        });
    }

    public void collectArticle(int articleId, final CollectListener listener) {
        OkHelper.getInstance().postMethod(WanUrl.collectArticleUrl(articleId), new WanOkCallback<String>(WanTypeToken.STRING_TOKEN) {
            @Override
            protected void onSuccess(WanResponce<String> data) {
                if (data != null) {
                    callOnCollect(data.errorCode == WanUrl.SUCCESS_ERROR_CODE);
                }
            }

            @Override
            protected void onFail() {
                callOnCollect(false);
            }

            private void callOnCollect(final boolean success) {
                if (listener != null) {
                    MainThreadExecutor.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onCollect(success);
                        }
                    });
                }
            }
        });
    }

    public void unCollectArticle(int articleId, final UnCollectListener listener) {
        OkHelper.getInstance().postMethod(WanUrl.unCollectArticleUrl(articleId), new WanOkCallback<String>(WanTypeToken.STRING_TOKEN) {
            @Override
            protected void onSuccess(WanResponce<String> data) {
                if (data != null) {
                    callUnOnCollect(data.errorCode == WanUrl.SUCCESS_ERROR_CODE);
                }
            }

            @Override
            protected void onFail() {
                callUnOnCollect(false);
            }

            private void callUnOnCollect(final boolean success) {
                if (listener != null) {
                    MainThreadExecutor.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onUnCollect(success);
                        }
                    });
                }
            }
        });
    }

    public interface CollectListener {
        void onCollect(boolean success);
    }

    public interface UnCollectListener {
        void onUnCollect(boolean success);
    }
}
