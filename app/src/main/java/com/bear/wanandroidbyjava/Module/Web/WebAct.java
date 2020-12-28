package com.bear.wanandroidbyjava.Module.Web;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bear.libcomponent.ComponentAct;
import com.bear.libcomponent.ShareVM;
import com.bear.wanandroidbyjava.Bean.Article;
import com.bear.wanandroidbyjava.R;

public class WebAct extends ComponentAct {
    public static final String TAG = "web_tag";
    public static final String KEY_WEB_CONTENT_ARTICLE = "key_web_content_article";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        regComponent(new WebInputCom());
        regComponent(new WebActionCom());
        regComponent(new WebContentCom());
    }

    @Override
    protected void handleIntent(Intent intent) {
        Article article = intent.getParcelableExtra(KEY_WEB_CONTENT_ARTICLE);
        if (article != null) {
            ShareVM.put(this, KEY_WEB_CONTENT_ARTICLE, article);
        }
    }

    @Override
    protected int layoutId() {
        return R.layout.act_web;
    }

    // TODO: 2020-04-19 WebAct is set to an independent process when version is less than 7.
    public static void go(Context context, Article article) {
        Intent intent = new Intent(context, WebAct.class);
        intent.putExtra(KEY_WEB_CONTENT_ARTICLE, article);
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
