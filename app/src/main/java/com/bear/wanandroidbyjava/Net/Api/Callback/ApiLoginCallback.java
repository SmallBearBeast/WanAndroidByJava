package com.bear.wanandroidbyjava.Net.Api.Callback;

import com.bear.wanandroidbyjava.Data.NetBean.Dto.UserInfoDTO;

public interface ApiLoginCallback {
    void onSuccess(UserInfoDTO userInfoDTO);

    void onFail(int errorCode);
}
