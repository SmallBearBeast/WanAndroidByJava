package com.bear.wanandroidbyjava.Module.Public;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.bear.libcomponent.ComponentFrag;
import com.bear.wanandroidbyjava.R;

public class PublicFrag extends ComponentFrag {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        regComponent(new PublicCom());
    }

    @Override
    protected int layoutId() {
        return R.layout.frag_public;
    }

    public static PublicFrag newInstance() {
        return new PublicFrag();
    }
}
