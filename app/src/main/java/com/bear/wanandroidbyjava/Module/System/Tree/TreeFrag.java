package com.bear.wanandroidbyjava.Module.System.Tree;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.bear.libcomponent.component.ComponentFrag;
import com.bear.wanandroidbyjava.R;

public class TreeFrag extends ComponentFrag {
    @Override
    protected int layoutId() {
        return R.layout.frag_tree;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        regFragComponent(new TreeCom());
    }

    public static TreeFrag newInstance() {
        return new TreeFrag();
    }
}
