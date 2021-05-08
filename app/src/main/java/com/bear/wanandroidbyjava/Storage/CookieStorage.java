package com.bear.wanandroidbyjava.Storage;

import androidx.annotation.NonNull;

import com.bear.libstorage.FileStorage;
import com.bear.wanandroidbyjava.WanApp;
import com.example.libbase.Executor.BgThreadExecutor;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;

public class CookieStorage {
    private static final String COOKIE_STORAGE_DIR =
            WanApp.getAppContext().getCacheDir() + File.separator + "cookie";
    private static final String COOKIE_STORAGE_PATH = COOKIE_STORAGE_DIR + File.separator + "cookie.txt";

    private static boolean hasLoadFromStorage = false;
    private static Map<String, List<Cookie>> cookieListMap = new HashMap<>();

    private CookieStorage() {
        throw new IllegalStateException("Utility class");
    }

    public static void init() {
        BgThreadExecutor.execute(new InitCookieRunnable());
    }

    public static void saveCookies(@NonNull String host, @NonNull List<Cookie> cookieList) {
        cookieListMap.put(host, cookieList);
        FileStorage.writeObjToJson(COOKIE_STORAGE_PATH, cookieListMap);
    }

    public static List<Cookie> getCookies(@NonNull String host) {
        if (!hasLoadFromStorage) {
            new InitCookieRunnable().run();
        }
        List<Cookie> cookieList = cookieListMap.get(host);
        cookieList = cookieList != null ? cookieList : new ArrayList<Cookie>();
        return cookieList;
    }

    public static void clearCookies() {
        cookieListMap.clear();
        FileStorage.writeObjToJson(COOKIE_STORAGE_PATH, cookieListMap);
    }

    private static class InitCookieRunnable implements Runnable {
        @Override
        public void run() {
            if (!hasLoadFromStorage) {
                synchronized (CookieStorage.class) {
                    if (!hasLoadFromStorage) {
                        cookieListMap = FileStorage.readObjFromJson(COOKIE_STORAGE_PATH,
                                new TypeToken<Map<String, List<Cookie>>>() {});
                        cookieListMap = cookieListMap != null ? cookieListMap : new HashMap<String, List<Cookie>>();
                        hasLoadFromStorage = true;
                    }
                }
            }
        }
    }
}
