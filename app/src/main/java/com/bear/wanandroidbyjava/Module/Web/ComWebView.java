package com.bear.wanandroidbyjava.Module.Web;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
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

public class ComWebView extends WebView {
    private static final String TAG = "ComWebView";
    public static final String APP_CACHE_PATH = "ComWebView";
    public static final String SCHEME_HTTP = "http";
    public static final String SCHEME_HTTPS = "https";
    public static final String SCHEME_MARKET = "market";
    public static final String SCHEME_INTENT = "intent";
    public static final String SCHEME_TEL = "tel";
    public static final String SCHEME_SMS = "sms";
    public static final String SCHEME_MAILTO = "mailto";
    private static boolean DEBUG = BuildConfig.DEBUG;
    private List<WebCallback> mWebCallbackList = new ArrayList<>();
    private List<String> mValidSchemeList = new ArrayList<>();
    private List<String> mBlackHostList = new ArrayList<>();

    {
        mValidSchemeList.add(SCHEME_HTTP);
        mValidSchemeList.add(SCHEME_HTTPS);
        mValidSchemeList.add(SCHEME_MARKET);
        mValidSchemeList.add(SCHEME_INTENT);
        mValidSchemeList.add(SCHEME_TEL);
        mValidSchemeList.add(SCHEME_SMS);
        mValidSchemeList.add(SCHEME_MAILTO);
    }

    public ComWebView(Context context) {
        this(context, null);
    }

    public ComWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setFocusable(true);
        setFocusableInTouchMode(true);
        setEnabled(true);
        setWebContentsDebuggingEnabled(BuildConfig.DEBUG);

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
                for (WebCallback webCallback : mWebCallbackList) {
                    webCallback.onProgressChanged(view, newProgress);
                }
            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                if (DEBUG) {
                    SLog.d(TAG, "onReceivedIcon: ");
                }
                for (WebCallback webCallback : mWebCallbackList) {
                    webCallback.onReceivedIcon(view, icon);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                if (DEBUG) {
                    SLog.d(TAG, "onReceivedTitle: title = " + title);
                }
                for (WebCallback webCallback : mWebCallbackList) {
                    webCallback.onReceivedTitle(view, title);
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
                    boolean result = false;
                    for (WebCallback webCallback : mWebCallbackList) {
                        result = result || webCallback.shouldOverrideUrlLoading(view, url);
                    }
                    return result;
                }
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (DEBUG) {
                    SLog.d(TAG, "onPageStarted: url = " + url);
                }
                for (WebCallback webCallback : mWebCallbackList) {
                    webCallback.onPageStarted(view, url, favicon);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (DEBUG) {
                    SLog.d(TAG, "onPageFinished: url = " + url);
                }
                for (WebCallback webCallback : mWebCallbackList) {
                    webCallback.onPageFinished(view, url);
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                if (DEBUG) {
                    SLog.d(TAG, "onReceivedError: errorCode = " + errorCode + ", description = " + description + ", failingUrl = " + failingUrl);
                }
                if ((failingUrl != null && !failingUrl.equals(view.getUrl()) && !failingUrl.equals(view.getOriginalUrl()))
                        || (failingUrl == null && errorCode != ERROR_BAD_URL) || errorCode == ERROR_UNKNOWN) {
                    return;
                }
                if (!TextUtils.isEmpty(failingUrl)) {
                    if (failingUrl.equals(view.getUrl())) {
                        for (WebCallback webCallback : mWebCallbackList) {
                            webCallback.onReceivedError(view, errorCode, description, failingUrl);
                        }
                    }
                }
            }


            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                if (DEBUG) {
                    SLog.d(TAG, "onReceivedSslError: url = " + error.getUrl());
                }
                for (WebCallback webCallback : mWebCallbackList) {
                    webCallback.onReceivedSslError(view, handler, error);
                }
            }
        });
    }

    private void initWebSettings() {
        WebSettings webSettings = getSettings();
        // Allow running js code.
        webSettings.setJavaScriptEnabled(true);
        // Allow js code to open new window.
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        // WebView page adjustment.
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        // Open mixed mode to load resource above Android 5.0, It may not be safe.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        // Allow SessionStorage/LocalStorage storage.
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        // Disable zoom.
        webSettings.setDisplayZoomControls(false);
        webSettings.setBuiltInZoomControls(false);
        // Disable text zoom.
        webSettings.setTextZoom(100);
        // Allow cache and set cache location.
//        webSettings.setAppCacheEnabled(true);
//        webSettings.setAppCachePath(getContext().getDir(APP_CACHE_PATH, 0).getPath());
        // Allow WebView to use File protocol.
        webSettings.setAllowFileAccess(true);
        // Set UA.
        webSettings.setUserAgentString(webSettings.getUserAgentString() + getContext().getPackageName());
        webSettings.setLoadsImagesAutomatically(true);
        // Allow third-party cookies.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true);
        }
        setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                for (WebCallback webCallback : mWebCallbackList) {
                    webCallback.onDownloadStart(url, userAgent, contentDisposition, mimetype, contentLength);
                }
            }
        });

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

    public void addWebCallback(@NonNull WebCallback webCallback) {
        mWebCallbackList.add(webCallback);
    }

    public void addValidScheme(@NonNull String scheme) {
        mValidSchemeList.add(scheme);
    }

    public void addBlackHost(@NonNull String host) {
        mBlackHostList.add(host);
    }

    public static class WebCallback {
        public void onProgressChanged(WebView view, int newProgress) {

        }

        public void onReceivedIcon(WebView view, Bitmap icon) {

        }

        public void onReceivedTitle(WebView view, String title) {

        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {

        }

        public void onPageFinished(WebView view, String url) {

        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

        }

        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {

        }

        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {

        }
    }
}
