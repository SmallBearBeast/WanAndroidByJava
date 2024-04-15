package com.bear.wanandroidbyjava.Module.Project;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.bear.libcomponent.component.ComponentFrag;
import com.bear.wanandroidbyjava.R;

public class ProjectFrag extends ComponentFrag {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        regFragComponent(new ProjectCom(getLifecycle()));
    }

    @Override
    protected int layoutId() {
        return R.layout.frag_project;
    }

    public static ProjectFrag newInstance() {
        return new ProjectFrag();
    }
}
