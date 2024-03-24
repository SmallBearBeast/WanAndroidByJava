package com.bear.wanandroidbyjava.Storage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bear.libstorage.FileStorage;
import com.bear.wanandroidbyjava.Module.Collect.CollectInfo;
import com.bear.wanandroidbyjava.WanApp;
import com.example.libbase.Executor.BgThreadExecutor;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


public class CollectInfoStorage extends FileStorage {
    private static final String COLLECT_STORAGE_DIR =
            WanApp.getAppContext().getCacheDir() + File.separator + "collect";
    private static final String COLLECT_STORAGE_PATH = COLLECT_STORAGE_DIR + File.separator + "collect.txt";
    private static volatile boolean hasLoadFromStorage = false;
    private static Map<Integer, CollectInfo> collectInfoMap = new HashMap<>();

    private CollectInfoStorage() {
        throw new IllegalStateException("Utility class");
    }

    public static void init() {
        if (!hasLoadFromStorage) {
            BgThreadExecutor.execute(new InitCollectInfoRunnable());
        }
    }

    public static synchronized @Nullable CollectInfo getCollectInfo(int collectId) {
        if (!hasLoadFromStorage) {
            throw new InitException();
        }
        return collectInfoMap.get(collectId);
    }

    public static synchronized void saveCollectInfo(@NonNull CollectInfo collectInfo) {
        int collectId = collectInfo.getCollectId();
        if (collectId != CollectInfo.INVALID_ID) {
            collectInfoMap.put(collectId, collectInfo);
            FileStorage.writeObjToJson(COLLECT_STORAGE_PATH, collectInfoMap);
        }
    }

    public static synchronized void clearCollectInfo() {
        collectInfoMap.clear();
        FileStorage.writeObjToJson(COLLECT_STORAGE_PATH, collectInfoMap);
    }


    private static class InitCollectInfoRunnable implements Runnable {
        @Override
        public void run() {
            if (!hasLoadFromStorage) {
                synchronized (CollectInfoStorage.class) {
                    if (!hasLoadFromStorage) {
                        collectInfoMap = FileStorage.readObjFromJson(COLLECT_STORAGE_PATH,
                                new TypeToken<Map<Integer, CollectInfo>>() {
                                });
                        collectInfoMap = collectInfoMap != null ? collectInfoMap : new HashMap<>();
                        hasLoadFromStorage = true;
                    }
                }
            }
        }
    }

    private static class InitException extends RuntimeException {
        public InitException() {
            super("Forget to init CookieStorage first");
        }
    }
}
