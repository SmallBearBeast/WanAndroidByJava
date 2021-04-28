package com.bear.wanandroidbyjava.Module.Welcome;

import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.bear.libcomponent.ComponentAct;
import com.bear.wanandroidbyjava.Module.Main.MainAct;
import com.bear.wanandroidbyjava.R;
import com.example.libbase.Animator;
import com.example.libbase.Executor.MainThreadExecutor;
import com.example.libbase.Util.ScreenUtil;

public class WelcomeAct extends ComponentAct {

    private final Runnable startMainActRunnable = new Runnable() {
        @Override
        public void run() {
            finish();
            MainAct.go(WelcomeAct.this);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.WanTheme);
        super.onCreate(savedInstanceState);
        ScreenUtil.immersiveFullScreen(getWindow());
        initWelcomeTitleTv();
        MainThreadExecutor.postDelayed(startMainActRunnable, 1000);
    }

    private void initWelcomeTitleTv() {
        TextView welcomeTitleTv = findViewById(R.id.welcomeTitleTv);
        welcomeTitleTv.setAlpha(0f);
        Animator.make(welcomeTitleTv, Animator.ALPHA, 0f, 1f).duration(300).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ScreenUtil.immersiveFullScreen(getWindow());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MainThreadExecutor.removeCallbacks(startMainActRunnable);
    }

    @Override
    protected int layoutId() {
        return R.layout.act_welcome;
    }
}
