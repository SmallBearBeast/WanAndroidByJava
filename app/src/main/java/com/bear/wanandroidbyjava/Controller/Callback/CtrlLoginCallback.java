package com.bear.wanandroidbyjava.Controller.Callback;

import com.bear.wanandroidbyjava.Data.Bean.UserInfoBean;

public interface CtrlLoginCallback {
    void onSuccess(UserInfoBean userInfoBean);

    void onFail(int errorCode);
}
