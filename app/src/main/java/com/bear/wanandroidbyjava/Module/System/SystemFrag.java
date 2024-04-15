package com.bear.wanandroidbyjava.Module.System;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.bear.libcomponent.component.ComponentFrag;
import com.bear.wanandroidbyjava.R;

public class SystemFrag extends ComponentFrag {
    @Override
    protected int layoutId() {
        return R.layout.frag_system;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        regFragComponent(new SystemCom(getLifecycle()));
    }

    public static SystemFrag newInstance() {
        return new SystemFrag();
    }
}
