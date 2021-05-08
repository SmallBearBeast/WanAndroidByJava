package com.bear.wanandroidbyjava.Manager;

import com.bear.wanandroidbyjava.Data.NetBean.LoginBean;
import com.bear.wanandroidbyjava.Net.NetUrl;
import com.bear.wanandroidbyjava.Net.WanOkCallback;
import com.bear.wanandroidbyjava.Net.WanResponce;
import com.bear.wanandroidbyjava.Net.WanTypeToken;
import com.example.libbase.Executor.MainThreadExecutor;
import com.example.libokhttp.OkHelper;

import java.util.HashMap;
import java.util.Map;

public class LoginRegisterManager {
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String REPASSWORD = "repassword";

    public void login(String userName, String password, final ILoginListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put(USERNAME, userName);
        map.put(PASSWORD, password);
        OkHelper.getInstance().postMethod(NetUrl.LOGIN, map, new WanOkCallback<LoginBean>(WanTypeToken.LOGIN_TOKEN) {
            @Override
            protected void onSuccess(WanResponce<LoginBean> data) {
                if (data != null) {
                    if (data.errorCode == NetUrl.SUCCESS_ERROR_CODE) {
                        callOnLoginSuccess();
                    } else {
                        callOnLoginFail(data.errorMsg);
                    }
                }
            }

            @Override
            protected void onFail() {
                callOnLoginFail("");
            }

            private void callOnLoginFail(final String errorMsg) {
                if (listener != null) {
                    MainThreadExecutor.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onLoginFail(errorMsg);
                        }
                    });
                }
            }

            private void callOnLoginSuccess() {
                if (listener != null) {
                    MainThreadExecutor.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onLoginSuccess();
                        }
                    });
                }
            }
        });
    }

    public void register(String userName, String password, String rePassword, final IRegisterListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put(USERNAME, userName);
        map.put(PASSWORD, password);
        map.put(REPASSWORD, rePassword);
        OkHelper.getInstance().postMethod(NetUrl.REGISTER, map, new WanOkCallback<LoginBean>(WanTypeToken.LOGIN_TOKEN) {
            @Override
            protected void onSuccess(WanResponce<LoginBean> data) {
                if (data != null) {
                    if (data.errorCode == NetUrl.SUCCESS_ERROR_CODE) {
                        callOnRegisterSuccess();
                    } else {
                        callOnRegisterFail(data.errorMsg);
                    }
                }
            }

            @Override
            protected void onFail() {
                callOnRegisterFail("");
            }

            private void callOnRegisterFail(final String errorMsg) {
                if (listener != null) {
                    MainThreadExecutor.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onRegisterFail(errorMsg);
                        }
                    });
                }
            }

            private void callOnRegisterSuccess() {
                if (listener != null) {
                    MainThreadExecutor.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onRegisterSuccess();
                        }
                    });
                }
            }
        });
    }

    public void logout(final ILogoutListener listener) {
        OkHelper.getInstance().getMethod(NetUrl.LOGOUT, new WanOkCallback<String>(WanTypeToken.LOGOUT_TOKEN) {
            @Override
            protected void onSuccess(WanResponce<String> data) {
                if (data != null) {
                    if (data.errorCode == NetUrl.SUCCESS_ERROR_CODE) {
                        callOnLogoutSuccess();
                    } else {
                        callOnLogoutFail();
                    }
                }
            }

            @Override
            protected void onFail() {
                callOnLogoutFail();
            }

            private void callOnLogoutFail() {
                if (listener != null) {
                    listener.onLogoutFail();
                }
            }

            private void callOnLogoutSuccess() {
                if (listener != null) {
                    listener.onLogoutSuccess();
                }
            }
        });
    }

    public interface ILoginListener {
        void onLoginSuccess();

        void onLoginFail(String errorMsg);
    }

    public interface IRegisterListener {
        void onRegisterSuccess();

        void onRegisterFail(String errorMsg);
    }

    public interface ILogoutListener {
        void onLogoutSuccess();

        void onLogoutFail();
    }
}
