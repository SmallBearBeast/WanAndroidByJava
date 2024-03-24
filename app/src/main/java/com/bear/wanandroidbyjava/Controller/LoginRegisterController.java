package com.bear.wanandroidbyjava.Controller;

import androidx.annotation.NonNull;

import com.bear.wanandroidbyjava.Controller.Callback.CtrlLoginCallback;
import com.bear.wanandroidbyjava.Controller.Callback.CtrlLogoutCallback;
import com.bear.wanandroidbyjava.Controller.Callback.CtrlRegisterCallback;
import com.bear.wanandroidbyjava.Data.Bean.UserInfoBean;
import com.bear.wanandroidbyjava.Data.NetBean.Dto.UserInfoDTO;
import com.bear.wanandroidbyjava.Net.Api.Callback.ApiLoginCallback;
import com.bear.wanandroidbyjava.Net.Api.Callback.ApiLogoutCallback;
import com.bear.wanandroidbyjava.Net.Api.Callback.ApiRegisterCallback;
import com.bear.wanandroidbyjava.Net.Api.LoginRegisterApi;
import com.bear.wanandroidbyjava.Storage.UserRepo;

public class LoginRegisterController {

    private final LoginRegisterApi loginRegisterApi = new LoginRegisterApi();

    public void login(@NonNull String userName, @NonNull String password, @NonNull final CtrlLoginCallback callback) {
        loginRegisterApi.login(userName, password, new ApiLoginCallback() {
            @Override
            public void onSuccess(UserInfoDTO userInfoDTO) {
                UserInfoBean userInfoBean = userInfoDTO.toUserInfo();
                callback.onSuccess(userInfoBean);
            }

            @Override
            public void onFail(int errorCode) {
                callback.onFail(errorCode);
            }
        });
    }

    public void register(@NonNull String userName, @NonNull String password, @NonNull String rePassword, @NonNull final CtrlRegisterCallback callback) {
        loginRegisterApi.register(userName, password, rePassword, new ApiRegisterCallback() {
            @Override
            public void onSuccess(UserInfoDTO userInfoDTO) {
                UserInfoBean userInfoBean = userInfoDTO.toUserInfo();
                callback.onSuccess(userInfoBean);
            }

            @Override
            public void onFail(int errorCode) {
                callback.onFail(errorCode);
            }
        });
    }

    public void logout(@NonNull final CtrlLogoutCallback callback) {
        loginRegisterApi.logout(new ApiLogoutCallback() {
            @Override
            public void onSuccess() {
                UserRepo.clearUserData();
                callback.onSuccess();
            }

            @Override
            public void onFail(int errorCode) {
                callback.onFail(errorCode);
            }
        });
    }
}
