package com.bear.wanandroidbyjava.Module.Web;

import android.view.View;

import com.bear.libcomponent.ComponentAct;
import com.bear.libcomponent.ComponentService;
import com.bear.libcomponent.ViewComponent;
import com.bear.wanandroidbyjava.R;
import com.example.libbase.Animator;
import com.example.libbase.Manager.KeyBoardManager;
import com.example.libbase.Util.ClipboardUtil;
import com.example.libbase.Util.DensityUtil;
import com.example.libbase.Util.ToastUtil;

public class WebLinkCom extends ViewComponent<ComponentAct> implements IWebLinkCom, View.OnClickListener {
    private View maskView;
    private View topLinkView;

    @Override
    protected void onCreate() {
        super.onCreate();
        maskView = findViewById(R.id.maskView);
        topLinkView = findViewById(R.id.topLinkView);
        clickListener(this, R.id.copyLinkTv, R.id.openWithBrowserTv, R.id.maskView);

        maskView.setFocusable(true);
        maskView.setFocusableInTouchMode(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.copyLinkTv:
                String copyText = ComponentService.get().getComponent(IWebInputCom.class).getWebLink();
                if (ClipboardUtil.copyToClipboard(copyText)) {
                    ToastUtil.showToast(R.string.str_link_already_copy);
                } else {
                    ToastUtil.showToast(R.string.str_link_copy_fail);
                }
                break;
            case R.id.openWithBrowserTv:
                break;
            default:
                break;
        }
        hideLinkView();
    }

    @Override
    public void showLinkView() {
        maskView.setVisibility(View.VISIBLE);
        topLinkView.setVisibility(View.VISIBLE);
        Animator.make(maskView, Animator.ALPHA, 0f, 0.5f).start();
        Animator.make(topLinkView, Animator.TRANSLATION_Y, -DensityUtil.dp2Px(40), 0f).start();
    }

    @Override
    public void hideLinkView() {
        maskView.requestFocus();
        KeyBoardManager.get().hideKeyBoard(getDependence(), topLinkView);
        Animator.make(maskView, Animator.ALPHA, 0.5f, 0f).statusAdapter(new Animator.StatusAdapter(){
            @Override
            public void onAnimationCancel(android.animation.Animator animation) {
                maskView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                maskView.setVisibility(View.GONE);
            }
        }).start();
        Animator.make(topLinkView, Animator.TRANSLATION_Y, 0f, -DensityUtil.dp2Px(40)).statusAdapter(new Animator.StatusAdapter(){
            @Override
            public void onAnimationCancel(android.animation.Animator animation) {
                topLinkView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                topLinkView.setVisibility(View.GONE);
            }
        }).start();
    }
}
