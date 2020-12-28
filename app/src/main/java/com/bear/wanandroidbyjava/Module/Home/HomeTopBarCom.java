package com.bear.wanandroidbyjava.Module.Home;

import android.view.View;

import com.bear.libcomponent.ComponentFrag;
import com.bear.libcomponent.ViewComponent;
import com.bear.wanandroidbyjava.R;
import com.bear.wanandroidbyjava.Module.Search.SearchAct;
import com.bear.wanandroidbyjava.Module.Square.SquareAct;

public class HomeTopBarCom extends ViewComponent<ComponentFrag> implements View.OnClickListener {

    @Override
    protected void onCreateView() {
        clickListener(this, R.id.iv_square, R.id.iv_search);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_square:
                SquareAct.go(getComActivity());
                break;

            case R.id.iv_search:
                SearchAct.go(getComActivity());
                break;
        }
    }
}
