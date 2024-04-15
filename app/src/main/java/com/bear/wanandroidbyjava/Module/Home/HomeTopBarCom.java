package com.bear.wanandroidbyjava.Module.Home;

import android.view.View;

import androidx.lifecycle.Lifecycle;

import com.bear.libcomponent.component.FragmentComponent;
import com.bear.wanandroidbyjava.Module.Login.LoginRegisterAct;
import com.bear.wanandroidbyjava.R;
import com.bear.wanandroidbyjava.Module.Search.SearchAct;
import com.bear.wanandroidbyjava.Module.Square.SquareAct;
import com.example.libbase.OnProtectClickListener;
import com.example.libbase.Util.ToastUtil;

public class HomeTopBarCom extends FragmentComponent {

    public HomeTopBarCom(Lifecycle lifecycle) {
        super(lifecycle);
    }

    @Override
    protected void onCreateView() {
        setOnClickListener(new OnProtectClickListener() {
            @Override
            public void onProtectClick(View view) {
                switch (view.getId()) {
                    case R.id.iv_square:
                        ToastUtil.showToast(R.string.str_feature_not_open);
//                        SquareAct.go(getComActivity());
                        break;

                    case R.id.iv_search:
                        ToastUtil.showToast(R.string.str_feature_not_open);
//                        SearchAct.go(getComActivity());
                        break;
                }
            }
        }, R.id.iv_square, R.id.iv_search);
    }
}
