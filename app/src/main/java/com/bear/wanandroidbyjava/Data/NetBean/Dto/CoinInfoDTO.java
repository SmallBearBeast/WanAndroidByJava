package com.bear.wanandroidbyjava.Data.NetBean.Dto;

import com.bear.wanandroidbyjava.Data.Bean.CoinInfoBean;
import com.google.gson.annotations.SerializedName;

public class CoinInfoDTO {
    @SerializedName("coinCount")
    private Integer coinCount;
    @SerializedName("level")
    private Integer level;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("rank")
    private String rank;
    @SerializedName("userId")
    private Integer userId;
    @SerializedName("username")
    private String username;

    public Integer getCoinCount() {
        return coinCount;
    }

    public void setCoinCount(Integer coinCount) {
        this.coinCount = coinCount;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public CoinInfoBean toCoinInfo() {
        CoinInfoBean coinInfo = new CoinInfoBean();
        coinInfo.setCoinCount(coinCount != null ? coinCount : 0);
        coinInfo.setLevel(level != null ? level : 0);
        coinInfo.setNickname(nickname);
        coinInfo.setRank(rank);
        coinInfo.setUserId(userId != null ? userId : 0);
        coinInfo.setUsername(username);
        return coinInfo;
    }
}
