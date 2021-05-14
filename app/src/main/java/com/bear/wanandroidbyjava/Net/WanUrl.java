package com.bear.wanandroidbyjava.Net;

public class WanUrl {

    private WanUrl() {
        throw new IllegalStateException("Utility class");
    }

    public static final int SUCCESS_ERROR_CODE = 0;
    public static final int FAIL_ERROR_CODE = -1;

    private static final String HTTPS = "https://";
    private static final String HOST = "www.wanandroid.com";

    // 首页banner
    // https://www.wanandroid.com/banner/json
    public static final String BANNER_URL = HTTPS + HOST + "/banner/json";

    // 置顶文章
    public static final String TOP_ARTICLE_URL = HTTPS + HOST + "/article/top/json";

    // 首页文章列表
    public static String getHomeArticleListUrl(int pageIndex) {
        return HTTPS + HOST + "/article/list/" + pageIndex + "/json";
    }

    // 搜索热词
    public static final String SEARCH_HOT_KEY_URL = HTTPS + HOST + "/hotkey/json";

    // 知识体系数据
    public static final String TREE_URL = HTTPS + HOST + "/tree/json";

    // 知识体系下的文章
    public static String getTreeArticleListUrl(int pageIndex, int cid) {
        return HTTPS + HOST + "/article/list/" + pageIndex + "/json?cid=" + cid;
    }

    // 导航
    // https://www.wanandroid.com/navi/json
    public static final String NAV_URL = HTTPS + HOST + "/navi/json";

    // 公众号Tab
    // https://wanandroid.com/wxarticle/chapters/json
    public static final String PUBLIC_TAB_URL = HTTPS + HOST + "/wxarticle/chapters/json";

    // 某个公众号历史数据
    // https://wanandroid.com/wxarticle/list/408/1/json
    public static String getPublicArticleListUrl(int id, int pageIndex) {
        return HTTPS + HOST + "/wxarticle/list/" + id + "/" + pageIndex + "/json";
    }

    // 项目
    // https://www.wanandroid.com/project/tree/json
    public static final String PROJECT_TAB_URL = HTTPS + HOST + "/project/tree/json";

    // 某个项目历史数据
    // https://www.wanandroid.com/project/list/1/json?cid=294
    public static String getProjectArticleListUrl(int cid, int pageIndex) {
        return HTTPS + HOST + "/project/list/" + pageIndex + "/json?cid=" + cid;
    }

    /**
     * 登录
     * https://www.wanandroid.com/user/login
     * 方法：POST
     * 参数：
     * username，password
     */
    public static final String LOGIN_URL = HTTPS + HOST + "/user/login";

    /**
     * 注册
     * https://www.wanandroid.com/user/register
     * 方法：POST
     * 参数
     * username, password, repassword
     */
    public static final String REGISTER_URL = HTTPS + HOST + "/user/register";

    /**
     * 退出
     * https://www.wanandroid.com/user/logout/json
     * 方法：GET
     */
    public static final String LOGOUT_URL = HTTPS + HOST + "/user/logout/json";

    /**
     * 收藏文章列表
     * https://www.wanandroid.com/lg/collect/list/0/json
     * 方法：GET
     * 参数： 页码：拼接在链接中，从0开始。
     */
    public static String getCollectArticleListUrl(int pageIndex) {
        return HTTPS + HOST + "/lg/collect/list/" + pageIndex + "/json";
    }

    /**
     * 收藏站内文章
     * https://www.wanandroid.com/lg/collect/1165/json
     * 方法：POST
     * 参数： 文章id，拼接在链接中。
     */
    public static String collectArticleUrl(int articleId) {
        return HTTPS + HOST + "/lg/collect/" + articleId + "/json";
    }

    /**
     * 收藏站外文章
     * https://www.wanandroid.com/lg/collect/add/json
     * 方法：POST
     * 参数：
     * title，author，link
     */
    public static final String COLLECT_OUT_ARTICLE_URL = HTTPS + HOST + "/lg/collect/add/json";

    /**
     * https://www.wanandroid.com/lg/uncollect_originId/2333/json
     * 取消收藏
     * 方法：POST
     * 参数：
     * id: 拼接在链接上
     * 还有另外一种
     */
    public static String unCollectArticleUrl(int articleId) {
        return HTTPS + HOST + "/lg/uncollect_originId/" + articleId + "/json";
    }

    /**
     * https://www.wanandroid.com/lg/collect/usertools/json
     * 收藏网站列表
     * 方法：GET
     * 参数：无
     */
    public static final String GET_COLLECT_LINK_URL = HTTPS + HOST + "/lg/collect/usertools/json";

    /**
     * https://www.wanandroid.com/lg/collect/addtool/json
     * 收藏网址
     * 方法：POST
     * 参数：
     * name, link
     */
    public static final String COLLECT_LINK_URL = HTTPS + HOST + "/lg/collect/addtool/json";

    /**
     * https://www.wanandroid.com/lg/collect/updatetool/json
     * 编辑收藏网站
     * 方法：POST
     * 参数：
     * id, name, link
     */
    public static final String EDIT_COLLECT_LINK_URL = HTTPS + HOST + "/lg/collect/updatetool/json";

    /**
     * https://www.wanandroid.com/lg/collect/deletetool/json
     * 删除收藏网站
     * 方法：POST
     * 参数：
     * id
     */
    public static final String DELETE_COLLECT_LINK_URL = HTTPS + HOST + "/lg/collect/deletetool/json";
}
