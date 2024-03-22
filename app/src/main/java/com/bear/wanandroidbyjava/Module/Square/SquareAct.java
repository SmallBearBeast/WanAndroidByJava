package com.bear.wanandroidbyjava.Module.Square;

import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;

import com.bear.libcomponent.component.ComponentAct;

public class SquareAct extends ComponentAct {
    @Override
    protected int layoutId() {
        return 0;
    }

    public static void go(Context context) {
        Intent intent = new Intent(context, SquareAct.class);
        ContextCompat.startActivity(context, intent, null);
    }
}
