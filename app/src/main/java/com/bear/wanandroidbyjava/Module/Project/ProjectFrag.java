package com.bear.wanandroidbyjava.Module.Project;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.bear.wanandroidbyjava.R;
import com.example.libframework.CoreUI.ComponentFrag;

public class ProjectFrag extends ComponentFrag {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        regComponent(new ProjectCom());
    }

    @Override
    protected int layoutId() {
        return R.layout.frag_project;
    }

    public static ProjectFrag newInstance() {
        return new ProjectFrag();
    }
}
