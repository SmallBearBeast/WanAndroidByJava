package com.bear.wanandroidbyjava.NetBean;

import com.bear.wanandroidbyjava.Bean.Nav;

import java.util.ArrayList;
import java.util.List;

public class NavBean {
    public int cid;
    public String name;
    public List<ArticleBean> articles;

    public Nav toNav() {
        Nav nav = new Nav();
        nav.name = name;
        nav.articleList = new ArrayList<>();
        for (ArticleBean article : articles) {
            nav.articleList.add(article.toArticle());
        }
        return nav;
    }
}
