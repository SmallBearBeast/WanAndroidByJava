package com.bear.wanandroidbyjava.Module.Home;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.bear.libcomponent.component.ComponentFrag;
import com.bear.wanandroidbyjava.R;

public class HomeFrag extends ComponentFrag {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        regFragComponent(new HomeTopBarCom());
        regFragComponent(new HomeListCom());
    }

    @Override
    protected int layoutId() {
        return R.layout.frag_home;
    }

    public static HomeFrag newInstance() {
        return new HomeFrag();
    }
}
