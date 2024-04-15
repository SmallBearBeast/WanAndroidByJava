package com.bear.wanandroidbyjava.Module.Web;

import android.graphics.Bitmap;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;

import com.bear.libcomponent.ShareVM;
import com.bear.libcomponent.component.ActivityComponent;
import com.bear.wanandroidbyjava.Module.Collect.CollectInfo;
import com.bear.wanandroidbyjava.Module.Collect.CollectArticleView;
import com.bear.wanandroidbyjava.R;
import com.example.libbase.Manager.KeyBoardManager;
import com.example.libbase.Util.StringUtil;
import com.example.libbase.Util.ToastUtil;
import com.example.libframework.Wrapper.TextWatcherWrapper;
import com.example.liblog.SLog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebInputCom extends ActivityComponent implements View.OnClickListener {
    private static final String TAG = WebAct.TAG + "-WebInputCom";
    private static final int LOADING_PROGRESS_DONE = 100;
    private int webProgress;

    private ImageView webIconIv;
    private EditText searchInputEt;
    private ImageView clearInputIv;
    private ProgressBar webLoadingPb;
    private String webLink;
    private String webTitle;

    public WebInputCom(Lifecycle lifecycle) {
        super(lifecycle);
    }

    @Override
    protected void onCreate() {
        CollectInfo collectInfo = ShareVM.get(getActivity(), WebAct.KEY_WEB_COLLECT_INFO);
        webLink = collectInfo.getLink();
        webTitle = collectInfo.getTitle();
        searchInputEt = findViewById(R.id.et_search_input);
        clearInputIv = findViewById(R.id.iv_clear_input);
        webLoadingPb = findViewById(R.id.pb_web_loading);
        webIconIv = findViewById(R.id.iv_web_icon);
        CollectArticleView collectArticleView = findViewById(R.id.collectArticleView);
        collectArticleView.setCollectInfo(collectInfo);
        setUpEditText();
        setOnClickListener(this, R.id.iv_clear_input, R.id.tv_search);
    }

    private void setUpEditText() {
        setEditTextEnable(false);
        searchInputEt.setText(webLink);
        searchInputEt.addTextChangedListener(new TextWatcherWrapper() {
            @Override
            public void afterTextChanged(Editable s) {
                boolean focus = searchInputEt.hasFocus();
                clearInputIv.setVisibility(focus && s.length() > 0 ? View.VISIBLE : View.INVISIBLE);
            }
        });
        searchInputEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                SLog.d(TAG, "hasFocus = " + hasFocus);
                if (hasFocus) {
                    clearInputIv.setVisibility(View.VISIBLE);
                    webIconIv.setVisibility(View.INVISIBLE);
                    searchInputEt.setText(webLink);
                    getComponent(WebLinkCom.class).showLinkView();
                } else {
                    clearInputIv.setVisibility(View.INVISIBLE);
                    webIconIv.setVisibility(View.VISIBLE);
                    searchInputEt.setText(webTitle);
                    getComponent(WebLinkCom.class).hideLinkView();
                }
            }
        });
        searchInputEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search();
                    return true;
                }
                return false;
            }
        });
    }

    private void setEditTextEnable(boolean enable) {
        searchInputEt.setClickable(enable);
        searchInputEt.setFocusable(enable);
        searchInputEt.setFocusableInTouchMode(enable);
    }

    public void setWebProgress(int progress) {
        webProgress = progress;
        if (webLoadingPb.getVisibility() == View.VISIBLE) {
            if (progress >= 0 && progress < LOADING_PROGRESS_DONE) {
                webLoadingPb.setProgress(progress);
            } else {
                onPageFinished();
            }
        }
    }

    public void onPageStarted(String url) {
        webLink = url;
        webLoadingPb.setVisibility(View.VISIBLE);
        setSearchInputTextWhenNoFocus(url);
        setEditTextEnable(false);
    }

    public void onPageFinished() {
        if (webProgress >= LOADING_PROGRESS_DONE) {
            webProgress = 0;
            webLoadingPb.setVisibility(View.GONE);
            setSearchInputTextWhenNoFocus(webTitle);
            setEditTextEnable(true);
        }
    }

    public void setWebTitle(String title) {
        if (isValidUrl(title)) {
            return;
        }
        webTitle = title;
        setSearchInputTextWhenNoFocus(webTitle);
    }

    public void setWebIcon(Bitmap bitmap) {
        if (!searchInputEt.hasFocus()) {
            webIconIv.setImageBitmap(bitmap);
            webIconIv.setVisibility(View.VISIBLE);
        }
    }

    private void search() {
        String inputUrl = String.valueOf(searchInputEt.getText());
        if (StringUtil.isEmpty(inputUrl)) {
            ToastUtil.showToast(R.string.str_input_net_address);
            return;
        }
        if (isValidUrl(inputUrl)) {
            SLog.d(TAG, "search: inputUrl = " + inputUrl);
            webLink = inputUrl;
            webIconIv.setVisibility(View.INVISIBLE);
            KeyBoardManager.get().hideKeyBoard(getContext(), searchInputEt);
            setEditTextEnable(false);
            searchInputEt.setText(inputUrl);
            getComponent(WebContentCom.class).loadUrl(inputUrl);
        }
    }

    private boolean isValidUrl(String url) {
        Pattern pattern = Pattern.compile("^(http|https)(://)([A-Za-z0-9.]++$|[A-Za-z0-9.]++[/?].*+)");
        Matcher mat = pattern.matcher(url.trim());
        return mat.matches();
    }

    private void setSearchInputTextWhenNoFocus(String text) {
        String searchText = String.valueOf(searchInputEt.getText());
        if (!searchInputEt.hasFocus() && !text.equals(searchText)) {
            searchInputEt.setText(text);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_clear_input:
                clearInputIv.setVisibility(View.INVISIBLE);
                searchInputEt.setText("");
                break;
            case R.id.tv_search:
                search();
                break;
        }
    }

    public @Nullable String getWebLink() {
        return webLink;
    }
}