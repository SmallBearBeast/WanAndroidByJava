package com.bear.wanandroidbyjava.Data.NetBean.Dto;

import com.bear.wanandroidbyjava.Data.Bean.UserInfoBean;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserInfoDTO {
    @SerializedName("admin")
    private Boolean admin;
    @SerializedName("chapterTops")
    private List<?> chapterTops;
    @SerializedName("coinCount")
    private Integer coinCount;
    @SerializedName("collectIds")
    private List<Integer> collectIds;
    @SerializedName("email")
    private String email;
    @SerializedName("icon")
    private String icon;
    @SerializedName("id")
    private Integer id;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("password")
    private String password;
    @SerializedName("publicName")
    private String publicName;
    @SerializedName("token")
    private String token;
    @SerializedName("type")
    private Integer type;
    @SerializedName("username")
    private String username;

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public List<?> getChapterTops() {
        return chapterTops;
    }

    public void setChapterTops(List<?> chapterTops) {
        this.chapterTops = chapterTops;
    }

    public Integer getCoinCount() {
        return coinCount;
    }

    public void setCoinCount(Integer coinCount) {
        this.coinCount = coinCount;
    }

    public List<Integer> getCollectIds() {
        return collectIds;
    }

    public void setCollectIds(List<Integer> collectIds) {
        this.collectIds = collectIds;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPublicName() {
        return publicName;
    }

    public void setPublicName(String publicName) {
        this.publicName = publicName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserInfoBean toUserInfo() {
        UserInfoBean userInfoBean = new UserInfoBean();
        userInfoBean.setAdmin(admin != null ? admin : false);
        userInfoBean.setEmail(email);
        userInfoBean.setIcon(icon);
        userInfoBean.setId(id != null ? id : 0);
        userInfoBean.setNickname(nickname);
        userInfoBean.setPublicName(publicName);
        userInfoBean.setUsername(username);
        userInfoBean.setCoinCount(coinCount != null ? coinCount : 0);
        userInfoBean.setType(type != null ? type : 0);
        return userInfoBean;
    }
}
