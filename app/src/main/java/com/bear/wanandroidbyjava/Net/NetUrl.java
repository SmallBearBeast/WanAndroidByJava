package com.bear.wanandroidbyjava.Net;

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

    // 知识体系数据
    public static final String TREE = HTTPS + HOST + "/tree/json";

    // 知识体系下的文章
    public static String getTreeArticleList(int pageIndex, int cid) {
        return HTTPS + HOST + "/article/list/" + pageIndex + "/json?cid=" + cid;
    }

    // 导航
    // https://www.wanandroid.com/navi/json
    public static final String NAV = HTTPS + HOST + "/navi/json";

    // 公众号Tab
    // https://wanandroid.com/wxarticle/chapters/json
    public static final String PUBLIC_TAB = HTTPS + HOST + "/wxarticle/chapters/json";

    // 某个公众号历史数据
    // https://wanandroid.com/wxarticle/list/408/1/json
    public static String getPublicArticleList(int id, int pageIndex) {
        return HTTPS + HOST + "/wxarticle/list/" + id + "/" + pageIndex + "/json";
    }

    // 项目
    // https://www.wanandroid.com/project/tree/json
    public static final String PROJECT_TAB = HTTPS + HOST + "/project/tree/json";

    // 某个项目历史数据
    // https://www.wanandroid.com/project/list/1/json?cid=294
    public static String getProjectArticleList(int cid, int pageIndex) {
        return HTTPS + HOST + "/project/list/" + pageIndex + "/json?cid=" + cid;
    }

}
