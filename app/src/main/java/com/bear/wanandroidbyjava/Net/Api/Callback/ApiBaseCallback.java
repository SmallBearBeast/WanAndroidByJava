package com.bear.wanandroidbyjava.Net.Api.Callback;

public interface ApiBaseCallback {
    void onSuccess();

    void onFail(int errorCode);
}
