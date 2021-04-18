package com.bear.wanandroidbyjava.Module.Web;

import android.view.View;
import com.bear.libcomponent.ComponentAct;
import com.bear.libcomponent.ComponentService;
import com.bear.libcomponent.ViewComponent;
import com.bear.wanandroidbyjava.R;

public class WebActionCom extends ViewComponent<ComponentAct> implements View.OnClickListener {
    private static final String TAG = WebAct.TAG + "-WebActionCom";

    @Override
    protected void onCreate() {
        clickListener(this, R.id.iv_cancel, R.id.iv_more, R.id.iv_forward);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_cancel:
                getDependence().finish();
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
