package com.bear.wanandroidbyjava.Module.System;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.bear.wanandroidbyjava.R;
import com.example.libframework.CoreUI.ComponentFrag;

public class SystemFrag extends ComponentFrag {
    @Override
    protected int layoutId() {
        return R.layout.frag_system;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        regComponent(new SystemCom());
    }

    public static SystemFrag newInstance() {
        return new SystemFrag();
    }
}
