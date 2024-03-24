package com.bear.wanandroidbyjava.Module.Personal;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bear.wanandroidbyjava.Controller.Callback.CtrlLogoutCallback;
import com.bear.wanandroidbyjava.Controller.Callback.CtrlUserDataCallback;
import com.bear.wanandroidbyjava.Controller.LoginRegisterController;
import com.bear.wanandroidbyjava.Controller.UserDataController;
import com.bear.wanandroidbyjava.Data.Bean.UserDataBean;
import com.bear.wanandroidbyjava.Net.WanUrl;

public class PersonalVM extends ViewModel {
    private final UserDataController userDataController = new UserDataController();
    private final LoginRegisterController loginRegisterController = new LoginRegisterController();

    private final MutableLiveData<UserDataBean> fetchUserDataResultLD = new MutableLiveData<>();
    private final MutableLiveData<Boolean> logoutResultLD = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loginStateLD = new MutableLiveData<>();

    public void fetchUserData() {
        userDataController.fetchUserData(new CtrlUserDataCallback() {
            @Override
            public void onSuccess(UserDataBean userData) {
                if (userData != null) {
                    loginStateLD.setValue(true);
                    fetchUserDataResultLD.setValue(userData);
                }
            }

            @Override
            public void onFail(int errorCode) {
                if (errorCode == WanUrl.NEED_TO_LOGIN_CODE) {
                    loginStateLD.setValue(false);
                }
            }
        });
    }

    public void logout() {
        loginRegisterController.logout(new CtrlLogoutCallback() {
            @Override
            public void onSuccess() {
                logoutResultLD.setValue(true);
                loginStateLD.setValue(false);
            }

            @Override
            public void onFail(int errorCode) {
                logoutResultLD.setValue(false);
            }
        });
    }

    public LiveData<UserDataBean> getFetchUserDataResultLD() {
        return fetchUserDataResultLD;
    }

    public LiveData<Boolean> getLogoutResultLD() {
        return logoutResultLD;
    }

    public LiveData<Boolean> getLoginStateLD() {
        return loginStateLD;
    }
}
