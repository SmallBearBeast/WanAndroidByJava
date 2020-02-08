package com.bear.wanandroidbyjava.Module.System.Nav;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bear.wanandroidbyjava.Bean.Nav;
import com.bear.wanandroidbyjava.NetBean.NavBean;
import com.bear.wanandroidbyjava.NetBean.WanResponce;
import com.bear.wanandroidbyjava.NetUrl;
import com.example.libbase.Util.CollectionUtil;
import com.example.libbase.Util.StringUtil;
import com.example.liblog.SLog;
import com.example.libokhttp.OkCallback;
import com.example.libokhttp.OkHelper;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class NavVM extends ViewModel {
    private static final String TAG = "NavVM";

    public MutableLiveData<List<Nav>> mNavLD = new MutableLiveData<>();

    public void fetchNav() {
        OkHelper.getInstance().getMethod(NetUrl.NAV, new OkCallback<WanResponce<List<NavBean>>>(new TypeToken<WanResponce<List<NavBean>>>(){}) {
            @Override
            protected void onSuccess(WanResponce<List<NavBean>> data) {
                if (data != null) {
                    SLog.d(TAG, "fetchNav: data.errorCode = " + data.errorCode + (StringUtil.isEmpty(data.errorMsg) ? "" : ", data.errorMsg = " + data.errorMsg));
                    if (CollectionUtil.isEmpty(data.data)) {
                        SLog.d(TAG, "fetchNav: navBeanList is empty");
                    } else {
                        List<NavBean> navBeanList = data.data;
                        List<Nav> navList = new ArrayList<>();
                        for (NavBean navBean : navBeanList) {
                            navList.add(navBean.toNav());
                        }
                        mNavLD.postValue(navList);
                        SLog.d(TAG, "fetchNav: navBeanList.size = " + navBeanList.size());
                        SLog.d(TAG, "fetchNav: navBeanList = " + navBeanList);
                    }
                }
            }

            @Override
            protected void onFail() {
                SLog.d(TAG, "fetchNav: onFail");
            }
        });
    }
}
