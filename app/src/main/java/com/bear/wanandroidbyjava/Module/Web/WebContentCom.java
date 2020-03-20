package com.bear.wanandroidbyjava.Module.Web;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.bear.wanandroidbyjava.Bean.Article;
import com.bear.wanandroidbyjava.R;
import com.example.libframework.CoreUI.ActComponent;

public class WebContentCom extends ActComponent {
    private static final String TAG = WebAct.TAG + "-WebContentCom";
    private BaseWebView mWvContent;
    private Article mArticle;

    @Override
    protected void onCreate() {
        mArticle = mMain.get(WebAct.KEY_WEB_CONTENT_ARTICLE);
        FrameLayout flWebContainer = findViewById(R.id.fl_web_container);
        mWvContent = new BaseWebView(mMain.getApplicationContext());
        flWebContainer.addView(mWvContent, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        initWebViewClient();
        initWebChromeClient();
        mWvContent.loadUrl(mArticle.link);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWvContent.onDestroy();
        mWvContent = null;
    }

    private void initWebChromeClient() {
        mWvContent.addWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mMain.getComponent(WebInputCom.class).setWebProgress(newProgress);
            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                mMain.getComponent(WebInputCom.class).setWebIcon(icon);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                mMain.getComponent(WebInputCom.class).setWebTitle(title);
            }
        });
    }

    private void initWebViewClient() {
        mWvContent.addWebViewClient(new WebTimeClient());
        mWvContent.addWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                mMain.getComponent(WebInputCom.class).onPageStarted(url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                mMain.getComponent(WebInputCom.class).onPageFinished();
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });
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
