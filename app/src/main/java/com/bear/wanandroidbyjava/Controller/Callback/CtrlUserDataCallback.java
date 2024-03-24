package com.bear.wanandroidbyjava.Controller.Callback;

import com.bear.wanandroidbyjava.Data.Bean.UserDataBean;

public interface CtrlUserDataCallback {
    void onSuccess(UserDataBean userDataBean);

    void onFail(int errorCode);
}
