package com.bear.wanandroidbyjava.Storage;

import androidx.annotation.NonNull;

import com.bear.libstorage.FileStorage;
import com.bear.wanandroidbyjava.Data.Bean.Nav;
import com.bear.wanandroidbyjava.Data.Bean.Tree;
import com.bear.wanandroidbyjava.WanApp;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.List;

public class SysStorage {
    private static final String SYS_STORAGE_DIR =
            WanApp.getAppContext().getCacheDir() + File.separator + "sys";
    private static final String SYS_TREE_STORAGE_PATH = SYS_STORAGE_DIR + File.separator + "tree.txt";
    private static final String SYS_NAV_STORAGE_PATH = SYS_STORAGE_DIR + File.separator + "nav.txt";

    private SysStorage() {
        throw new IllegalStateException("Utility class");
    }

    public static void saveTreeList(@NonNull List<Tree> treeList) {
        FileStorage.writeObjToJson(SYS_TREE_STORAGE_PATH, treeList);
    }

    public static List<Tree> getTreeList() {
        return FileStorage.readObjFromJson(SYS_TREE_STORAGE_PATH, new TypeToken<List<Tree>>(){});
    }

    public static void saveNavList(@NonNull List<Nav> navList) {
        FileStorage.writeObjToJson(SYS_NAV_STORAGE_PATH, navList);
    }

    public static List<Nav> getNavList() {
        return FileStorage.readObjFromJson(SYS_NAV_STORAGE_PATH, new TypeToken<List<Nav>>(){});
    }
}
