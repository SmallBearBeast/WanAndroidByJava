package com.bear.wanandroidbyjava.Module.Web;

import android.view.Gravity;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bear.wanandroidbyjava.R;
import com.example.libbase.Util.DensityUtil;
import com.example.libframework.CoreUI.ActComponent;

public class WebActionCom extends ActComponent implements View.OnClickListener{
    private static final String TAG = WebAct.TAG + "-WebActionCom";
    private BrowserSelectWindow mBrowserSelectWindow;

    private ConstraintLayout mClActionContainer;
    @Override
    protected void onCreate() {
        mClActionContainer = findViewById(R.id.cl_action_container);
        setOnClickListener(this, R.id.iv_cancel, R.id.iv_browser_type, R.id.iv_more, R.id.iv_forward);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_cancel:
                mMain.finish();
                break;

            case R.id.iv_browser_type:
                if (mBrowserSelectWindow == null) {
                    mBrowserSelectWindow = new BrowserSelectWindow(mMain, DensityUtil.getScreenWidth(), DensityUtil.dp2Px(45));
                }
                mBrowserSelectWindow.showAtLocation(mClActionContainer, Gravity.BOTTOM, 0, 0);
                mBrowserSelectWindow.setBrowserSelectListener(new BrowserSelectWindow.BrowserSelectListener() {
                    @Override
                    public void onBrowserType(int browserType) {
                        mMain.getComponent(WebInputCom.class).setSearchType(browserType);
                    }
                });
                break;

            case R.id.iv_more:
                // TODO: 2020-03-19 show bottom dialog
                break;

            case R.id.iv_forward:
                mMain.getComponent(WebContentCom.class).goForward();
                break;
        }
    }
}
