package com.bear.wanandroidbyjava.Module.Personal;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.bear.wanandroidbyjava.R;
import com.example.libframework.CoreUI.ComponentFrag;

public class PersonalFrag extends ComponentFrag {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        regComponent(new PersonalCom());
    }

    @Override
    protected int layoutId() {
        return R.layout.frag_personal;
    }

    public static PersonalFrag newInstance() {
        return new PersonalFrag();
    }
}
