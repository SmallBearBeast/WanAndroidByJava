package com.bear.wanandroidbyjava.Module.Login;

import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bear.wanandroidbyjava.Controller.Callback.CtrlLoginCallback;
import com.bear.wanandroidbyjava.Controller.Callback.CtrlRegisterCallback;
import com.bear.wanandroidbyjava.Controller.Callback.CtrlUserDataCallback;
import com.bear.wanandroidbyjava.Controller.LoginRegisterController;
import com.bear.wanandroidbyjava.Controller.UserDataController;
import com.bear.wanandroidbyjava.Data.Bean.UserDataBean;
import com.bear.wanandroidbyjava.Data.Bean.UserInfoBean;
import com.bear.wanandroidbyjava.Net.WanUrl;
import com.bear.wanandroidbyjava.R;
import com.example.libbase.Util.ResourceUtil;
import com.example.libbase.Util.StringUtil;
import com.example.libbase.Util.ToastUtil;

public class LoginRegisterVM extends ViewModel {
    private final LoginRegisterController loginRegisterController = new LoginRegisterController();
    private final UserDataController userDataController = new UserDataController();

    private final MutableLiveData<Pair<Boolean, UserInfoBean>> loginStateLD = new MutableLiveData<>();
    private final MutableLiveData<Pair<Boolean, UserInfoBean>> registerStateLD = new MutableLiveData<>();
    private final MutableLiveData<UserDataBean> fetchUserDataResultLD = new MutableLiveData<>();

    public void login(String userName, String password) {
        loginRegisterController.login(userName, password, new CtrlLoginCallback() {
            @Override
            public void onSuccess(UserInfoBean userInfoBean) {
                loginStateLD.setValue(new Pair<>(true, userInfoBean));
            }

            @Override
            public void onFail(int errorCode) {
                loginStateLD.setValue(new Pair<>(false, null));
            }
        });
    }

    public void register(String userName, String password, String rePassword) {
        loginRegisterController.register(userName, password, rePassword, new CtrlRegisterCallback() {
            @Override
            public void onSuccess(UserInfoBean userInfoBean) {
                ToastUtil.showToast(ResourceUtil.getString(R.string.str_register_success));
                registerStateLD.setValue(new Pair<>(true, userInfoBean));
            }

            @Override
            public void onFail(int errorCode) {
                String errorMsg = "";
                if (StringUtil.isEmpty(errorMsg)) {
                    errorMsg = ResourceUtil.getString(R.string.str_register_fail);
                }
                ToastUtil.showToast(errorMsg);
            }
        });
    }

    public void fetchUserData() {
        userDataController.fetchUserData(new CtrlUserDataCallback() {
            @Override
            public void onSuccess(UserDataBean userData) {
                if (userData != null) {
                    fetchUserDataResultLD.setValue(userData);
                }
            }

            @Override
            public void onFail(int errorCode) {

            }
        });
    }

    public LiveData<Pair<Boolean, UserInfoBean>> getLoginStateLD() {
        return loginStateLD;
    }

    public LiveData<Pair<Boolean, UserInfoBean>> getRegisterSuccessLD() {
        return registerStateLD;
    }

    public LiveData<UserDataBean> getFetchUserDataResultLD() {
        return fetchUserDataResultLD;
    }
}
