package com.bear.wanandroidbyjava.Module.Web;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.bear.wanandroidbyjava.R;

public class BrowserSelectWindow extends PopupWindow implements View.OnClickListener{
    private Context mContext;
    private View mContentView;
    private ImageView mIvBaiduCheck;
    private ImageView mIvBingCheck;
    private ImageView mIvSougoCheck;
    private ImageView mIvGoogleCheck;

    private BrowserSelectListener mBrowserSelectListener;

    public BrowserSelectWindow(Context context, int width, int height) {
        super(width, height);
        mContext = context;
        mContentView = LayoutInflater.from(mContext).inflate(R.layout.window_browser_select, null);
        setContentView(mContentView);
        setBackgroundDrawable(new ColorDrawable());
        setFocusable(true);
        setOutsideTouchable(true);
        mIvBaiduCheck = mContentView.findViewById(R.id.iv_baidu_check);
        mIvBingCheck = mContentView.findViewById(R.id.iv_bing_check);
        mIvSougoCheck = mContentView.findViewById(R.id.iv_sougo_check);
        mIvGoogleCheck = mContentView.findViewById(R.id.iv_google_check);
        mContentView.findViewById(R.id.fl_baidu).setOnClickListener(this);
        mContentView.findViewById(R.id.fl_bing).setOnClickListener(this);
        mContentView.findViewById(R.id.fl_sougo).setOnClickListener(this);
        mContentView.findViewById(R.id.fl_google).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mIvBaiduCheck.setVisibility(View.GONE);
        mIvBingCheck.setVisibility(View.GONE);
        mIvSougoCheck.setVisibility(View.GONE);
        mIvGoogleCheck.setVisibility(View.GONE);
        int browserType = WebInputCom.BROWSER_TYPE_BAIDU;
        switch (v.getId()) {
            case R.id.fl_baidu:
                mIvBaiduCheck.setVisibility(View.VISIBLE);
                browserType = WebInputCom.BROWSER_TYPE_BAIDU;
                break;

            case R.id.fl_bing:
                mIvBingCheck.setVisibility(View.VISIBLE);
                browserType = WebInputCom.BROWSER_TYPE_BING;
                break;

            case R.id.fl_sougo:
                mIvSougoCheck.setVisibility(View.VISIBLE);
                browserType = WebInputCom.BROWSER_TYPE_SOGOU;
                break;

            case R.id.fl_google:
                mIvGoogleCheck.setVisibility(View.VISIBLE);
                browserType = WebInputCom.BROWSER_TYPE_GOOGLE;
                break;
        }
        if (mBrowserSelectListener != null) {
            mBrowserSelectListener.onBrowserType(browserType);
        }
        dismiss();
    }

    public void setBrowserSelectListener(BrowserSelectListener browserSelectListener) {
        mBrowserSelectListener = browserSelectListener;
    }

    public interface BrowserSelectListener {
        void onBrowserType(@WebInputCom.BrowserType int browserType);
    }
}
