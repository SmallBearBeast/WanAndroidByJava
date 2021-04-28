package com.bear.wanandroidbyjava.Module.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.bear.libcomponent.ComponentAct;
import com.bear.wanandroidbyjava.R;
import com.example.libbase.Util.ScreenUtil;
import com.example.liblog.SLog;

public class MainAct extends ComponentAct {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        regComponent(new MainContentCom());
        regComponent(new MainBottomCom());
        normalScreen();
    }

    private void normalScreen() {
        int color = ContextCompat.getColor(this, R.color.color_FFFFFF);
        View homeRootView = findViewById(R.id.mainRootView);
        ScreenUtil.normalScreen(getWindow(), color, color, homeRootView);
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onBackPressed() {
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            SLog.d(TAG, "" + e);
            super.onBackPressed();
        }
    }

    public static void go(Context context) {
        context.startActivity(new Intent(context, MainAct.class));
    }
}
