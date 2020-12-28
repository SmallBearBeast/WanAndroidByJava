package com.bear.wanandroidbyjava.Module.Home;

import android.view.View;

import androidx.annotation.NonNull;

import com.bear.librv.VHBridge;
import com.bear.librv.VHolder;
import com.bear.wanandroidbyjava.R;

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
