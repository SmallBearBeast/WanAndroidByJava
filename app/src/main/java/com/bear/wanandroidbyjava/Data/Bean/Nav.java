package com.bear.wanandroidbyjava.Data.Bean;

import java.util.List;

public class Nav {
    public int cid;
    public String name;
    public List<Article> articleList;

    @Override
    public String toString() {
        return "Nav{" +
                "cid=" + cid +
                ", name='" + name + '\'' +
                ", articleList=" + articleList +
                '}';
    }
}
