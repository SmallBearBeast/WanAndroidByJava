package com.bear.wanandroidbyjava.Module.Web;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bear.libcomponent.ShareVM;
import com.bear.libcomponent.component.ComponentAct;
import com.bear.wanandroidbyjava.Data.Bean.Article;
import com.bear.wanandroidbyjava.Data.Bean.Banner;
import com.bear.wanandroidbyjava.Module.Collect.CollectInfo;
import com.bear.wanandroidbyjava.R;
import com.example.libbase.Util.ScreenUtil;

public class WebAct extends ComponentAct {
    public static final String TAG = "web_tag";
    public static final String KEY_WEB_COLLECT_INFO = "key_web_collect_info";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        regActComponent(new WebInputCom(getLifecycle()));
        regActComponent(new WebActionCom(getLifecycle()));
        regActComponent(new WebContentCom(getLifecycle()));
        regActComponent(new WebLinkCom(getLifecycle()));
        normalScreen();
    }

    private void normalScreen() {
        int color = ContextCompat.getColor(this, R.color.color_FFFFFF);
        View homeRootView = findViewById(R.id.webRootView);
        ScreenUtil.normalScreen(getWindow(), color, color, homeRootView);
    }

    @Override
    protected void handleIntent(Intent intent) {
        CollectInfo collectInfo = intent.getParcelableExtra(KEY_WEB_COLLECT_INFO);
        ShareVM.put(this, KEY_WEB_COLLECT_INFO, collectInfo);
    }

    @Override
    protected int layoutId() {
        return R.layout.act_web;
    }

    // TODO: 2020-04-19 WebAct is set to an independent process when version is less than 7.
    public static void go(Context context, @NonNull Article article) {
        Intent intent = new Intent(context, WebAct.class);
        intent.putExtra(KEY_WEB_COLLECT_INFO, article.toCollectInfo());
        ContextCompat.startActivity(context, intent, null);
    }

    public static void go(Context context, @NonNull Banner banner) {
        Intent intent = new Intent(context, WebAct.class);
        intent.putExtra(KEY_WEB_COLLECT_INFO, banner.toCollectInfo());
        ContextCompat.startActivity(context, intent, null);
    }

    @Override
    public void onBackPressed() {
        if (!getComponent(WebContentCom.class).goBack()) {
            super.onBackPressed();
        }
    }
}
