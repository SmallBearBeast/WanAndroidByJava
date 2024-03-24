package com.bear.wanandroidbyjava.Net.Api;

import androidx.annotation.NonNull;

import com.bear.wanandroidbyjava.Data.NetBean.Dto.UserInfoDTO;
import com.bear.wanandroidbyjava.Net.Api.Callback.ApiLoginCallback;
import com.bear.wanandroidbyjava.Net.Api.Callback.ApiLogoutCallback;
import com.bear.wanandroidbyjava.Net.Api.Callback.ApiRegisterCallback;
import com.bear.wanandroidbyjava.Net.WanOkCallback;
import com.bear.wanandroidbyjava.Net.WanResponce;
import com.bear.wanandroidbyjava.Net.WanTypeToken;
import com.bear.wanandroidbyjava.Net.WanUrl;
import com.example.libbase.Executor.MainThreadExecutor;
import com.example.libbase.Util.NetWorkUtil;
import com.example.liblog.SLog;
import com.example.libokhttp.OkHelper;

import java.util.HashMap;
import java.util.Map;

public class LoginRegisterApi {

    private static final String TAG = "LoginRegisterApi";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String REPASSWORD = "repassword";

    public void login(@NonNull String userName, @NonNull String password, @NonNull final ApiLoginCallback callback) {
        SLog.d(TAG, "login: userName = " + userName);
        if (!NetWorkUtil.isConnected()) {
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put(USERNAME, userName);
        map.put(PASSWORD, password);
        OkHelper.getInstance().postMethod(WanUrl.LOGIN_URL, map, new WanOkCallback<UserInfoDTO>(WanTypeToken.USER_INFO_TOKEN) {
            @Override
            protected void onSuccess(WanResponce<UserInfoDTO> data) {
                if (data != null) {
                    if (data.errorCode == WanUrl.SUCCESS_ERROR_CODE) {
                        MainThreadExecutor.post(() -> callback.onSuccess(data.data));
                    } else {
                        MainThreadExecutor.post(() -> callback.onFail(data.errorCode));
                    }
                }
            }

            @Override
            protected void onFail() {
                MainThreadExecutor.post(() -> callback.onFail(WanUrl.FAIL_ERROR_CODE));
            }
        });
    }

    public void register(@NonNull String userName, @NonNull String password, @NonNull String rePassword, @NonNull final ApiRegisterCallback callback) {
        SLog.d(TAG, "register: userName = " + userName);
        if (!NetWorkUtil.isConnected()) {
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put(USERNAME, userName);
        map.put(PASSWORD, password);
        map.put(REPASSWORD, rePassword);
        OkHelper.getInstance().postMethod(WanUrl.REGISTER_URL, map, new WanOkCallback<UserInfoDTO>(WanTypeToken.USER_INFO_TOKEN) {
            @Override
            protected void onSuccess(WanResponce<UserInfoDTO> data) {
                if (data != null) {
                    if (data.errorCode == WanUrl.SUCCESS_ERROR_CODE) {
                        MainThreadExecutor.post(() -> callback.onSuccess(data.data));
                    } else {
                        MainThreadExecutor.post(() -> callback.onFail(data.errorCode));
                    }
                }
            }

            @Override
            protected void onFail() {
                MainThreadExecutor.post(() -> callback.onFail(WanUrl.FAIL_ERROR_CODE));
            }
        });
    }

    public void logout(@NonNull final ApiLogoutCallback callback) {
        SLog.d(TAG, "logout: start");
        if (!NetWorkUtil.isConnected()) {
            return;
        }
        OkHelper.getInstance().getMethod(WanUrl.LOGOUT_URL, new WanOkCallback<String>(WanTypeToken.LOGOUT_TOKEN) {
            @Override
            protected void onSuccess(WanResponce<String> data) {
                if (data != null) {
                    if (data.errorCode == WanUrl.SUCCESS_ERROR_CODE) {
                        MainThreadExecutor.post(callback::onSuccess);
                    } else {
                        MainThreadExecutor.post(() -> callback.onFail(data.errorCode));
                    }
                }
            }

            @Override
            protected void onFail() {
                MainThreadExecutor.post(() -> callback.onFail(WanUrl.FAIL_ERROR_CODE));
            }
        });
    }
}
