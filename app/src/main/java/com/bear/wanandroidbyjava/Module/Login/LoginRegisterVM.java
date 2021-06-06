package com.bear.wanandroidbyjava.Module.Login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bear.wanandroidbyjava.Controller.LoginRegisterController;
import com.bear.wanandroidbyjava.R;
import com.example.libbase.Util.NetWorkUtil;
import com.example.libbase.Util.ResourceUtil;
import com.example.libbase.Util.StringUtil;
import com.example.libbase.Util.ToastUtil;

public class LoginRegisterVM extends ViewModel {
    private final LoginRegisterController loginRegisterController = new LoginRegisterController();
    private final MutableLiveData<Boolean> loginSuccessLD = new MutableLiveData<>();
    private final MutableLiveData<Boolean> registerSuccessLD = new MutableLiveData<>();

    public void login(String userName, String password) {
        if (!NetWorkUtil.isConnected()) {
            ToastUtil.showToast(R.string.str_net_error_to_try_again);
            return;
        }
        loginRegisterController.login(userName, password, new LoginRegisterController.ILoginListener() {
            @Override
            public void onLoginSuccess() {
                ToastUtil.showToast(ResourceUtil.getString(R.string.str_login_success));
                loginSuccessLD.setValue(true);
            }

            @Override
            public void onLoginFail(String errorMsg) {
                if (StringUtil.isEmpty(errorMsg)) {
                    errorMsg = ResourceUtil.getString(R.string.str_login_fail);
                }
                ToastUtil.showToast(errorMsg);
            }
        });
    }

    public void register(String userName, String password, String rePassword) {
        if (!NetWorkUtil.isConnected()) {
            ToastUtil.showToast(R.string.str_net_error_to_try_again);
            return;
        }
        loginRegisterController.register(userName, password, rePassword, new LoginRegisterController.IRegisterListener() {
            @Override
            public void onRegisterSuccess() {
                ToastUtil.showToast(ResourceUtil.getString(R.string.str_register_success));
                registerSuccessLD.setValue(true);
            }

            @Override
            public void onRegisterFail(String errorMsg) {
                if (StringUtil.isEmpty(errorMsg)) {
                    errorMsg = ResourceUtil.getString(R.string.str_register_fail);
                }
                ToastUtil.showToast(errorMsg);
            }
        });
    }

    public void logout() {
        if (!NetWorkUtil.isConnected()) {
            ToastUtil.showToast(R.string.str_net_error_to_try_again);
            return;
        }
        loginRegisterController.logout(new LoginRegisterController.ILogoutListener() {
            @Override
            public void onLogoutSuccess() {
                // Do nothing
            }

            @Override
            public void onLogoutFail() {
                // Do nothing
            }
        });
    }

    public LiveData<Boolean> getLoginSuccessLD() {
        return loginSuccessLD;
    }

    public LiveData<Boolean> getRegisterSuccessLD() {
        return registerSuccessLD;
    }
}
