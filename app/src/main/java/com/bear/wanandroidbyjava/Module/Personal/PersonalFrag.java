package com.bear.wanandroidbyjava.Module.Personal;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.bear.libcomponent.component.ComponentFrag;
import com.bear.wanandroidbyjava.R;

public class PersonalFrag extends ComponentFrag {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        regFragComponent(new PersonalCom());
    }

    @Override
    protected int layoutId() {
        return R.layout.frag_personal;
    }

    public static PersonalFrag newInstance() {
        return new PersonalFrag();
    }
}
