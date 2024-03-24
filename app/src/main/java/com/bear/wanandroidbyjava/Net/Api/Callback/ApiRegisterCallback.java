package com.bear.wanandroidbyjava.Net.Api.Callback;

import com.bear.wanandroidbyjava.Data.NetBean.Dto.UserInfoDTO;

public interface ApiRegisterCallback {
    void onSuccess(UserInfoDTO userInfoDTO);

    void onFail(int errorCode);
}
