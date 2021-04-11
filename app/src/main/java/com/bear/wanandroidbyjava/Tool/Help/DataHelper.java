package com.bear.wanandroidbyjava.Tool.Help;

import com.bear.wanandroidbyjava.Data.Bean.Article;
import com.bear.wanandroidbyjava.Data.Bean.BannerSet;
import com.bear.wanandroidbyjava.Data.Bean.Tree;
import com.bear.wanandroidbyjava.Data.NetBean.ArticleBean;
import com.bear.wanandroidbyjava.Data.NetBean.BannerBean;
import com.bear.wanandroidbyjava.Data.NetBean.TreeBean;

import java.util.ArrayList;
import java.util.List;

public class DataHelper {
    public static List<Article> articleBeanToArticle(List<ArticleBean> articleBeanList, boolean top) {
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
        List<Tree> treeList = new ArrayList<>();
        for (TreeBean treeBean : treeBeanList) {
            treeList.add(treeBean.toTree());
        }
        return treeList;
    }
}
