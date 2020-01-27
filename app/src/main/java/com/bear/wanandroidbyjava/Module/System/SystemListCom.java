package com.bear.wanandroidbyjava.Module.System;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.libframework.CoreUI.FragComponent;
import com.example.libframework.Rv.DataManager;
import com.example.libframework.Rv.RvUtil;
import com.example.libframework.Rv.VHAdapter;

public class SystemListCom extends FragComponent {
    private RecyclerView mRecyclerView;
    private VHAdapter mVhAdapter;
    private DataManager mDataManager;

    @Override
    protected void onCreateView(View contentView) {

    }

    public void scrollToTop() {
        RvUtil.scrollToTop(mRecyclerView, true);
    }
}
