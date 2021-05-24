package com.bear.wanandroidbyjava.Data.Bean;

import com.example.libbase.Util.CollectionUtil;

import java.util.ArrayList;
import java.util.List;

public class BannerSet {
    public List<Banner> bannerList = new ArrayList<>();

    public boolean isEmpty() {
        return CollectionUtil.isEmpty(bannerList);
    }

    public void clear() {
        bannerList.clear();
    }

    public void add(BannerSet bannerSet) {
        bannerList.addAll(bannerSet.bannerList);
    }

    @Override
    public String toString() {
        return "BannerSet{" +
                "bannerList=" + bannerList +
                '}';
    }
}
