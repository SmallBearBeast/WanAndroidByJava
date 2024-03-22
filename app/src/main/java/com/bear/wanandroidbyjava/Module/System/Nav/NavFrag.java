package com.bear.wanandroidbyjava.Module.System.Nav;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.bear.libcomponent.component.ComponentFrag;
import com.bear.wanandroidbyjava.R;

public class NavFrag extends ComponentFrag {
    @Override
    protected int layoutId() {
        return R.layout.frag_nav;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        regFragComponent(new NavCom());
    }

    public static NavFrag newInstance() {
        return new NavFrag();
    }

}
