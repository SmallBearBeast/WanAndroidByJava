package com.bear.wanandroidbyjava.Module.Web;

import android.graphics.Bitmap;

import androidx.annotation.Nullable;

public interface IWebInputCom {
    void setWebProgress(int progress);
    void onPageStarted(String url);
    void onPageFinished();
    void setWebTitle(String title);
    void setWebIcon(Bitmap bitmap);
    @Nullable String getWebLink();
}
