package com.bear.wanandroidbyjava.Storage;

import androidx.annotation.NonNull;

import com.bear.libstorage.FileStorage;
import com.bear.wanandroidbyjava.Data.Bean.UserDataBean;
import com.bear.wanandroidbyjava.WanApp;
import com.example.libbase.Executor.BgThreadExecutor;
import com.google.gson.reflect.TypeToken;

import java.io.File;

public class UserRepo {
    private static final String USER_STORAGE_DIR =
            WanApp.getAppContext().getCacheDir() + File.separator + "user";
    private static final String USER_DATA_STORAGE_PATH = USER_STORAGE_DIR + File.separator + "user_data.txt";

    private static UserDataBean userDataCache;

    private UserRepo() {
        throw new IllegalStateException("Utility class");
    }

    public static void saveUserData(UserDataBean userData) {
        userDataCache = userData;
        BgThreadExecutor.execute(() -> FileStorage.writeObjToJson(USER_DATA_STORAGE_PATH, userData));
    }

    public static void loadUserData(@NonNull UserDataCallback callback) {
        if (userDataCache != null) {
            callback.onUserDataLoaded(userDataCache);
            return;
        }
        BgThreadExecutor.execute(() -> {
            UserDataBean userData = FileStorage.readObjFromJson(USER_DATA_STORAGE_PATH, new TypeToken<UserDataBean>() {});
            userDataCache = userData;
            callback.onUserDataLoaded(userData);
        });
    }

    public static void clearUserData() {
        userDataCache = null;
        BgThreadExecutor.execute(() -> FileStorage.writeObjToJson(USER_DATA_STORAGE_PATH, null));
    }

    public static UserDataBean getUserData() {
        return userDataCache;
    }

    private void callOnUserDataLoaded() {

    }

    public interface UserDataCallback {
        void onUserDataLoaded(UserDataBean userData);
    }
}
