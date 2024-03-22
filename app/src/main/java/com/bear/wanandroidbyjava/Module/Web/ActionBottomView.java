package com.bear.wanandroidbyjava.Module.Web;

import android.app.Activity;
import android.view.View;

import com.bear.libcomponent.component.ComponentService;
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
        switch (view.getId()) {
            case R.id.homeWebItemView:
                ComponentService.get().getComponent(WebContentCom.class).goBackHome();
                break;
            case R.id.collectItemView:
                break;
            case R.id.bookmarkItemView:
                break;
            case R.id.refreshItemView:
                ComponentService.get().getComponent(WebContentCom.class).reload();
                break;
            case R.id.shareItemView:
                break;
            case R.id.quitItemView:
                getActivity().finish();
                break;
        }
        hide();
    }
}
