package com.bear.wanandroidbyjava.Net.Api;

import com.bear.wanandroidbyjava.Data.Bean.CoinInfoBean;
import com.bear.wanandroidbyjava.Data.Bean.CollectArticleInfoBean;
import com.bear.wanandroidbyjava.Data.Bean.UserDataBean;
import com.bear.wanandroidbyjava.Data.Bean.UserInfoBean;
import com.bear.wanandroidbyjava.Data.NetBean.UserDataNetBean;
import com.bear.wanandroidbyjava.Net.Api.Callback.ApiUserDataCallback;
import com.bear.wanandroidbyjava.Net.WanOkCallback;
import com.bear.wanandroidbyjava.Net.WanResponce;
import com.bear.wanandroidbyjava.Net.WanTypeToken;
import com.bear.wanandroidbyjava.Net.WanUrl;
import com.example.libbase.Util.NetWorkUtil;
import com.example.liblog.SLog;
import com.example.libokhttp.OkHelper;

public class UserDataApi {

    private static final String TAG = "UserDataApi";

    public void fetchUserData(final ApiUserDataCallback callback) {
        SLog.d(TAG, "fetchUserData: start");
        if (!NetWorkUtil.isConnected()) {
            return;
        }
        OkHelper.getInstance().getMethod(WanUrl.GET_USERINFO_URL, new WanOkCallback<UserDataNetBean>(WanTypeToken.USER_DATA_TOKEN) {
            @Override
            protected void onSuccess(WanResponce<UserDataNetBean> data) {
                SLog.d(TAG, "onSuccess: data = " + data);
                if (data != null) {
                    if (data.errorCode == WanUrl.SUCCESS_ERROR_CODE) {
                        UserDataNetBean userDataNetBean = data.data;
                        if (userDataNetBean != null) {
                            CoinInfoBean coinInfo = userDataNetBean.getCoinInfo() != null ? userDataNetBean.getCoinInfo().toCoinInfo() : null;
                            CollectArticleInfoBean collectArticleInfo = userDataNetBean.getCollectArticleInfo() != null ? userDataNetBean.getCollectArticleInfo().toCollectArticleInfo() : null;
                            UserInfoBean userInfo = userDataNetBean.getUserInfo() != null ? userDataNetBean.getUserInfo().toUserInfo() : null;
                            UserDataBean userData = new UserDataBean(coinInfo, collectArticleInfo, userInfo);
                            callback.onSuccess(userData);
                        }
                    } else {
                        callback.onFail(data.errorCode);
                    }
                }
            }

            @Override
            protected void onFail() {
                SLog.d(TAG, "onFail");
                callback.onFail(WanUrl.FAIL_ERROR_CODE);
            }
        });
    }
}
