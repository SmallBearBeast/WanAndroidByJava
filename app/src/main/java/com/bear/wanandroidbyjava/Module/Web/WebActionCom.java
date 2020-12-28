package com.bear.wanandroidbyjava.Module.Web;

import android.view.Gravity;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bear.libcomponent.ComponentAct;
import com.bear.libcomponent.ComponentService;
import com.bear.libcomponent.ViewComponent;
import com.bear.wanandroidbyjava.R;
import com.example.libbase.Util.DensityUtil;

public class WebActionCom extends ViewComponent<ComponentAct> implements View.OnClickListener{
    private static final String TAG = WebAct.TAG + "-WebActionCom";
    private BrowserSelectWindow mBrowserSelectWindow;

    private ConstraintLayout mClActionContainer;
    @Override
    protected void onCreate() {
        mClActionContainer = findViewById(R.id.cl_action_container);
        clickListener(this, R.id.iv_cancel, R.id.iv_browser_type, R.id.iv_more, R.id.iv_forward);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_cancel:
                getDependence().finish();
                break;

            case R.id.iv_browser_type:
                if (mBrowserSelectWindow == null) {
                    mBrowserSelectWindow = new BrowserSelectWindow(getDependence(), DensityUtil.getScreenWidth(), DensityUtil.dp2Px(45));
                }
                mBrowserSelectWindow.showAtLocation(mClActionContainer, Gravity.BOTTOM, 0, 0);
                mBrowserSelectWindow.setBrowserSelectListener(new BrowserSelectWindow.BrowserSelectListener() {
                    @Override
                    public void onBrowserType(int browserType) {
                        ComponentService.get().getComponent(WebInputCom.class).setSearchType(browserType);
                    }
                });
                break;

            case R.id.iv_more:
                // TODO: 2020-03-19 show bottom dialog
                break;

            case R.id.iv_forward:
                ComponentService.get().getComponent(WebContentCom.class).goForward();
                break;
        }
    }
}
