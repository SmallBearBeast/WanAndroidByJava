package com.bear.wanandroidbyjava.Module.Main;

import android.content.Intent;
import android.os.Bundle;

import com.bear.wanandroidbyjava.R;
import com.example.libframework.CoreUI.ComponentAct;
import com.example.liblog.SLog;

public class MainAct extends ComponentAct {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        regComponent(new MainContentCom());
        regComponent(new MainBottomCom());
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
}
