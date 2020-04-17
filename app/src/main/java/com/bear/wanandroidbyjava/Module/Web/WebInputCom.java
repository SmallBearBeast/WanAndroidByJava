package com.bear.wanandroidbyjava.Module.Web;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.IntDef;

import com.bear.wanandroidbyjava.Bean.Article;
import com.bear.wanandroidbyjava.R;
import com.example.libbase.Util.CollectionUtil;
import com.example.libbase.Util.OtherUtil;
import com.example.libbase.Util.StringUtil;
import com.example.libbase.Util.ToastUtil;
import com.example.libframework.CoreUI.ActComponent;
import com.example.libframework.Wrapper.TextWatcherWrapper;
import com.example.liblog.SLog;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebInputCom extends ActComponent implements View.OnClickListener {
    private static final String TAG = WebAct.TAG + "-WebInputCom";

    public static final int BROWSER_TYPE_BAIDU = 1;
    public static final int BROWSER_TYPE_BING = 2;
    public static final int BROWSER_TYPE_SOGOU = 3;
    public static final int BROWSER_TYPE_GOOGLE = 4;

    private static final String BROWSER_URL_BAIDU = "https://www.baidu.com/s?wd=";
    private static final String BROWSER_URL_BING = "https://cn.bing.com/search?q=";
    private static final String BROWSER_URL_SOGOU = "https://www.sogou.com/web?query=";
    private static final String BROWSER_URL_GOOGLE = "https://www.google.com.hk/search?q=";

    private ImageView mIvCollect;
    private ImageView mIvWebIcon;
    private EditText mEtSearchInput;
    private ImageView mIvClearInput;
    private TextView mTvSearch;
    private ProgressBar mPbWebLoading;
    private Article mArticle;
    private String mWebSearchText;
    private String mWebTitle;

    private boolean mIsLoading = false;
    private @BrowserType int mSearchType = BROWSER_TYPE_BAIDU;

    @Override
    protected void onCreate() {
        mArticle = mMain.get(WebAct.KEY_WEB_CONTENT_ARTICLE);
        mWebSearchText = mArticle.link;
        mWebTitle = mArticle.title;
        mIvCollect = findViewById(R.id.iv_collect);
        mEtSearchInput = findViewById(R.id.et_search_input);
        mIvClearInput = findViewById(R.id.iv_clear_input);
        mIvClearInput.setVisibility(View.INVISIBLE);
        mTvSearch = findViewById(R.id.tv_search);
        mPbWebLoading = findViewById(R.id.pb_web_loading);
        mIvWebIcon = findViewById(R.id.iv_web_icon);
        setUpEditText();
        setOnClickListener(this, R.id.iv_collect, R.id.iv_clear_input, R.id.tv_search);
    }

    private void setUpEditText() {
        mEtSearchInput.setText(mWebSearchText);
        setEditTextEnable(false);
        mEtSearchInput.addTextChangedListener(new TextWatcherWrapper() {
            @Override
            public void afterTextChanged(Editable s) {
                boolean focus = mEtSearchInput.hasFocus();
                mIvClearInput.setVisibility(focus && s.length() > 0 ? View.VISIBLE : View.INVISIBLE);
                if (!mWebTitle.equals(s.toString())) {
                    mWebSearchText = s.toString();
                }
            }
        });
        mEtSearchInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                SLog.d(TAG, "hasFocus = " + hasFocus);
                if (hasFocus && mWebSearchText.length() > 0) {
                    mIvClearInput.setVisibility(View.VISIBLE);
                    mIvWebIcon.setVisibility(View.INVISIBLE);
                    mEtSearchInput.setText(mWebSearchText);
                } else {
                    mIvClearInput.setVisibility(View.INVISIBLE);
                    mIvWebIcon.setVisibility(View.VISIBLE);
                    mEtSearchInput.setText(mWebTitle);
                }
            }
        });
        mEtSearchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search();
                    OtherUtil.hideSoftInput(mMain, mEtSearchInput);
                    return true;
                }
                return false;
            }
        });
    }

    private void setEditTextEnable(boolean enable) {
        mEtSearchInput.setClickable(enable);
        mEtSearchInput.setFocusable(enable);
        mEtSearchInput.setFocusableInTouchMode(enable);
    }

    public void setWebProgress(int progress) {
        if (progress > 0 && progress <= 100) {
            mPbWebLoading.setProgress(progress);
        }
        if (progress == 100) {
            mPbWebLoading.setProgress(progress);
            mPbWebLoading.setVisibility(View.GONE);
            mEtSearchInput.setText(mWebTitle);
        }
    }

    public void onPageStarted(String url) {
        mWebSearchText = url;
        mEtSearchInput.setText(url);
        mPbWebLoading.setVisibility(View.VISIBLE);
        setEditTextEnable(false);
    }

    public void onPageFinished() {
        mPbWebLoading.setVisibility(View.GONE);
        mEtSearchInput.setText(mWebTitle);
        setEditTextEnable(true);
        mIsLoading = false;
    }

    public void setWebTitle(String title) {
        mWebTitle = title;
        mEtSearchInput.setText(mWebTitle);
    }

    public void setWebIcon(Bitmap bitmap) {
        mIvWebIcon.setImageBitmap(bitmap);
        mIvWebIcon.setVisibility(View.VISIBLE);
    }

    public void setSearchType(@BrowserType int searchType) {
        mSearchType = searchType;
        if (mIsLoading) {
            search();
        }
    }

    private void search() {
        if (StringUtil.isEmpty(mWebSearchText)) {
            ToastUtil.showToast(R.string.str_please_input);
            return;
        }
        mIsLoading = true;
        mIvWebIcon.setVisibility(View.INVISIBLE);
        if (isValidUrl(mWebSearchText)) {
            SLog.d(TAG, "search: loadUrl = " + mWebSearchText);
            mMain.getComponent(WebContentCom.class).loadUrl(mWebSearchText);
        } else {
            String searchUrl = getSearchUrlByType();
            SLog.d(TAG, "search: searchUrl = " + searchUrl);
            mMain.getComponent(WebContentCom.class).loadUrl(searchUrl);
        }
    }

    private boolean isValidUrl(String url) {
        Pattern pattern = Pattern.compile("^(http|https)(://)([A-Za-z0-9.]++$|[A-Za-z0-9.]++[/?].*+)");
        Matcher mat = pattern.matcher(url.trim());
        return mat.matches();
    }

    private String getSearchUrlByType() {
        String url = null;
        switch (mSearchType) {
            case BROWSER_TYPE_BAIDU:
                url = BROWSER_URL_BAIDU + mWebSearchText;
                break;
            case BROWSER_TYPE_BING:
                url = BROWSER_URL_BING + mWebSearchText;
                break;
            case BROWSER_TYPE_SOGOU:
                url = BROWSER_URL_SOGOU + mWebSearchText;
                break;
            case BROWSER_TYPE_GOOGLE:
                url = BROWSER_URL_GOOGLE + mWebSearchText;
                break;
        }
        if (url == null) {
            url = BROWSER_URL_BAIDU + mWebSearchText;
        }
        return url;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_collect:
                break;
            case R.id.iv_clear_input:
                mEtSearchInput.setText("");
                mIvClearInput.setVisibility(View.INVISIBLE);
                break;
            case R.id.tv_search:
                search();
                break;
        }
    }

    @IntDef({BROWSER_TYPE_BAIDU, BROWSER_TYPE_BING, BROWSER_TYPE_SOGOU, BROWSER_TYPE_GOOGLE})
    public @interface BrowserType {

    }
}