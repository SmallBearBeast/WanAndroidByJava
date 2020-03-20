package com.bear.wanandroidbyjava.Module.Web;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;

import com.bear.wanandroidbyjava.BuildConfig;
import com.example.liblog.SLog;

import java.util.ArrayList;
import java.util.List;

public class BaseWebView extends WebView {
    private static final String TAG = "BaseWebView";
    private static boolean DEBUG = BuildConfig.DEBUG;
    private List<WebViewClient> mWebViewClientList = new ArrayList<>();
    private List<WebChromeClient> mWebChromeClientList = new ArrayList<>();
    private List<String> mValidSchemeList = new ArrayList<>();
    private List<String> mBlackHostList = new ArrayList<>();

    {
        mValidSchemeList.add("http");
        mValidSchemeList.add("https");
        mValidSchemeList.add("market");
        mValidSchemeList.add("intent");
    }

    public BaseWebView(Context context) {
        this(context, null);
    }

    public BaseWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setFocusable(true);
        setFocusableInTouchMode(true);
        setEnabled(true);

        initWebSettings();
        initWebViewClient();
        initWebChromeClient();
    }

    private void initWebChromeClient() {
        setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (DEBUG) {
                    SLog.d(TAG, "onProgressChanged: newProgress = " + newProgress);
                }
                for (WebChromeClient client : mWebChromeClientList) {
                    client.onProgressChanged(view, newProgress);
                }
            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                if (DEBUG) {
                    SLog.d(TAG, "onReceivedIcon: ");
                }
                for (WebChromeClient client : mWebChromeClientList) {
                    client.onReceivedIcon(view, icon);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                if (DEBUG) {
                    SLog.d(TAG, "onReceivedTitle: title = " + title);
                }
                for (WebChromeClient client : mWebChromeClientList) {
                    client.onReceivedTitle(view, title);
                }
            }
        });
    }

    private void initWebViewClient() {
        setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (DEBUG) {
                    SLog.d(TAG, "shouldOverrideUrlLoading: url = " + url);
                }
                Uri uri = Uri.parse(url);
                String host = uri.getHost();
                String scheme = uri.getScheme();
                if (mBlackHostList.contains(host)) {
                    SLog.d(TAG, "shouldOverrideUrlLoading: url is black url");
                    return true;
                }
                if (mValidSchemeList.contains(scheme)) {
                    for (WebViewClient client : mWebViewClientList) {
                        client.shouldOverrideUrlLoading(view, url);
                    }
                    return false;
                }
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (DEBUG) {
                    SLog.d(TAG, "onPageStarted: url = " + url);
                }
                for (WebViewClient client : mWebViewClientList) {
                    client.onPageStarted(view, url, favicon);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (DEBUG) {
                    SLog.d(TAG, "onPageFinished: url = " + url);
                }
                for (WebViewClient client : mWebViewClientList) {
                    client.onPageFinished(view, url);
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                if (DEBUG) {
                    SLog.d(TAG, "onReceivedError: errorCode = " + errorCode + ", description = " + description + ", failingUrl = " + failingUrl);
                }
                for (WebViewClient client : mWebViewClientList) {
                    client.onReceivedError(view, errorCode, description, failingUrl);
                }
            }


            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                if (DEBUG) {
                    SLog.d(TAG, "onReceivedSslError: url = " + error.getUrl());
                }
                for (WebViewClient client : mWebViewClientList) {
                    client.onReceivedSslError(view, handler, error);
                }
                handler.proceed();
            }
        });
    }

    private void initWebSettings() {
        WebSettings webSettings = getSettings();
        // 允许js代码
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        // web页面调整
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        // 5.0以上开启混合模式加载
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        // 允许SessionStorage/LocalStorage存储
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        // 禁用放缩
        webSettings.setDisplayZoomControls(false);
        webSettings.setBuiltInZoomControls(false);
        // 允许缓存，设置缓存位置
        webSettings.setAppCacheEnabled(true);
        webSettings.setAppCachePath(getContext().getDir("WanWebCache", 0).getPath());
        // 允许WebView使用File协议
        webSettings.setAllowFileAccess(true);
        // 设置UA
        webSettings.setUserAgentString(webSettings.getUserAgentString() + getContext().getPackageName());
        // 自动加载图片
        webSettings.setLoadsImagesAutomatically(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        resumeTimers();
    }

    @Override
    public void onPause() {
        super.onPause();
        pauseTimers();
    }

    public void onDestroy() {
        loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
        clearHistory();
        ((ViewGroup) getParent()).removeView(this);
        destroy();
    }

    public boolean forward() {
        if (canGoForward()) {
            goForward();
            return true;
        }
        return false;
    }

    public boolean back() {
        if (canGoBack()) {
            goBack();
            return true;
        }
        return false;
    }

    public void addWebViewClient(@NonNull WebViewClient webViewClient) {
        mWebViewClientList.add(webViewClient);
    }

    public void addWebChromeClient(@NonNull WebChromeClient webChromeClient) {
        mWebChromeClientList.add(webChromeClient);
    }
}
