package com.bear.wanandroidbyjava;

import android.app.Application;

import com.example.libbase.Util.AppInitUtil;
import com.example.libframework.Bus.Bus;
import com.example.libframework.Bus.Event;
import com.example.libframework.Receiver.NetReceiver;
import com.example.liblog.SLog;
import com.example.libokhttp.OkHelper;

public class WanApp extends Application {
    private static final String TAG = "WanApp";
    @Override
    public void onCreate() {
        super.onCreate();
        OkHelper.init(this, null);
        AppInitUtil.init(this);
        NetReceiver.init(this, new NetReceiver.NetChangeListener() {
            @Override
            public void onConnected(boolean connect) {
                SLog.d(TAG, "net change connect = " + connect);
                Bus.get().post(new Event(EventKey.KEY_NET_CHANGE, connect));
            }
        });
        WanRoomDataBase.init(this);
    }
}
