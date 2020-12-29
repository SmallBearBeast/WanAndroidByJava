package com.bear.wanandroidbyjava.Module.Home;

import android.view.View;

import androidx.annotation.NonNull;

import com.bear.librv.VHBridge;
import com.bear.librv.VHolder;
import com.bear.wanandroidbyjava.R;

@SuppressWarnings({"rawtypes"})
public class LoadMoreVHBridge extends VHBridge {
    @NonNull
    @Override
    protected VHolder onCreateViewHolder(@NonNull View view) {
        return new VHolder(view);
    }

    @Override
    protected int layoutId() {
        return R.layout.item_load_more;
    }
}
