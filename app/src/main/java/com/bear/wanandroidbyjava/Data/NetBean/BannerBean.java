package com.bear.wanandroidbyjava.Data.NetBean;

import com.bear.wanandroidbyjava.Data.Bean.Banner;

public class BannerBean {
    public String desc;
    public int id;
    public String imagePath;
    public int isVisible;
    public int order;
    public String title;
    public int type;
    public String url;

    public Banner toBanner() {
        Banner banner = new Banner();
        banner.desc = desc;
        banner.id = id;
        banner.imagePath = imagePath;
        banner.isVisible = isVisible;
        banner.order = order;
        banner.title = title;
        banner.type = type;
        banner.url = url;
        return banner;
    }
}
