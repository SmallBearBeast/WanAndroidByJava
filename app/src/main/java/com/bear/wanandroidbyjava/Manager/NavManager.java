package com.bear.wanandroidbyjava.Manager;

import com.bear.wanandroidbyjava.Data.Bean.Nav;
import com.bear.wanandroidbyjava.Data.NetBean.NavBean;
import com.bear.wanandroidbyjava.Net.WanUrl;
import com.bear.wanandroidbyjava.Net.WanOkCallback;
import com.bear.wanandroidbyjava.Net.WanResponce;
import com.bear.wanandroidbyjava.Net.WanTypeToken;
import com.bear.wanandroidbyjava.Storage.SysStorage;
import com.bear.wanandroidbyjava.Tool.Helper.DataHelper;
import com.example.libbase.Executor.BgThreadExecutor;
import com.example.libbase.Executor.MainThreadExecutor;
import com.example.libbase.Util.CollectionUtil;
import com.example.libbase.Util.StringUtil;
import com.example.liblog.SLog;
import com.example.libokhttp.OkHelper;

import java.util.ArrayList;
import java.util.List;

public class NavManager {
    private static final String TAG = "NavManager";
    public void loadDataFromStorage(final NavDataListener listener) {
        BgThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                List<Nav> navList = SysStorage.getNavList();
                SLog.d(TAG, "loadDataFromStorage: navList = " + navList);
                callNavDataListener(listener, navList, false);
            }
        });
    }

    public void loadDataFromNet(final NavDataListener listener) {
        OkHelper.getInstance().getMethod(WanUrl.NAV_URL, new WanOkCallback<List<NavBean>>(WanTypeToken.LIST_NAV_TOKEN) {
            @Override
            protected void onSuccess(WanResponce<List<NavBean>> data) {
                if (data != null) {
                    SLog.d(TAG, "loadDataFromNet: data.errorCode = " + data.errorCode + (StringUtil.isEmpty(data.errorMsg) ? "" : ", data.errorMsg = " + data.errorMsg));
                    List<Nav> navList = DataHelper.navBeanToNav(data.data);
                    SLog.d(TAG, "loadDataFromNet: navList.size = " + navList.size() + ", navList = " + navList);
                    if (!CollectionUtil.isEmpty(navList)) {
                        SysStorage.saveNavList(navList);
                    }
                    callNavDataListener(listener, navList, true);
                }
            }

            @Override
            protected void onFail() {
                SLog.d(TAG, "loadNavData: onFail");
                callNavDataListener(listener, new ArrayList<Nav>(), true);
            }
        });
    }

    private void callNavDataListener(final NavDataListener listener, final List<Nav> navList, final boolean fromNet) {
        if (listener == null) {
            return;
        }
        MainThreadExecutor.post(new Runnable() {
            @Override
            public void run() {
                listener.onLoad(navList, fromNet);
            }
        });
    }

    public interface NavDataListener {
        void onLoad(List<Nav> navList, boolean fromNet);
    }
}
