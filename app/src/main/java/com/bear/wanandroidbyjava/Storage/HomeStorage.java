package com.bear.wanandroidbyjava.Storage;

import androidx.annotation.NonNull;

import com.bear.libstorage.FileStorage;
import com.bear.wanandroidbyjava.Data.Bean.Article;
import com.bear.wanandroidbyjava.Data.Bean.BannerSet;
import com.bear.wanandroidbyjava.WanApp;
import com.example.libbase.Util.CollectionUtil;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.List;

public class HomeStorage {
    private static final String HOME_STORAGE_DIR =
            WanApp.getAppContext().getCacheDir() + File.separator + "home";
    private static final String HOME_BANNER_STORAGE_PATH = HOME_STORAGE_DIR + File.separator + "banner.txt";
    private static final String HOME_TOP_ARTICLE_STORAGE_PATH = HOME_STORAGE_DIR + File.separator + "top_article.txt";
    private static final String HOME_NORMAL_ARTICLE_STORAGE_PATH = HOME_STORAGE_DIR + File.separator + "normal_article.txt";

    private HomeStorage() {
        throw new IllegalStateException("Utility class");
    }

    public static void saveBannerSet(@NonNull BannerSet bannerSet) {
        FileStorage.writeObjToJson(HOME_BANNER_STORAGE_PATH, bannerSet);
    }

    public static BannerSet getBannerSet() {
        return FileStorage.readObjFromJson(HOME_BANNER_STORAGE_PATH, new TypeToken<BannerSet>(){});
    }

    public static void saveTopArticleList(@NonNull List<Article> articleList) {
        if (!CollectionUtil.isEmpty(articleList)) {
            FileStorage.writeObjToJson(HOME_TOP_ARTICLE_STORAGE_PATH, articleList);
        }
    }

    public static List<Article> getTopArticleList() {
        return FileStorage.readObjFromJson(HOME_TOP_ARTICLE_STORAGE_PATH, new TypeToken<List<Article>>(){});
    }

    public static void saveNormalArticleList(@NonNull List<Article> articleList) {
        if (!CollectionUtil.isEmpty(articleList)) {
            FileStorage.writeObjToJson(HOME_NORMAL_ARTICLE_STORAGE_PATH, articleList);
        }
    }

    public static List<Article> getNormalArticleList() {
        return FileStorage.readObjFromJson(HOME_NORMAL_ARTICLE_STORAGE_PATH, new TypeToken<List<Article>>(){});
    }
}
