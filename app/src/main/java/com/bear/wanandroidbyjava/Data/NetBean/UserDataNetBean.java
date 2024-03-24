package com.bear.wanandroidbyjava.Data.NetBean;

import com.bear.wanandroidbyjava.Data.NetBean.Dto.CoinInfoDTO;
import com.bear.wanandroidbyjava.Data.NetBean.Dto.CollectArticleInfoDTO;
import com.bear.wanandroidbyjava.Data.NetBean.Dto.UserInfoDTO;
import com.google.gson.annotations.SerializedName;

public class UserDataNetBean {
    @SerializedName("coinInfo")
    private CoinInfoDTO coinInfo;
    @SerializedName("collectArticleInfo")
    private CollectArticleInfoDTO collectArticleInfo;
    @SerializedName("userInfo")
    private UserInfoDTO userInfo;

    public CoinInfoDTO getCoinInfo() {
        return coinInfo;
    }

    public void setCoinInfo(CoinInfoDTO coinInfo) {
        this.coinInfo = coinInfo;
    }

    public CollectArticleInfoDTO getCollectArticleInfo() {
        return collectArticleInfo;
    }

    public void setCollectArticleInfo(CollectArticleInfoDTO collectArticleInfo) {
        this.collectArticleInfo = collectArticleInfo;
    }

    public UserInfoDTO getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoDTO userInfo) {
        this.userInfo = userInfo;
    }
}
