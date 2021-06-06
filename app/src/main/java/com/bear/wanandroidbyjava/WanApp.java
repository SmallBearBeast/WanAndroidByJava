package com.bear.wanandroidbyjava;

import android.app.Application;
import android.content.Context;

import com.bear.libkv.SpVal.SpHelper;
import com.bear.wanandroidbyjava.Net.WanCookieJar;
import com.bear.wanandroidbyjava.Storage.CollectInfoStorage;
import com.bear.wanandroidbyjava.Storage.CookieStorage;
import com.bear.wanandroidbyjava.Storage.DataBase.WanRoomDataBase;
import com.bear.wanandroidbyjava.Storage.KV.SpValHelper;
import com.example.libbase.Util.AppInitUtil;
import com.example.libframework.Bus.Bus;
import com.example.libframework.Bus.Event;
import com.example.libframework.Receiver.NetReceiver;
import com.example.liblog.SLog;
import com.example.libokhttp.OkHelper;

import okhttp3.OkHttpClient;

public class WanApp extends Application {
    private static final String TAG = "WanApp";
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        OkHelper.init(this, new OkHttpClient.Builder().cookieJar(new WanCookieJar()));
        AppInitUtil.init(this);
        NetReceiver.init(this, new NetReceiver.NetChangeListener() {
            @Override
            public void onConnected(boolean connect) {
                SLog.d(TAG, "net change connect = " + connect);
                Bus.get().post(new Event(EventKey.KEY_NET_CHANGE, connect));
            }
        });
        WanRoomDataBase.init(this);
        SpHelper.init(this);
        SpHelper.preload(SpValHelper.SP_GLOBAL_CONFIG);
        CookieStorage.init();
        CollectInfoStorage.init();
    }

    public static Context getAppContext() {
        return context;
    }
}
