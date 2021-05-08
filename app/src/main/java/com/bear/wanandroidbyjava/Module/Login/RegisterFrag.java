package com.bear.wanandroidbyjava.Module.Login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bear.libcomponent.ComponentFrag;
import com.bear.wanandroidbyjava.R;

public class RegisterFrag extends ComponentFrag {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        regComponent(new RegisterCom());
    }

    @Override
    protected int layoutId() {
        return R.layout.frag_register;
    }

    public static RegisterFrag newInstance() {
        return new RegisterFrag();
    }
}
