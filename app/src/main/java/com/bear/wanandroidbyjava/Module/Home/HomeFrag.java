package com.bear.wanandroidbyjava.Module.Home;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.bear.wanandroidbyjava.R;
import com.example.libframework.CoreUI.ComponentFrag;

public class HomeFrag extends ComponentFrag {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        regComponent(new HomeTopBarCom());
        regComponent(new HomeListCom());
    }

    @Override
    protected int layoutId() {
        return R.layout.frag_home;
    }

    public static HomeFrag newInstance() {
        return new HomeFrag();
    }
}
