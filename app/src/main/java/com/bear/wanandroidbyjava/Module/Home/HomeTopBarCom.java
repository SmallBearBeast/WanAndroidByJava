package com.bear.wanandroidbyjava.Module.Home;

import android.view.View;

import com.bear.libcomponent.ComponentFrag;
import com.bear.libcomponent.ViewComponent;
import com.bear.wanandroidbyjava.R;
import com.bear.wanandroidbyjava.Module.Search.SearchAct;
import com.bear.wanandroidbyjava.Module.Square.SquareAct;
import com.example.libbase.OnProtectClickListener;

public class HomeTopBarCom extends ViewComponent<ComponentFrag> {

    @Override
    protected void onCreateView() {
        clickListener(new OnProtectClickListener() {
            @Override
            public void onProtectClick(View view) {
                switch (view.getId()) {
                    case R.id.iv_square:
                        SquareAct.go(getComActivity());
                        break;

                    case R.id.iv_search:
                        SearchAct.go(getComActivity());
                        break;
                }
            }
        }, R.id.iv_square, R.id.iv_search);
    }
}
