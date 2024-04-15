package com.bear.wanandroidbyjava.Module.Login;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bear.libcomponent.component.ComponentAct;
import com.bear.wanandroidbyjava.R;
import com.bear.wanandroidbyjava.WanApp;
import com.example.liblog.SLog;

public class LoginRegisterAct extends ComponentAct {

    private static final String TAG = "LoginRegisterAct";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        regActComponent(new LoginRegisterCom(getLifecycle()));
        ((WanApp)WanApp.getAppContext()).registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {

            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {

            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {
                SLog.d(TAG, "onActivityPaused");
            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {
                SLog.d(TAG, "onActivityStopped");
            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

            }

            @Override
            public void onActivityPreDestroyed(@NonNull Activity activity) {
                SLog.d(TAG, "onActivityPreDestroyed");
            }

            @Override
            public void onActivityPostDestroyed(@NonNull Activity activity) {
                SLog.d(TAG, "onActivityPostDestroyed");
            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
                SLog.d(TAG, "onActivityDestroyed");
                ((WanApp)WanApp.getAppContext()).unregisterActivityLifecycleCallbacks(this);
            }
        });
    }

    @Override
    protected int layoutId() {
        return R.layout.act_login_register;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
