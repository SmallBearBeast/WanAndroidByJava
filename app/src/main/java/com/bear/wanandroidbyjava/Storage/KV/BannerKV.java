package com.bear.wanandroidbyjava.Storage.KV;

import com.bear.wanandroidbyjava.Data.Bean.BannerSet;
import com.example.libbase.Util.CollectionUtil;
import com.example.libbase.Util.StringUtil;

import java.util.List;

public class BannerKV {
    public static BannerSet getBannerSet() {
        String kvBannerImageUrlListStr = SpValHelper.kvBannerImageUrlList.get();
        String kvBannerClickUrlListStr = SpValHelper.kvBannerClickUrlList.get();
        if (StringUtil.isEmpty(kvBannerImageUrlListStr) && StringUtil.isEmpty(kvBannerClickUrlListStr)) {
            return null;
        }
        BannerSet bannerSet = new BannerSet();
        bannerSet.imageUrlList = StringUtil.splitToList(kvBannerImageUrlListStr, ",", String.class);
        bannerSet.clickUrlList = StringUtil.splitToList(kvBannerClickUrlListStr, ",", String.class);
        return bannerSet;
    }

    public static void saveBannerSet(BannerSet bannerSet) {
        List<String> imageUrlList = bannerSet.imageUrlList;
        List<String> clickUrlList = bannerSet.clickUrlList;
        if (CollectionUtil.isSameLength(imageUrlList, clickUrlList)) {
            String imageUrlStr = StringUtil.joinToString(imageUrlList, ",");
            String clickUrlStr = StringUtil.joinToString(clickUrlList, ",");
            SpValHelper.kvBannerImageUrlList.set(imageUrlStr);
            SpValHelper.kvBannerClickUrlList.set(clickUrlStr);
        }
    }
}
