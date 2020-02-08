package com.bear.wanandroidbyjava.Module.Home;

import android.view.View;

import androidx.annotation.NonNull;

import com.bear.wanandroidbyjava.R;
import com.example.libframework.Rv.VHBridge;
import com.example.libframework.Rv.VHolder;

public class NoMoreDataVHBridge extends VHBridge {
    @NonNull
    @Override
    protected VHolder onCreateViewHolder(@NonNull View view) {
        return new VHolder(view);
    }

    @Override
    protected int layoutId() {
        return R.layout.item_no_more_data;
    }
}
