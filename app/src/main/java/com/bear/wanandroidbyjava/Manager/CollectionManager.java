package com.bear.wanandroidbyjava.Manager;

import com.bear.wanandroidbyjava.Net.NetUrl;
import com.bear.wanandroidbyjava.Net.WanOkCallback;
import com.bear.wanandroidbyjava.Net.WanResponce;
import com.bear.wanandroidbyjava.Net.WanTypeToken;
import com.example.libokhttp.OkHelper;

public class CollectionManager {
    public void loadCollectArticle() {
        OkHelper.getInstance().getMethod(NetUrl.getCollectArticleListUrl(0), new WanOkCallback<String>(WanTypeToken.STRING_TOKEN) {
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
}
