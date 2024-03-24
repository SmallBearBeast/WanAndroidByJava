package com.bear.wanandroidbyjava.Net.Api.Callback;

import com.bear.wanandroidbyjava.Data.Bean.UserDataBean;

public interface ApiUserDataCallback {
    void onSuccess(UserDataBean userDataBean);

    void onFail(int errorCode);
}
