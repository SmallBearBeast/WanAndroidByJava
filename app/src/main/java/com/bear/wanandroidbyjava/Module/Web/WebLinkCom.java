package com.bear.wanandroidbyjava.Module.Web;

import android.view.View;

import androidx.lifecycle.Lifecycle;

import com.bear.libcomponent.component.ActivityComponent;
import com.bear.wanandroidbyjava.R;
import com.example.libbase.Animator;
import com.example.libbase.Manager.KeyBoardManager;
import com.example.libbase.Util.ClipboardUtil;
import com.example.libbase.Util.DensityUtil;
import com.example.libbase.Util.ToastUtil;

public class WebLinkCom extends ActivityComponent implements View.OnClickListener {
    private View maskView;
    private View topLinkView;

    public WebLinkCom(Lifecycle lifecycle) {
        super(lifecycle);
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        maskView = findViewById(R.id.maskView);
        topLinkView = findViewById(R.id.topLinkView);
        setOnClickListener(this, R.id.copyLinkTv, R.id.openWithBrowserTv, R.id.maskView);

        maskView.setFocusable(true);
        maskView.setFocusableInTouchMode(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.copyLinkTv:
                String copyText = getComponent(WebInputCom.class).getWebLink();
                if (ClipboardUtil.copy(copyText)) {
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

    public void showLinkView() {
        maskView.setVisibility(View.VISIBLE);
        topLinkView.setVisibility(View.VISIBLE);
        Animator.make(maskView, Animator.ALPHA, 0f, 0.5f).start();
        Animator.make(topLinkView, Animator.TRANSLATION_Y, -DensityUtil.dp2Px(40), 0f).start();
    }

    public void hideLinkView() {
        maskView.requestFocus();
        KeyBoardManager.get().hideKeyBoard(getContext(), topLinkView);
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
