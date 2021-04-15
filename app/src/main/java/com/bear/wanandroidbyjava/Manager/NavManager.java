package com.bear.wanandroidbyjava.Manager;

import com.bear.wanandroidbyjava.Data.Bean.Nav;
import com.bear.wanandroidbyjava.Data.NetBean.NavBean;
import com.bear.wanandroidbyjava.Net.NetUrl;
import com.bear.wanandroidbyjava.Net.WanOkCallback;
import com.bear.wanandroidbyjava.Net.WanResponce;
import com.bear.wanandroidbyjava.Net.WanTypeToken;
import com.bear.wanandroidbyjava.Storage.SysStorage;
import com.bear.wanandroidbyjava.Tool.Helper.DataHelper;
import com.example.libbase.Util.CollectionUtil;
import com.example.libbase.Util.ExecutorUtil;
import com.example.libbase.Util.StringUtil;
import com.example.liblog.SLog;
import com.example.libokhttp.OkHelper;

import java.util.List;

public class NavManager {
    private static final String TAG = "NavManager";
    public void loadDataFromStorage(final NavDataListener listener) {
        ExecutorUtil.execute(new Runnable() {
            @Override
            public void run() {
                List<Nav> navList = SysStorage.getNavList();
                SLog.d(TAG, "loadDataFromStorage: navList = " + navList);
                if (listener != null) {
                    listener.onLoad(navList, false);
                }
            }
        });
    }

    public void loadDataFromNet(final NavDataListener listener) {
        OkHelper.getInstance().getMethod(NetUrl.NAV, new WanOkCallback<List<NavBean>>(WanTypeToken.NAV_TOKEN) {
            @Override
            protected void onSuccess(WanResponce<List<NavBean>> data) {
                if (data != null) {
                    SLog.d(TAG, "loadDataFromNet: data.errorCode = " + data.errorCode + (StringUtil.isEmpty(data.errorMsg) ? "" : ", data.errorMsg = " + data.errorMsg));
                    List<Nav> navList = DataHelper.navBeanToNav(data.data);
                    SLog.d(TAG, "loadDataFromNet: navList.size = " + navList.size() + ", navList = " + navList);
                    if (!CollectionUtil.isEmpty(navList)) {
                        SysStorage.saveNavList(navList);
                    }
                    if (listener != null) {
                        listener.onLoad(navList, true);
                    }
                }
            }

            @Override
            protected void onFail() {
                SLog.d(TAG, "loadNavData: onFail");
            }
        });
    }

    public interface NavDataListener {
        void onLoad(List<Nav> navList, boolean fromNet);
    }
}
