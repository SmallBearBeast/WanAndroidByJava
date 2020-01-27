package com.bear.wanandroidbyjava.Module.Main;

import android.os.Bundle;

import com.bear.wanandroidbyjava.R;
import com.example.libframework.CoreUI.ComponentAct;

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
}
