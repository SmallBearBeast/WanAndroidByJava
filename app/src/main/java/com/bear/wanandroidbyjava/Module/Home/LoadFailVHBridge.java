package com.bear.wanandroidbyjava.Module.Home;

import android.view.View;

import androidx.annotation.NonNull;

import com.bear.librv.VHolder;
import com.bear.wanandroidbyjava.R;
import com.bear.wanandroidbyjava.base.ComponentVHBridge;

@SuppressWarnings({"rawtypes"})
public class LoadFailVHBridge extends ComponentVHBridge<VHolder> implements View.OnClickListener {
    @NonNull
    @Override
    protected VHolder onCreateViewHolder(@NonNull View view) {
        view.setOnClickListener(this);
        return new VHolder(view);
    }

    @Override
    protected int layoutId() {
        return R.layout.item_load_fail;
    }

    @Override
    public void onClick(View v) {
        HomeListCom homeListCom = getComponent(HomeListCom.class);
        if (homeListCom != null) {
            homeListCom.loadMore();
        }
    }
}
