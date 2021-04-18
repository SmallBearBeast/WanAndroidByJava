package com.bear.wanandroidbyjava.Module.Web;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bear.libcomponent.ComponentAct;
import com.bear.libcomponent.ShareVM;
import com.bear.wanandroidbyjava.R;
import com.example.libbase.Util.ScreenUtil;

public class WebAct extends ComponentAct {
    public static final String TAG = "web_tag";
    public static final String KEY_WEB_LINK = "key_web_link";
    public static final String KEY_WEB_TITLE = "key_web_title";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        regComponent(new WebInputCom());
        regComponent(new WebActionCom());
        regComponent(new WebContentCom());
        normalScreen();
    }

    private void normalScreen() {
        int color = ContextCompat.getColor(this, R.color.color_ffffff);
        View homeRootView = findViewById(R.id.webRootView);
        ScreenUtil.normalScreen(getWindow(), color, color, homeRootView);
    }

    @Override
    protected void handleIntent(Intent intent) {
        String title = intent.getStringExtra(KEY_WEB_TITLE);
        String link = intent.getStringExtra(KEY_WEB_LINK);
        ShareVM.put(this, KEY_WEB_TITLE, title);
        ShareVM.put(this, KEY_WEB_LINK, link);
    }

    @Override
    protected int layoutId() {
        return R.layout.act_web;
    }

    // TODO: 2020-04-19 WebAct is set to an independent process when version is less than 7.
    public static void go(Context context, String title, @NonNull String link) {
        Intent intent = new Intent(context, WebAct.class);
        intent.putExtra(KEY_WEB_TITLE, title);
        intent.putExtra(KEY_WEB_LINK, link);
        ContextCompat.startActivity(context, intent, null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!getComponent(WebContentCom.class).goBack()) {
            finish();
        }
    }
}
