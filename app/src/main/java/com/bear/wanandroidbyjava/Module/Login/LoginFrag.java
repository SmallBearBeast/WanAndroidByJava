package com.bear.wanandroidbyjava.Module.Login;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.bear.libcomponent.component.ComponentFrag;
import com.bear.wanandroidbyjava.R;

public class LoginFrag extends ComponentFrag {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        regFragComponent(new LoginCom(getLifecycle()));
    }

    @Override
    protected int layoutId() {
        return R.layout.frag_login;
    }

    public static LoginFrag newInstance() {
        return new LoginFrag();
    }
}
