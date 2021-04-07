package com.bear.wanandroidbyjava.Storage.KV;

import com.bear.wanandroidbyjava.Data.Bean.Banner;
import com.example.libbase.Util.CollectionUtil;
import com.example.libbase.Util.StringUtil;

public class BannerKV {
    public static Banner getBanner() {
        String kvBannerImageUrlList = SpValHelper.kvBannerImageUrlList.get();
        String kvBannerClickUrlList = SpValHelper.kvBannerClickUrlList.get();
        if (StringUtil.isEmpty(kvBannerImageUrlList) && StringUtil.isEmpty(kvBannerClickUrlList)) {
            return null;
        }
        String[] imageUrls = kvBannerImageUrlList.split(",");
        String[] clickUrls = kvBannerClickUrlList.split(",");
        if (CollectionUtil.isSameLength(imageUrls, clickUrls)) {
            Banner banner = new Banner();
            banner.imageUrlList = CollectionUtil.asListNull(imageUrls);
            banner.clickUrlList = CollectionUtil.asListNull(clickUrls);
            return banner;
        }
        return null;
    }

    public static void saveBanner(Banner banner) {
        // TODO: 2021/4/7
    }
}
