package com.bear.wanandroidbyjava.Module.System.Nav;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bear.wanandroidbyjava.Data.Bean.Nav;
import com.bear.wanandroidbyjava.Data.NetBean.NavBean;
import com.bear.wanandroidbyjava.Net.WanOkCallback;
import com.bear.wanandroidbyjava.Net.WanResponce;
import com.bear.wanandroidbyjava.Net.WanTypeToken;
import com.bear.wanandroidbyjava.Net.NetUrl;
import com.example.libbase.Util.CollectionUtil;
import com.example.libbase.Util.StringUtil;
import com.example.liblog.SLog;
import com.example.libokhttp.OkHelper;

import java.util.ArrayList;
import java.util.List;

public class NavVM extends ViewModel {
    private static final String TAG = "NavVM";
    private boolean mIsFirstLoad = true;
    private MutableLiveData<Boolean> mShowProgressLD = new MutableLiveData<>();
    private MutableLiveData<List<Nav>> mNavLD = new MutableLiveData<>();

    public void fetchNav() {
        SLog.d(TAG, "fetchNav: start");
        mShowProgressLD.postValue(true);
        OkHelper.getInstance().getMethod(NetUrl.NAV, new WanOkCallback<List<NavBean>>(WanTypeToken.NAV_TOKEN) {
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
                        SLog.d(TAG, "fetchNav: navBeanList.size = " + navBeanList.size() + ", navBeanList = " + navBeanList);
                    }
                }
                mShowProgressLD.postValue(false);
            }

            @Override
            protected void onFail() {
                SLog.d(TAG, "fetchNav: onFail");
                mShowProgressLD.postValue(false);
            }
        });
    }

    public boolean isFirstLoad() {
        return mIsFirstLoad;
    }

    public MutableLiveData<Boolean> getShowProgressLD() {
        return mShowProgressLD;
    }

    public MutableLiveData<List<Nav>> getNavLD() {
        return mNavLD;
    }
}
