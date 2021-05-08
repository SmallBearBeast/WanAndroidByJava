package com.bear.wanandroidbyjava.Net;

import com.bear.wanandroidbyjava.Data.NetBean.ArticleBean;
import com.bear.wanandroidbyjava.Data.NetBean.ArticleListBean;
import com.bear.wanandroidbyjava.Data.NetBean.BannerBean;
import com.bear.wanandroidbyjava.Data.NetBean.LoginBean;
import com.bear.wanandroidbyjava.Data.NetBean.NavBean;
import com.bear.wanandroidbyjava.Data.NetBean.ProjectTabBean;
import com.bear.wanandroidbyjava.Data.NetBean.PublicTabBean;
import com.bear.wanandroidbyjava.Data.NetBean.TreeBean;
import com.bear.wanandroidbyjava.Net.WanResponce;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class WanTypeToken {
    public static final TypeToken<WanResponce<List<BannerBean>>> BANNER_TOKEN = new TypeToken<WanResponce<List<BannerBean>>>(){};
    public static final TypeToken<WanResponce<List<ArticleBean>>> ARTICLE_TOKEN = new TypeToken<WanResponce<List<ArticleBean>>>(){};
    public static final TypeToken<WanResponce<ArticleListBean>> ARTICLE_LIST_TOKEN = new TypeToken<WanResponce<ArticleListBean>>(){};
    public static final TypeToken<WanResponce<List<ProjectTabBean>>> PROJECT_TAB_TOKEN = new TypeToken<WanResponce<List<ProjectTabBean>>>(){};
    public static final TypeToken<WanResponce<List<PublicTabBean>>> PUBLIC_TAB_TOKEN = new TypeToken<WanResponce<List<PublicTabBean>>>(){};
    public static final TypeToken<WanResponce<List<NavBean>>> NAV_TOKEN = new TypeToken<WanResponce<List<NavBean>>>(){};
    public static final TypeToken<WanResponce<List<TreeBean>>> TREE_TOKEN = new TypeToken<WanResponce<List<TreeBean>>>(){};

    public static final TypeToken<WanResponce<LoginBean>> LOGIN_TOKEN = new TypeToken<WanResponce<LoginBean>>(){};
    public static final TypeToken<WanResponce<String>> LOGOUT_TOKEN = new TypeToken<WanResponce<String>>(){};
}
