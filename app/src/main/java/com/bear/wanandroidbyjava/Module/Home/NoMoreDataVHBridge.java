package com.bear.wanandroidbyjava.Module.Home;

import android.view.View;

import androidx.annotation.NonNull;

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
        return 0;
    }
}
