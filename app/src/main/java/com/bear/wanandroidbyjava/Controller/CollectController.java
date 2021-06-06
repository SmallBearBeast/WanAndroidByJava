package com.bear.wanandroidbyjava.Controller;

import com.bear.wanandroidbyjava.Data.NetBean.ArticleBean;
import com.bear.wanandroidbyjava.Module.Collect.CollectInfo;
import com.bear.wanandroidbyjava.Net.WanOkCallback;
import com.bear.wanandroidbyjava.Net.WanResponce;
import com.bear.wanandroidbyjava.Net.WanTypeToken;
import com.bear.wanandroidbyjava.Net.WanUrl;
import com.example.libbase.Executor.MainThreadExecutor;
import com.example.libokhttp.OkHelper;

import java.util.HashMap;
import java.util.Map;

public class CollectController {
    private static final String TITLE = "title";
    private static final String AUTHOR = "author";
    private static final String LINK = "link";
    private static final String ORIGINID = "originId";
    private static final String NAME = "name";
    private static final String ID = "id";

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

    public void collectOutArticle(String title, String author, String link, final OutCollectListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put(TITLE, title);
//        map.put(AUTHOR, author);
        map.put(LINK, link);
        OkHelper.getInstance().postMethod(WanUrl.COLLECT_OUT_ARTICLE_URL, map, new WanOkCallback<ArticleBean>(WanTypeToken.ARTICLE_TOKEN) {
            @Override
            protected void onSuccess(WanResponce<ArticleBean> data) {
                if (data != null && data.data != null) {
                    CollectInfo collectInfo = data.data.toArticle().toCollectInfo();
                    callOnCollect(true, collectInfo);
                }
            }

            @Override
            protected void onFail() {
                callOnCollect(false, null);
            }

            private void callOnCollect(final boolean success, final CollectInfo collectInfo) {
                if (listener != null) {
                    MainThreadExecutor.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onOutCollect(success, collectInfo);
                        }
                    });
                }
            }
        });
    }

    public void unCollectOutArticleUrl(int id, int originId, final UnCollectListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put(ORIGINID, String.valueOf(originId));
        OkHelper.getInstance().postMethod(WanUrl.unCollectOutArticleUrl(id), map, new WanOkCallback<String>(WanTypeToken.STRING_TOKEN) {
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

    public void collectLinkUrl(String name, String link, final CollectListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put(NAME, name);
        map.put(LINK, link);
        OkHelper.getInstance().postMethod(WanUrl.COLLECT_LINK_URL, map, new WanOkCallback<String>(WanTypeToken.STRING_TOKEN) {
            @Override
            protected void onSuccess(WanResponce<String> data) {
                super.onSuccess(data);
            }

            @Override
            protected void onFail() {
                super.onFail();
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

    public void unCollectLinkUrl(int id, final UnCollectListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put(ID, String.valueOf(id));
        OkHelper.getInstance().postMethod(WanUrl.DELETE_COLLECT_LINK_URL, map, new WanOkCallback<String>(WanTypeToken.STRING_TOKEN) {
            @Override
            protected void onSuccess(WanResponce<String> data) {
                super.onSuccess(data);
            }

            @Override
            protected void onFail() {
                super.onFail();
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

    public interface OutCollectListener {
        void onOutCollect(boolean success, CollectInfo collectInfo);
    }
}
