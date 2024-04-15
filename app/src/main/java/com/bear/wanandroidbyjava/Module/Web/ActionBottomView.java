package com.bear.wanandroidbyjava.Module.Web;

import android.app.Activity;
import android.view.View;

import com.bear.libcomponent.component.ComponentAct;
import com.bear.libcomponent.component.GroupComponent;
import com.bear.wanandroidbyjava.R;
import com.bear.wanandroidbyjava.Widget.BottomView;

public class ActionBottomView extends BottomView implements View.OnClickListener {
    private View homeWebItemView;
    private View collectItemView;
    private View bookmarkItemView;
    private View refreshItemView;
    private View shareItemView;
    private View quitItemView;

    public ActionBottomView(Activity activity) {
        super(activity);
        contentView(R.layout.view_action_bottom).dimEnable(true);
        homeWebItemView = findViewById(R.id.homeWebItemView);
        collectItemView = findViewById(R.id.collectItemView);
        bookmarkItemView = findViewById(R.id.bookmarkItemView);
        refreshItemView = findViewById(R.id.refreshItemView);
        shareItemView = findViewById(R.id.shareItemView);
        quitItemView = findViewById(R.id.quitItemView);
        homeWebItemView.setOnClickListener(this);
        collectItemView.setOnClickListener(this);
        bookmarkItemView.setOnClickListener(this);
        refreshItemView.setOnClickListener(this);
        shareItemView.setOnClickListener(this);
        quitItemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        WebContentCom webContentCom;
                switch (view.getId()) {
            case R.id.homeWebItemView:
                webContentCom = getComponent(WebContentCom.class);
                if (webContentCom != null) {
                    webContentCom.goBackHome();
                }
                break;
            case R.id.collectItemView:
                break;
            case R.id.bookmarkItemView:
                break;
            case R.id.refreshItemView:
                webContentCom = getComponent(WebContentCom.class);
                if (webContentCom != null) {
                    getComponent(WebContentCom.class).reload();
                }
                break;
            case R.id.shareItemView:
                break;
            case R.id.quitItemView:
                getActivity().finish();
                break;
        }
        hide();
    }

    private <C extends GroupComponent> C getComponent(Class<C> clz) {
        if (getActivity() instanceof ComponentAct) {
            ((ComponentAct)getActivity()).getComponentManager().getComponent(clz);
        }
        return null;
    }
}
