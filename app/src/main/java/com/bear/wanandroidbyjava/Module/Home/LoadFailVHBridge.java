package com.bear.wanandroidbyjava.Module.Home;

import android.view.View;

import androidx.annotation.NonNull;

import com.bear.libcomponent.component.ComponentService;
import com.bear.librv.VHBridge;
import com.bear.librv.VHolder;
import com.bear.wanandroidbyjava.R;

@SuppressWarnings({"rawtypes"})
public class LoadFailVHBridge extends VHBridge implements View.OnClickListener{
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
        ComponentService.get().getComponent(HomeListCom.class).loadMore();
    }
}
