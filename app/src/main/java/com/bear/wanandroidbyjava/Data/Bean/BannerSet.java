package com.bear.wanandroidbyjava.Data.Bean;

import java.util.List;

public class BannerSet {
    public List<String> imageUrlList;
    public List<String> clickUrlList;

    @Override
    public String toString() {
        return "BannerSet{" +
                "imageUrlList=" + imageUrlList +
                ", clickUrlList=" + clickUrlList +
                '}';
    }
}
