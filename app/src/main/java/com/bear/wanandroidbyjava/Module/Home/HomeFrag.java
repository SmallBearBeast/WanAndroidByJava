package com.bear.wanandroidbyjava.Module.Home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bear.wanandroidbyjava.R;
import com.example.libframework.CoreUI.ComponentFrag;

public class HomeFrag extends ComponentFrag {
    private int fragId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        regComponent(new HomeTopBarCom(), fragId);
        regComponent(new HomeListCom(), fragId);
    }

    @Override
    protected int layoutId() {
        return R.layout.frag_home;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void handleArgument(@NonNull Bundle bundle) {
        fragId = bundle.getInt("id");
    }

    public static HomeFrag newInstance(int id) {
        HomeFrag homeFrag = new HomeFrag();
        Bundle argument = new Bundle();
        argument.putInt("id", id);
        homeFrag.setArguments(argument);
        return homeFrag;
    }
}
