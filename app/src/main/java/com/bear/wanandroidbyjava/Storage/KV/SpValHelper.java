package com.bear.wanandroidbyjava.Storage.KV;

import com.bear.libkv.KV;
import com.bear.libkv.SpVal.SpHelper;


public class SpValHelper {
    public static final String SP_GLOBAL_CONFIG = "sp_global_config_";
    private static final String SP_USER_CONFIG = "sp_user_config_";
    private static long UID;

    // sp_global_config
    public static final KV<Boolean> hasHomeStorageData = SpHelper.create("hasHomeStorageData", false);
    public static final KV<Boolean> hasTreeStorageData = SpHelper.create("hasTreeStorageData", false);
    public static final KV<Boolean> hasNavStorageData = SpHelper.create("hasNavStorageData", false);

    // sp_user_config

    private static String getUserConfigKey() {
        return SP_USER_CONFIG + UID;
    }

    public static void changeUserId(long uid) {
        if (UID != uid) {
            UID = uid;
        }
    }
}
