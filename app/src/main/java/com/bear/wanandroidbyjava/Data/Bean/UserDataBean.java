package com.bear.wanandroidbyjava.Data.Bean;

import java.io.Serializable;

public class UserDataBean implements Serializable {
    private CoinInfoBean coinInfoBean;

    private CollectArticleInfoBean collectArticleInfoBean;

    private UserInfoBean userInfoBean;

    public UserDataBean(CoinInfoBean coinInfoBean, CollectArticleInfoBean collectArticleInfoBean, UserInfoBean userInfoBean) {
        this.coinInfoBean = coinInfoBean;
        this.collectArticleInfoBean = collectArticleInfoBean;
        this.userInfoBean = userInfoBean;
    }

    public CoinInfoBean getCoinInfoBean() {
        return coinInfoBean;
    }

    public void setCoinInfoBean(CoinInfoBean coinInfoBean) {
        this.coinInfoBean = coinInfoBean;
    }

    public CollectArticleInfoBean getCollectArticleInfoBean() {
        return collectArticleInfoBean;
    }

    public void setCollectArticleInfoBean(CollectArticleInfoBean collectArticleInfoBean) {
        this.collectArticleInfoBean = collectArticleInfoBean;
    }

    public UserInfoBean getUserInfoBean() {
        return userInfoBean;
    }

    public void setUserInfoBean(UserInfoBean userInfoBean) {
        this.userInfoBean = userInfoBean;
    }
}
