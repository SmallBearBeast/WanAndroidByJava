package com.bear.wanandroidbyjava.Module.Personal;

import android.view.View;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import com.bear.libcomponent.component.FragmentComponent;
import com.bear.wanandroidbyjava.Data.Bean.UserDataBean;
import com.bear.wanandroidbyjava.R;
import com.example.libbase.Util.ToastUtil;

public class PersonalItemCom extends FragmentComponent implements View.OnClickListener {

    private PersonalVM personalVM;

    private TextView integralCountTv;

    private TextView collectCountTv;

    @Override
    protected void onCreateView() {
        integralCountTv = findViewById(R.id.integralCountTv);
        collectCountTv = findViewById(R.id.collectCountTv);
        setOnClickListener(this, R.id.ll_my_integral, R.id.ll_my_share, R.id.ll_my_collect, R.id.ll_my_todo, R.id.ll_about_developer, R.id.ll_my_setting, R.id.logoutContainer);
        initViewModel();
    }

    private void initViewModel() {
        personalVM = new ViewModelProvider(getFragment()).get(PersonalVM.class);
        personalVM.getLoginStateLD().observe(getFragment(), this::updateItemByLoginState);
        personalVM.getFetchUserDataResultLD().observe(getFragment(), this::updateItemByUserData);
        personalVM.getLogoutResultLD().observe(getFragment(), logoutResult -> {
            if (logoutResult != null && !logoutResult) {
                ToastUtil.showToast(R.string.str_operation_failed_and_retry_again);
            }
        });
    }

    private void updateItemByLoginState(boolean loginState) {
        if (loginState) {
            integralCountTv.setVisibility(View.VISIBLE);
            collectCountTv.setVisibility(View.VISIBLE);
        } else {
            integralCountTv.setVisibility(View.GONE);
            collectCountTv.setVisibility(View.GONE);
        }
    }

    private void updateItemByUserData(UserDataBean userData) {
        if (userData != null) {
            integralCountTv.setText(userData.getCoinInfoBean().getCoinCount() + "");
            collectCountTv.setText(userData.getCollectArticleInfoBean().getCount() + "");
        }
    }

    public void updateItemByUserDataExt(UserDataBean userData) {
        updateItemByUserData(userData);
    }

    public void updateItemByLoginStateExt(boolean loginState) {
        updateItemByLoginState(loginState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_my_integral:
                break;
            case R.id.ll_my_share:
                break;
            case R.id.ll_my_collect:
                break;
            case R.id.ll_my_todo:
                break;
            case R.id.ll_about_developer:
                break;
            case R.id.ll_my_setting:
                break;
            case R.id.logoutContainer:
                personalVM.logout();
                break;
            default:
                break;
        }
    }
}
