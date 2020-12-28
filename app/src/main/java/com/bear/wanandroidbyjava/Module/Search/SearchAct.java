package com.bear.wanandroidbyjava.Module.Search;

import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;

import com.bear.libcomponent.ComponentAct;

public class SearchAct extends ComponentAct {
    @Override
    protected int layoutId() {
        return 0;
    }

    public static void go(Context context) {
        Intent intent = new Intent(context, SearchAct.class);
        ContextCompat.startActivity(context, intent, null);
    }
}
