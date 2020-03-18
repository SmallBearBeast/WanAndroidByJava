package com.bear.wanandroidbyjava.Module.Home;

import android.view.View;

import com.bear.wanandroidbyjava.R;
import com.bear.wanandroidbyjava.Module.Search.SearchAct;
import com.bear.wanandroidbyjava.Module.Square.SquareAct;
import com.example.libframework.CoreUI.FragComponent;

public class HomeTopBarCom extends FragComponent {

    @Override
    protected void onCreateView() {
        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.iv_square:
                        mMain.mComActivity.goAct(SquareAct.class);
                        break;

                    case R.id.iv_search:
                        mMain.mComActivity.goAct(SearchAct.class);
                        break;
                }
            }
        }, R.id.iv_square, R.id.iv_search);
    }
}
