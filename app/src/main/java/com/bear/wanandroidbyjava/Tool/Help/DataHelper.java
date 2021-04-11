package com.bear.wanandroidbyjava.Tool.Help;

import com.bear.wanandroidbyjava.Data.Bean.Article;
import com.bear.wanandroidbyjava.Data.Bean.BannerSet;
import com.bear.wanandroidbyjava.Data.Bean.Nav;
import com.bear.wanandroidbyjava.Data.Bean.Tree;
import com.bear.wanandroidbyjava.Data.NetBean.ArticleBean;
import com.bear.wanandroidbyjava.Data.NetBean.BannerBean;
import com.bear.wanandroidbyjava.Data.NetBean.NavBean;
import com.bear.wanandroidbyjava.Data.NetBean.TreeBean;
import com.example.libbase.Util.CollectionUtil;

import java.util.ArrayList;
import java.util.List;

public class DataHelper {
    public static List<Article> articleBeanToArticle(List<ArticleBean> articleBeanList, boolean top) {
        if (CollectionUtil.isEmpty(articleBeanList)) {
            return new ArrayList<>();
        }
        List<Article> articleList = new ArrayList<>();
        for (ArticleBean articleBean : articleBeanList) {
            Article article = articleBean.toArticle();
            articleList.add(article);
            article.top = top;
        }
        return articleList;
    }

    public static List<Article> articleBeanToArticle(List<ArticleBean> articleBeanList) {
        return articleBeanToArticle(articleBeanList, false);
    }

    public static BannerSet bannerBeanToBannerSet(List<BannerBean> bannerBeanList) {
        if (CollectionUtil.isEmpty(bannerBeanList)) {
            return null;
        }
        List<String> imageUrlList = new ArrayList<>();
        List<String> clickUrlList = new ArrayList<>();
        for (BannerBean bannerBean : bannerBeanList) {
            imageUrlList.add(bannerBean.imagePath);
            clickUrlList.add(bannerBean.url);
        }
        BannerSet bannerSet = new BannerSet();
        bannerSet.imageUrlList = imageUrlList;
        bannerSet.clickUrlList = clickUrlList;
        return bannerSet;
    }

    public static List<Tree> treeBeanToTree(List<TreeBean> treeBeanList) {
        if (CollectionUtil.isEmpty(treeBeanList)) {
            return new ArrayList<>();
        }
        List<Tree> treeList = new ArrayList<>();
        for (TreeBean treeBean : treeBeanList) {
            treeList.add(treeBean.toTree());
        }
        return treeList;
    }

    public static List<Nav> navBeanToNav(List<NavBean> navBeanList) {
        if (CollectionUtil.isEmpty(navBeanList)) {
            return new ArrayList<>();
        }
        List<Nav> navList = new ArrayList<>();
        for (NavBean navBean : navBeanList) {
            navList.add(navBean.toNav());
        }
        return navList;
    }
}
