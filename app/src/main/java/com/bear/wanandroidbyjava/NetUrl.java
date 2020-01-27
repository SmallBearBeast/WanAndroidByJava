package com.bear.wanandroidbyjava;

public class NetUrl {

    private static final String HTTPS = "https://";
    private static final String HOST = "www.wanandroid.com";

    // 首页banner
    // https://www.wanandroid.com/banner/json
    public static final String BANNER = HTTPS + HOST + "/banner/json";

    // 置顶文章
    public static final String TOP_ARTICLE = HTTPS + HOST + "/article/top/json";

    // 首页文章列表
    public static String getHomeArticleList(int pageIndex) {
        return HTTPS + HOST + "/article/list/" + pageIndex + "/json";
    }

    // 搜索热词
    public static final String SEARCH_HOT_KEY = HTTPS + HOST + "/hotkey/json";

    // 体系数据
    public static final String SYSTEM = HTTPS + HOST + "/tree/json";

}
