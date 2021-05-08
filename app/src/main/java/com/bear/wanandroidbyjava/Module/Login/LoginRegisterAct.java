package com.bear.wanandroidbyjava.Module.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bear.libcomponent.ComponentAct;
import com.bear.wanandroidbyjava.R;

public class LoginRegisterAct extends ComponentAct {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        regComponent(new LoginRegisterCom());
    }

    @Override
    protected int layoutId() {
        return R.layout.act_login_register;
    }

    public static void go(Context context) {
        Intent intent = new Intent(context, LoginRegisterAct.class);
        ContextCompat.startActivity(context, intent, null);
    }
}
