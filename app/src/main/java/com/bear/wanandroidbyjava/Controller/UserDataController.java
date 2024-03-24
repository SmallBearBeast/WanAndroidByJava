package com.bear.wanandroidbyjava.Controller;

import androidx.annotation.NonNull;

import com.bear.wanandroidbyjava.Controller.Callback.CtrlUserDataCallback;
import com.bear.wanandroidbyjava.Data.Bean.UserDataBean;
import com.bear.wanandroidbyjava.Net.Api.Callback.ApiUserDataCallback;
import com.bear.wanandroidbyjava.Net.Api.UserDataApi;
import com.bear.wanandroidbyjava.Storage.UserRepo;
import com.example.libbase.Util.MainHandlerUtil;
import com.example.liblog.SLog;

public class UserDataController {

    private static final String TAG = "UserInfoController";

    private volatile boolean loadFromApiReady = false;
    private final UserDataApi userDataApi = new UserDataApi();

    public void fetchUserData(@NonNull final CtrlUserDataCallback listener) {
        SLog.d(TAG, "fetchUserData: start");
        loadFromApiReady = false;
        UserRepo.loadUserData(userData -> {
            if (!loadFromApiReady) {
                MainHandlerUtil.post(() -> listener.onSuccess(userData));
            }
        });
        userDataApi.fetchUserData(new ApiUserDataCallback() {
            @Override
            public void onSuccess(UserDataBean userData) {
                loadFromApiReady = true;
                UserRepo.saveUserData(userData);
                MainHandlerUtil.post(() -> listener.onSuccess(userData));
            }

            @Override
            public void onFail(int errorCode) {
                MainHandlerUtil.post(() -> listener.onFail(errorCode));
            }
        });
    }
}
