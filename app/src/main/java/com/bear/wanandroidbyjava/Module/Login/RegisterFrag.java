package com.bear.wanandroidbyjava.Module.Login;

import android.os.Bundle;
import androidx.annotation.Nullable;

import com.bear.libcomponent.component.ComponentFrag;
import com.bear.wanandroidbyjava.R;

public class RegisterFrag extends ComponentFrag {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        regFragComponent(new RegisterCom(getLifecycle()));
    }

    @Override
    protected int layoutId() {
        return R.layout.frag_register;
    }

    public static RegisterFrag newInstance() {
        return new RegisterFrag();
    }
}
