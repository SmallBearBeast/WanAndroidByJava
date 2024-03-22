package com.bear.wanandroidbyjava.Module.Web;

import android.content.res.ColorStateList;
import android.view.View;
import android.widget.ImageView;

import com.bear.libcomponent.component.ActivityComponent;
import com.bear.libcomponent.component.ComponentService;
import com.bear.wanandroidbyjava.R;
import com.example.libbase.Util.ResourceUtil;
import com.example.libbase.Util.ViewUtil;

public class WebActionCom extends ActivityComponent implements View.OnClickListener {
    private static final String TAG = WebAct.TAG + "-WebActionCom";

    private ImageView forWardIv;
    private ImageView backIv;
    private ImageView cancelIv;

    @Override
    protected void onCreate() {
        forWardIv = findViewById(R.id.iv_forward);
        backIv = findViewById(R.id.back_iv);
        cancelIv = findViewById(R.id.iv_cancel);
        setForwardEnable(false);
        setBackEnable(false);
        setOnClickListener(this, R.id.iv_cancel, R.id.iv_more, R.id.iv_forward, R.id.back_iv);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_cancel:
                getActivity().finish();
                break;

            case R.id.iv_more:
                new ActionBottomView(getActivity()).show();
                break;

            case R.id.iv_forward:
                ComponentService.get().getComponent(WebContentCom.class).goForward();
                break;

            case R.id.back_iv:
                ComponentService.get().getComponent(WebContentCom.class).goBack();
                break;
        }
    }

    public void setForwardEnable(boolean enable) {
        int color = ResourceUtil.getColor(enable ? R.color.color_5C5C5C : R.color.color_AAAAAA);
        forWardIv.setEnabled(enable);
        forWardIv.setImageTintList(ColorStateList.valueOf(color));
    }

    public void setBackEnable(boolean enable) {
        ViewUtil.gone(backIv, cancelIv);
        ViewUtil.visible(enable ? backIv : cancelIv);
    }
}
