package com.bear.wanandroidbyjava.Module.Web;

import android.graphics.Bitmap;
import android.webkit.WebView;

import com.example.libbase.Util.TimeUtil;
import com.example.liblog.SLog;

public class WebTimeCallback extends ComWebView.WebCallback {
    private static final String TAG = WebAct.TAG + "-WebTimeCallback";
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        TimeUtil.markStart(url);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        TimeUtil.markEnd(url);
        SLog.d(TAG, "onPageFinished: url cost time is " + TimeUtil.getDuration(url) + ", url = " + url);
    }
}
