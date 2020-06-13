package com.bear.wanandroidbyjava.Module.Web;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.bear.wanandroidbyjava.Bean.Article;
import com.bear.wanandroidbyjava.R;
import com.example.libframework.CoreUI.ComponentAct;
import com.example.libframework.CoreUI.ComponentService;
import com.example.libframework.CoreUI.ViewComponent;

public class WebContentCom extends ViewComponent<ComponentAct> {
    private static final String TAG = WebAct.TAG + "-WebContentCom";
    private ComWebView mWvContent;
    private Article mArticle;

    @Override
    protected void onCreate() {
        mArticle = getDependence().get(WebAct.KEY_WEB_CONTENT_ARTICLE);
        FrameLayout flWebContainer = findViewById(R.id.fl_web_container);
        mWvContent = new ComWebView(getDependence().getApplicationContext());
        flWebContainer.addView(mWvContent, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        initWebCallback();
        mWvContent.loadUrl(mArticle.link);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWvContent.onDestroy();
        mWvContent = null;
    }

    private void initWebCallback() {
        mWvContent.addWebCallback(new ComWebView.WebCallback() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                try {
                    Uri uri = Uri.parse(url);
                    String scheme = uri.getScheme();
                    Intent intent = null;
                    if (ComWebView.SCHEME_MARKET.equals(scheme)) {
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    } else if (ComWebView.SCHEME_TEL.equals(scheme)) {
                        intent = new Intent(Intent.ACTION_DIAL, uri);
                    } else if (ComWebView.SCHEME_SMS.equals(scheme) || ComWebView.SCHEME_MAILTO.equals(scheme)) {
                        intent = new Intent(Intent.ACTION_SENDTO, uri);
                    }
                    if (intent != null) {
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getDependence().startActivity(intent);
                        return true;
                    }
                } catch (Exception e) {
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                ComponentService.get().getComponent(WebInputCom.class).onPageStarted(url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                ComponentService.get().getComponent(WebInputCom.class).onPageFinished();
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                ComponentService.get().getComponent(WebInputCom.class).setWebProgress(newProgress);
            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                ComponentService.get().getComponent(WebInputCom.class).setWebIcon(icon);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                ComponentService.get().getComponent(WebInputCom.class).setWebTitle(title);
            }
        });
        mWvContent.addWebCallback(new WebTimeCallback());
    }

    public void goForward() {
        mWvContent.forward();
    }

    public boolean goBack() {
        return mWvContent.back();
    }

    public void loadUrl(String url) {
        mWvContent.loadUrl(url);
    }
}