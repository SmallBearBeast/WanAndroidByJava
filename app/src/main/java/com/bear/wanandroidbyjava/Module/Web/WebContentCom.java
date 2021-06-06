package com.bear.wanandroidbyjava.Module.Web;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.bear.libcomponent.ComponentAct;
import com.bear.libcomponent.ComponentService;
import com.bear.libcomponent.ShareVM;
import com.bear.libcomponent.ViewComponent;
import com.bear.wanandroidbyjava.Data.Bean.Article;
import com.bear.wanandroidbyjava.Module.Collect.CollectInfo;
import com.bear.wanandroidbyjava.R;

public class WebContentCom extends ViewComponent<ComponentAct> implements IWebContentCom {
    private static final String TAG = WebAct.TAG + "-WebContentCom";
    private ComWebView mWvContent;

    @Override
    protected void onCreate() {
        FrameLayout flWebContainer = findViewById(R.id.fl_web_container);
        mWvContent = new ComWebView(getDependence().getApplicationContext());
        mWvContent.setOverScrollMode(View.OVER_SCROLL_NEVER);
        flWebContainer.addView(mWvContent, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        initWebCallback();
        CollectInfo collectInfo = ShareVM.get(getDependence(), WebAct.KEY_WEB_COLLECT_INFO);
        mWvContent.loadUrl(collectInfo.getLink());
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
                ComponentService.get().getComponent(IWebInputCom.class).onPageStarted(url);
                updateBackAndForwardAction();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                ComponentService.get().getComponent(IWebInputCom.class).onPageFinished();
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                ComponentService.get().getComponent(IWebInputCom.class).setWebProgress(newProgress);
            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                ComponentService.get().getComponent(IWebInputCom.class).setWebIcon(icon);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                ComponentService.get().getComponent(IWebInputCom.class).setWebTitle(title);
            }
        });
        mWvContent.addWebCallback(new WebTimeCallback());
    }

    @Override
    public void goForward() {
        mWvContent.forward();
    }

    @Override
    public boolean goBack() {
        return mWvContent.back();
    }

    private void updateBackAndForwardAction() {
        boolean canGoForward = mWvContent.canGoForward();
        ComponentService.get().getComponent(IWebActionCom.class).setForwardEnable(canGoForward);
        boolean canGoBack = mWvContent.canGoBack();
        ComponentService.get().getComponent(IWebActionCom.class).setBackEnable(canGoBack);
    }

    @Override
    public void loadUrl(String url) {
        mWvContent.loadUrl(url);
    }

    @Override
    public void goBackHome() {
        int steps = -1;
        while (mWvContent.canGoBackOrForward(steps)) {
            steps --;
        }
        mWvContent.goBackOrForward(steps + 1);
    }

    @Override
    public void reload() {
        mWvContent.reload();
    }
}
